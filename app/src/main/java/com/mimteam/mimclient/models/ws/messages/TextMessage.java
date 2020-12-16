package com.mimteam.mimclient.models.ws.messages;

import com.mimteam.mimclient.models.dto.MessageDTO;
import com.mimteam.mimclient.models.ws.Transferable;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class TextMessage implements Transferable {
    private Integer userId;
    private Integer chatId;
    private String content;
    private Date time;

    @Override
    public void fromDataTransferObject(@NotNull MessageDTO dto) {
        userId = dto.getUserId();
        chatId = dto.getChatId();
        content = dto.getContent();
        time = dto.getDateTime();
    }

    @Override
    public MessageDTO toDataTransferObject() {
        MessageDTO dto = new MessageDTO();
        dto.setMessageType(MessageDTO.MessageType.TEXT_MESSAGE);
        dto.setUserId(userId);
        dto.setChatId(chatId);
        dto.setContent(content);
        dto.setDateTime(time);
        return dto;
    }

    public TextMessage(Integer userId, Integer chatId, String content) {
        this.userId = userId;
        this.chatId = chatId;
        this.content = content;
        this.time = new Date();
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public @NotNull String toString() {
        return "[userId:" + userId + ", "
                + "chatId: " + chatId + ", "
                + "content: " + content + ", "
                + "time: " + time.toString() +"]";
    }
}

