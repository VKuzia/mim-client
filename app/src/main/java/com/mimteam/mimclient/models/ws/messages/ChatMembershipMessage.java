package com.mimteam.mimclient.models.ws.messages;

import com.mimteam.mimclient.models.dto.MessageDto;
import com.mimteam.mimclient.models.ws.Transferable;

import org.jetbrains.annotations.NotNull;

public class ChatMembershipMessage implements Transferable {
    private Integer userId;
    private Integer chatId;
    private ChatMembershipMessageType chatMembershipMessageType;

    public enum ChatMembershipMessageType {
        JOIN, LEAVE
    }

    public ChatMembershipMessage(Integer userId, Integer chatId, ChatMembershipMessageType type) {
        this.userId = userId;
        this.chatId = chatId;
        this.chatMembershipMessageType = type;
    }

    @Override
    public MessageDto toDataTransferObject() {
        MessageDto dto = new MessageDto();
        dto.setMessageType(MessageDto.MessageType.CHAT_MEMBERSHIP_MESSAGE);
        dto.setUserId(userId);
        dto.setChatId(chatId);
        dto.setChatMembershipMessageType(chatMembershipMessageType);
        return dto;
    }

    @Override
    public void fromDataTransferObject(@NotNull MessageDto dto) {
        this.userId = dto.getUserId();
        this.chatId = dto.getChatId();
        this.chatMembershipMessageType = dto.getChatMembershipMessageType();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public ChatMembershipMessageType getChatMembershipMessageType() {
        return chatMembershipMessageType;
    }

    public void setChatMembershipMessageType(ChatMembershipMessageType chatMembershipMessageType) {
        this.chatMembershipMessageType = chatMembershipMessageType;
    }
}
