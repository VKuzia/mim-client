package com.mimteam.mimclient;

import android.app.Application;

import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import com.mimteam.mimclient.client.HTTPClient;
import com.mimteam.mimclient.client.HTTPWrapper;
import com.mimteam.mimclient.client.MessagesStorage;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import com.mimteam.mimclient.client.UserInfo;
import com.mimteam.mimclient.client.WSClient;

import java.util.List;

public class App extends Application {
    private EventBus messagesEventBus;
    private MessagesStorage messagesStorage;

    private WSClient wsClient;
    private HTTPWrapper httpWrapper;
    private UserInfo userInfo;

    private Integer openedChatId = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        messagesEventBus = new EventBus();
        messagesStorage = new MessagesStorage();
        messagesEventBus.register(messagesStorage);

        userInfo = new UserInfo(-1);

        httpWrapper = new HTTPWrapper(new HTTPClient(userInfo, getString(R.string.local_http_url)));

        wsClient = new WSClient(userInfo);
        wsClient.setMessagesEventBus(messagesEventBus);
    }

    public void connectWebSocket() {
        wsClient.connect(getString(R.string.local_ws_endpoint), this::subscribeToChats);
    }

    public void subscribeToChats() {
        Optional<List<Integer>> chatsList = httpWrapper.getChatsList();
        if (!chatsList.isPresent()) {
            showNotification(this, "Error getting chat list", "ERROR");
            return;
        }
        for (Integer chatId : chatsList.get()) {
            wsClient.subscribe(chatId);
            userInfo.addChat(chatId);

            messagesStorage.addMessages(chatId, httpWrapper.getChatMessages(chatId).orNull());
            Log.i("APP", "Subscribing to " + chatId);
        }

    }

    public EventBus getMessagesEventBus() {
        return messagesEventBus;
    }

    public MessagesStorage getMessagesStorage() {
        return messagesStorage;
    }

    public interface Operable {
        void operate();
    }

    public void showNotification(Context context, String message, String title) {
        showNotification(context, message, title, () -> {});
    }

    public void showNotification(Context context, String message, String title, Operable onClose) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setTitle(title)
                .setPositiveButton("OK", (dialog, which) -> onClose.operate())
                .create()
                .show();
    }

    public WSClient getWsClient() {
        return wsClient;
    }

    public HTTPWrapper getHttpWrapper() {
        return httpWrapper;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public Integer getOpenedChatId() {
        return openedChatId;
    }

    public void setOpenedChatId(Integer openedChatId) {
        this.openedChatId = openedChatId;
    }
}
