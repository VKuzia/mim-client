package com.mimteam.mimclient.client;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mimteam.mimclient.models.ws.MessageDTO;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;

public class WSClient {
    private final String LOG_TAG = "WS_CLIENT";
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final ArrayList<Disposable> messages = new ArrayList<>();
    private final HashMap<Integer, Disposable> idToSubscription = new HashMap<>();
    private final UserInfo userInfo;
    private StompClient stompClient;
    private Disposable lifecycleSubscription;

    public WSClient(@NotNull UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public static @NotNull StompClient createStompClient(String url) {
        return Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)
                .withClientHeartbeat(1000)
                .withServerHeartbeat(1000);
    }

    public void connect(String url) {
        setStompClient(createStompClient(url));
        connect();
    }

    private void connect() {
        lifecycleSubscription = stompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.i(LOG_TAG, "Stomp connection opened");
                            break;
                        case ERROR:
                            Log.e(LOG_TAG, "Stomp connection error", lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            Log.i(LOG_TAG, "Stomp connection closed");
                            dispose();
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            Log.e(LOG_TAG, "Stomp failed server heartbeat");
                            break;
                    }
                });
        stompClient.connect();
    }

    public void subscribe(Integer id) {
        if (stompClient == null || !stompClient.isConnected()) {
            return;
        }
        Log.d(LOG_TAG, "SUBSCRIBE TO " + id);
        String fullUrl = "/chats/" + id;
        Disposable disposableSubscription = stompClient.topic(fullUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleReceivedMessage,
                        throwable -> Log.e(LOG_TAG, "ERROR on subscribe topic", throwable));
        idToSubscription.put(id, disposableSubscription);
    }

    public void unsubscribe(Integer id) {
        Log.d(LOG_TAG, "UNSUBSCRIBE FROM " + id);
        Disposable subscriptionToDelete = idToSubscription.get(id);
        Log.d(LOG_TAG, Objects.requireNonNull(subscriptionToDelete).toString());
        idToSubscription.remove(id);
    }

    public void sendMessage(@NotNull MessageDTO message) {
        if (stompClient == null || !stompClient.isConnected()) {
            return;
        }
        String payload;
        try {
            payload = jsonMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return;
        }
        Disposable disposableMessage = stompClient.send("/app/chats/" + message.getChatId() + "/message", payload)
                .compose(applySchedulers())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Log.i(LOG_TAG, "\nSEND: " + payload + "\n");
                        dispose();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(LOG_TAG, "ERROR send STOMP sending", e);
                        dispose();
                    }
                });
        messages.add(disposableMessage);
    }

    public void dispose() {
        idToSubscription.clear();
        if (lifecycleSubscription != null) {
            lifecycleSubscription.dispose();
        }
        for (Disposable item : messages) {
            item.dispose();
        }
        messages.clear();
    }

    @Contract(pure = true)
    private @NotNull CompletableTransformer applySchedulers() {
        return upstream -> upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void handleReceivedMessage(@NotNull StompMessage message) {
        Log.i(LOG_TAG, "\nRECEIVE:  " + message.getPayload() + "\n");
        try {
            MessageDTO dto = jsonMapper.readValue(message.getPayload(), MessageDTO.class);
            Log.i(LOG_TAG, dto.getContent());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void setStompClient(StompClient client) {
        if (stompClient != null && stompClient.isConnected()) {
            stompClient.disconnect();
            dispose();
        }
        stompClient = client;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
