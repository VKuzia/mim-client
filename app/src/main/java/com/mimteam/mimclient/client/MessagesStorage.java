package com.mimteam.mimclient.client;

import com.google.common.eventbus.Subscribe;
import com.mimteam.mimclient.models.ws.MessageDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagesStorage {
    private final Map<Integer, List<MessageDTO>> chatMessages = new HashMap<>();

    public List<MessageDTO> getMessagesInChat(Integer chatId) {
        return getChatMessageList(chatId);
    }

    @Subscribe
    public void handleReceivedMessage(MessageDTO messageDTO) {
        getChatMessageList(messageDTO.getChatId()).add(messageDTO);
    }

    private List<MessageDTO> getChatMessageList(Integer chatId) {
        if (!chatMessages.containsKey(chatId)) {
            chatMessages.put(chatId, new ArrayList<>());
        }
        return chatMessages.get(chatId);
    }
}
