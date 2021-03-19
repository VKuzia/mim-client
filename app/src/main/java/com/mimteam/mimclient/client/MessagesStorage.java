package com.mimteam.mimclient.client;

import com.google.common.eventbus.Subscribe;
import com.mimteam.mimclient.models.dto.MessageDto;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagesStorage {
    private final Map<Integer, List<MessageDto>> chatMessages = new HashMap<>();

    public List<MessageDto> getMessagesInChat(Integer chatId) {
        return getChatMessageList(chatId);
    }

    public MessageDto getLastMessageInChat(Integer chatId) {
        List<MessageDto> messageDtoList = chatMessages.get(chatId);
        if (messageDtoList == null || messageDtoList.isEmpty()) {
            return null;
        }
        return messageDtoList.get(messageDtoList.size() - 1);
    }

    @Subscribe
    public void handleReceivedMessage(@NotNull MessageDto messageDto) {
        getChatMessageList(messageDto.getChatId()).add(messageDto);
    }

    public void addMessages(Integer chatId, @NotNull List<MessageDto> messages) {
        getChatMessageList(chatId).addAll(messages);
    }

    private List<MessageDto> getChatMessageList(Integer chatId) {
        if (!chatMessages.containsKey(chatId)) {
            chatMessages.put(chatId, new ArrayList<>());
        }
        return chatMessages.get(chatId);
    }
}
