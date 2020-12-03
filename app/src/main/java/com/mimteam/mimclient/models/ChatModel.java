package com.mimteam.mimclient.models;

public class ChatModel {
    private MessageModel messageModel;
    private String chatName;

    public ChatModel(MessageModel messageModel, String chatName) {
        this.messageModel = messageModel;
        this.chatName = chatName;
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

    public String getChatName() {
        return chatName;
    }
}
