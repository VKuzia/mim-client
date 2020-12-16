package com.mimteam.mimclient.client;

import com.google.common.eventbus.Subscribe;
import com.mimteam.mimclient.models.dto.MessageDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagesStorage {
    private final Map<Integer, List<MessageDTO>> chatMessages = new HashMap<>();

    public List<MessageDTO> getMessagesInChat(Integer chatId) {
        return getChatMessageList(chatId);
    }

    public MessageDTO getLastMessageInChat(Integer chatId) {
        if (!chatMessages.containsKey(chatId) || chatMessages.get(chatId).isEmpty()) {
            return null;
        }
        return chatMessages.get(chatId).get(chatMessages.get(chatId).size() - 1);
    }

    @Subscribe
    public void handleReceivedMessage(MessageDTO messageDTO) {
        getChatMessageList(messageDTO.getChatId()).add(messageDTO);
    }

    public void addMessages(Integer chatId, List<MessageDTO> messages) {
        if (messages == null) {
            return;
        }
        getChatMessageList(chatId).addAll(messages);
    }

    private List<MessageDTO> getChatMessageList(Integer chatId) {
        if (!chatMessages.containsKey(chatId)) {
            chatMessages.put(chatId, new ArrayList<>());
        }
        return chatMessages.get(chatId);
    }
}
