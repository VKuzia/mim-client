package com.mimteam.mimclient;

import android.app.Application;

import com.google.common.eventbus.EventBus;
import com.mimteam.mimclient.client.HTTPClient;
import com.mimteam.mimclient.client.HTTPWrapper;
import com.mimteam.mimclient.client.MessagesStorage;
import com.mimteam.mimclient.client.UserInfo;
import com.mimteam.mimclient.client.WSClient;

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

        userInfo = new UserInfo(1);
        userInfo.setToken("test_token");

        httpWrapper = new HTTPWrapper(new HTTPClient(userInfo, getString(R.string.local_http_url)));

        wsClient = new WSClient(userInfo);
        wsClient.setMessagesEventBus(messagesEventBus);

        wsClient.connect(getString(R.string.local_ws_endpoint));
    }

    public EventBus getMessagesEventBus() {
        return messagesEventBus;
    }

    public MessagesStorage getMessagesStorage() {
        return messagesStorage;
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
}
