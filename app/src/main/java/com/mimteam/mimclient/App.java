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
import com.mimteam.mimclient.models.dto.ChatDTO;
import com.mimteam.mimclient.models.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private EventBus messagesEventBus;
    private MessagesStorage messagesStorage;

    private WSClient wsClient;
    private HTTPWrapper httpWrapper;
    private UserInfo userInfo;

    private Integer openedChatId;

    @Override
    public void onCreate() {
        super.onCreate();
        messagesEventBus = new EventBus();
        messagesStorage = new MessagesStorage();
        messagesEventBus.register(messagesStorage);

        userInfo = new UserInfo();
        wsClient = new WSClient(userInfo);
        httpWrapper = new HTTPWrapper(new HTTPClient(userInfo, getString(R.string.http_url)));
    }

    public void connectWebSocket() {
        wsClient.dispose();  // Release previous resources before new connection
        wsClient = new WSClient(userInfo);
        wsClient.setMessagesEventBus(messagesEventBus);
        wsClient.connect(getString(R.string.ws_endpoint), this::subscribeToChats);
    }

    private void subscribeToChats() {
        userInfo.clearChats();
        Optional<List<ChatDTO>> chatsList = httpWrapper.getChatsList();
        if (!chatsList.isPresent()) {
            showNotification(this, "Error getting chat list", "ERROR");
            return;
        }
        for (ChatDTO chat : chatsList.get()) {
            userInfo.addChat(chat);
            wsClient.subscribe(chat.getChatId());
            messagesStorage.addMessages(chat.getChatId(),
                    httpWrapper.getChatMessages(chat.getChatId()).or(new ArrayList<>()));
            Log.d("APP", "Subscribing to " + chat.getChatId() + "(" + chat.getChatName() + ")");
            Optional<List<UserDTO>> userList = httpWrapper.getUserList(chat.getChatId());
            if (userList.isPresent()) {
                userInfo.updateUsers(userList.get());
            }
            // We need to handle else block somehow.
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
        showNotification(context, message, title, () -> { });
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
