package com.mimteam.mimclient.client;

import com.mimteam.mimclient.App;
import com.mimteam.mimclient.models.dto.ChatDTO;
import com.mimteam.mimclient.models.dto.UserDTO;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfo {
    private Integer id;
    private String token = "";
    private final ArrayList<ChatDTO> chats = new ArrayList<>();
    private final Map<Integer, String> userIdToName = new HashMap<>();

    private App.Operable onChatListChanged;

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

    public void removeChat(Integer chatId) {
        for (ChatDTO chatDTO : chats) {
            if (chatDTO.getChatId().equals(chatId)) {
                chats.remove(chatDTO);
                if (onChatListChanged != null) {
                    onChatListChanged.operate();
                }
                return;
            }
        }
    }

    public void clearChats() {
        chats.clear();
        if (onChatListChanged != null) {
            onChatListChanged.operate();
        }
    }

    public void updateUsers(@NotNull List<UserDTO> users) {
        for (UserDTO user : users) {
            userIdToName.put(user.getUserId(), user.getUserName());
        }
    }

    public ArrayList<ChatDTO> getChats() {
        return chats;
    }

    public void setOnChatListChanged(App.Operable onChatListChanged) {
        this.onChatListChanged = onChatListChanged;
    }

    public String getUserName(@NotNull Integer userId, String currentUserName) {
        return userId.equals(id) ? currentUserName : userIdToName.get(userId);
    }

    public String getNameById(Integer chatId) {
        for (ChatDTO chatDTO : chats) {
            if (chatDTO.getChatId().equals(chatId)) {
                return chatDTO.getChatName();
            }
        }
        return "";
    }
}
