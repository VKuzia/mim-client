package com.mimteam.mimclient.client;

import com.mimteam.mimclient.App;

import java.util.ArrayList;

public class UserInfo {
    private Integer id;
    private String token = "";
    private final ArrayList<Integer> chatIds = new ArrayList<>();

    private App.Operable onChatListChanged;

    public UserInfo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void addChat(Integer chatId) {
        chatIds.add(chatId);
        if (onChatListChanged != null) {
            onChatListChanged.operate();
        }
    }

    public void removeChat(Integer chatId) {
        chatIds.remove(chatId);
        if (onChatListChanged != null) {
            onChatListChanged.operate();
        }
    }

    public ArrayList<Integer> getChatIds() {
        return chatIds;
    }

    public void setOnChatListChanged(App.Operable onChatListChanged) {
        this.onChatListChanged = onChatListChanged;
    }
}
