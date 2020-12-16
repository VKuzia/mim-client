package com.mimteam.mimclient.client;

import com.mimteam.mimclient.App;
import com.mimteam.mimclient.models.dto.ChatDTO;

import java.util.ArrayList;

public class UserInfo {
    private Integer id;
    private String token = "";
    private final ArrayList<ChatDTO> chats = new ArrayList<>();

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

    public void addChat(ChatDTO chat) {
        chats.add(chat);
        if (onChatListChanged != null) {
            onChatListChanged.operate();
        }
    }

    public void removeChat(ChatDTO chat) {
        chats.remove(chat);
        if (onChatListChanged != null) {
            onChatListChanged.operate();
        }
    }

    public ArrayList<ChatDTO> getChats() {
        return chats;
    }

    public void setOnChatListChanged(App.Operable onChatListChanged) {
        this.onChatListChanged = onChatListChanged;
    }
}
