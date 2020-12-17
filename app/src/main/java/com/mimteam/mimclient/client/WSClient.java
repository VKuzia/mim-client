package com.mimteam.mimclient.client;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.EventBus;
import com.mimteam.mimclient.App;
import com.mimteam.mimclient.models.dto.MessageDTO;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompCommand;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

public class WSClient {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private final String LOG_TAG = "WS_CLIENT";
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final ArrayList<Disposable> messages = new ArrayList<>();
    private final HashMap<Integer, Disposable> idToSubscription = new HashMap<>();
    private final UserInfo userInfo;
    private StompClient stompClient;
    private Disposable lifecycleSubscription;

    private EventBus messagesEventBus;
    private App.Operable onConnected;

    public WSClient(@NotNull UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public static @NotNull StompClient createStompClient(String url) {
        return createStompClient(url, ImmutableMap.of());
    }

    public static @NotNull StompClient createStompClient(String url, ImmutableMap<String, String> headers) {
        return Stomp.over(Stomp.ConnectionProvider.OKHTTP, url, headers)
                .withClientHeartbeat(1000)
                .withServerHeartbeat(1000);
    }

    public void connect(String url) {
        connect(url, () -> {});
    }

    public void connect(String url, App.Operable onConnected) {
        this.onConnected = onConnected;

        ImmutableMap<String, String> authHeaders =
                ImmutableMap.of(AUTHORIZATION_HEADER, TOKEN_PREFIX + userInfo.getToken());
        setStompClient(createStompClient(url, authHeaders));
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
                            onConnected.operate();
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
        stompClient.connect(createAuthorizationHeaders());
    }

    public void subscribe(Integer id) {
        if (stompClient == null || !stompClient.isConnected()) {
            return;
        }
        Log.d(LOG_TAG, "SUBSCRIBE TO " + id);
        String fullUrl = "/chats/" + id;
        Disposable disposableSubscription = stompClient.topic(fullUrl, createAuthorizationHeaders())
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
        subscriptionToDelete.dispose();
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
        List<StompHeader> headers = createAuthorizationHeaders();
        headers.add(new StompHeader(StompHeader.DESTINATION, "/app/chats/" + message.getChatId() + "/message"));
        StompMessage stompMessage = new StompMessage(StompCommand.SEND, headers, payload);

        Disposable disposableMessage = stompClient.send(stompMessage)
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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

    private void handleReceivedMessage(@NotNull StompMessage message) {
        Log.i(LOG_TAG, "\nRECEIVE:  " + message.getPayload() + "\n");
        try {
            MessageDTO dto = jsonMapper.readValue(message.getPayload(), MessageDTO.class);
            if (messagesEventBus != null) {
                messagesEventBus.post(dto);
            }
            Log.i(LOG_TAG, dto.getContent());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private @NotNull List<StompHeader> createAuthorizationHeaders() {
        ArrayList<StompHeader> stompHeaders = new ArrayList<>();
        stompHeaders.add(new StompHeader(AUTHORIZATION_HEADER, TOKEN_PREFIX + userInfo.getToken()));
        return stompHeaders;
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

    public void setMessagesEventBus(EventBus messagesEventBus) {
        this.messagesEventBus = messagesEventBus;
    }
}
