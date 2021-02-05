package com.mimteam.mimclient.client;

import com.mimteam.mimclient.App;
import com.mimteam.mimclient.models.dto.ChatDto;
import com.mimteam.mimclient.models.dto.UserDto;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfo {
    private Integer id;
    private String token = "";
    private final ArrayList<ChatDto> chats = new ArrayList<>();
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

    public void addChat(ChatDto chat) {
        chats.add(chat);
        if (onChatListChanged != null) {
            onChatListChanged.operate();
        }
    }

    public void removeChat(Integer chatId) {
        ChatDto chatDto = getChatDtoById(chatId);
        if (chatDto != null) {
            chats.remove(chatDto);
            if (onChatListChanged != null) {
                onChatListChanged.operate();
            }
        }
    }

    public void clearChats() {
        chats.clear();
        if (onChatListChanged != null) {
            onChatListChanged.operate();
        }
    }

    public void updateUsers(@NotNull List<UserDto> users) {
        for (UserDto user : users) {
            userIdToName.put(user.getUserId(), user.getUserName());
        }
    }

    public ArrayList<ChatDto> getChats() {
        return chats;
    }

    public void setOnChatListChanged(App.Operable onChatListChanged) {
        this.onChatListChanged = onChatListChanged;
    }

    public String getUserName(@NotNull Integer userId, String currentUserName) {
        return userId.equals(id) ? currentUserName : userIdToName.get(userId);
    }

    public String getChatNameById(Integer chatId) {
        ChatDto chatDto = getChatDtoById(chatId);
        return chatDto != null ? chatDto.getChatName() : "";
    }

    private @Nullable ChatDto getChatDtoById(Integer chatId) {
        for (ChatDto chatDto : chats) {
            if (chatDto.getChatId().equals(chatId)) {
                return chatDto;
            }
        }
        return null;
    }
}
