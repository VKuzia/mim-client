package com.mimteam.mimclient.models;

import com.mimteam.mimclient.models.dto.ChatDto;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class ChatModel {
    private MessageModel messageModel;
    private String chatName;
    private Integer chatId;

    public ChatModel(MessageModel messageModel, @NotNull ChatDto chatDto) {
        this.messageModel = messageModel;
        this.chatName = chatDto.getChatName();
        this.chatId = chatDto.getChatId();
    }

    public String getMessage() {
        return messageModel.getMessage();
    }

    public String getUserName() {
        return messageModel.getUserName();
    }

    public String getTimeString() {
        return messageModel.getTimeString();
    }

    public String getDateString() {
        return messageModel.getDateString();
    }

    public Date getDateTime() {
        return messageModel.getDateTime();
    }

    public String getChatName() {
        return chatName;
    }

    public Integer getChatId() {
        return chatId;
    }
}
