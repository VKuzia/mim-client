package com.mimteam.mimclient;

import android.app.Application;

import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import com.mimteam.mimclient.client.HttpClient;
import com.mimteam.mimclient.client.HttpWrapper;
import com.mimteam.mimclient.client.MessagesStorage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.mimteam.mimclient.client.UserInfo;
import com.mimteam.mimclient.client.WsClient;
import com.mimteam.mimclient.models.dto.ChatDto;
import com.mimteam.mimclient.models.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private EventBus messagesEventBus;
    private MessagesStorage messagesStorage;

    private WsClient wsClient;
    private HttpWrapper httpWrapper;
    private UserInfo userInfo;

    private Integer openedChatId;

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        messagesEventBus = new EventBus();
        messagesStorage = new MessagesStorage();
        messagesEventBus.register(messagesStorage);

        userInfo = new UserInfo();
        wsClient = new WsClient(userInfo);
        httpWrapper = new HttpWrapper(new HttpClient(userInfo, getString(R.string.http_url)));
    }

    public void connectWebSocket() {
        wsClient.dispose();  // Release previous resources before new connection
        wsClient = new WsClient(userInfo);
        wsClient.setMessagesEventBus(messagesEventBus);
        wsClient.connect(getString(R.string.ws_endpoint), this::subscribeToChats);
    }

    private void subscribeToChats() {
        userInfo.clearChats();
        Optional<List<ChatDto>> chatsList = httpWrapper.getChatsList();
        if (!chatsList.isPresent()) {
            showNotification(this, "Error getting chat list", "ERROR");
            return;
        }
        for (ChatDto chat : chatsList.get()) {
            userInfo.addChat(chat);
            wsClient.subscribe(chat.getChatId());
            messagesStorage.addMessages(chat.getChatId(),
                    httpWrapper.getChatMessages(chat.getChatId()).or(new ArrayList<>()));
            Log.d("APP", "Subscribing to " + chat.getChatId() + "(" + chat.getChatName() + ")");
            Optional<List<UserDto>> userList = httpWrapper.getUserList(chat.getChatId());
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

    public void showYesNoDialog(Context context,
                                String message,
                                String title,
                                Operable onYes,
                                Operable onNo) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    onYes.operate();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    onNo.operate();
                    break;
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }

    public WsClient getWsClient() {
        return wsClient;
    }

    public HttpWrapper getHttpWrapper() {
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

    public static Context getAppContext() {
        return appContext;
    }
}
