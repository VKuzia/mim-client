package com.mimteam.mimclient.client;

import com.google.common.base.Optional;
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
        Optional<ChatDTO> chatDto = getChatDtoById(chatId);
        if (chatDto.isPresent()) {
            chats.remove(chatDto.get());
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

    public String getChatNameById(Integer chatId) {
        return getChatDtoById(chatId).or(new ChatDTO()).getChatName();
    }

    private Optional<ChatDTO> getChatDtoById(Integer chatId) {
        for (ChatDTO chatDto : chats) {
            if (chatDto.getChatId().equals(chatId)) {
                return Optional.of(chatDto);
            }
        }
        return Optional.absent();
    }
}
