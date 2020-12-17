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

import java.util.List;

public class App extends Application {
    private EventBus messagesEventBus;
    private MessagesStorage messagesStorage;

    private WSClient wsClient;
    private HTTPWrapper httpWrapper;
    private UserInfo userInfo;

    private Integer openedChatId;
    private String openedChatInvitationKey;

    @Override
    public void onCreate() {
        super.onCreate();
        messagesEventBus = new EventBus();
        messagesStorage = new MessagesStorage();
        messagesEventBus.register(messagesStorage);

        userInfo = new UserInfo();

        httpWrapper = new HTTPWrapper(new HTTPClient(userInfo, getString(R.string.local_http_url)));
    }

    public void connectWebSocket() {
        wsClient = new WSClient(userInfo);
        wsClient.setMessagesEventBus(messagesEventBus);
        wsClient.connect(getString(R.string.local_ws_endpoint), this::subscribeToChats);
    }

    public void subscribeToChats() {
        Optional<List<ChatDTO>> chatsList = httpWrapper.getChatsList();
        if (!chatsList.isPresent()) {
            showNotification(this, "Error getting chat list", "ERROR");
            return;
        }
        for (ChatDTO chat : chatsList.get()) {
            wsClient.subscribe(chat.getChatId());
            messagesStorage.addMessages(chat.getChatId(), httpWrapper.getChatMessages(chat.getChatId()).orNull());
            Log.i("APP", "Subscribing to " + chat.getChatId() + "(" + chat.getChatName() + ")");

            userInfo.addChat(chat);
            Optional<List<UserDTO>> userList = httpWrapper.getUserList(chat.getChatId());
            if (userList.isPresent()) {
                userInfo.addUsers(userList.get());
            }
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

    public String getOpenedChatInvitationKey() {
        return openedChatInvitationKey;
    }

    public void setOpenedChatId(Integer openedChatId) {
        this.openedChatId = openedChatId;
//        this.openedChatInvitationKey = httpWrapper.getInvitationKey(openedChatId).get();
    }
}
