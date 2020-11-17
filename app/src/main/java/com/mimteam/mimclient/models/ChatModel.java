package com.mimteam.mimclient.models;

public class ChatModel {
    private MessageModel messageModel;
    private String chatName;
    private String image;

    public ChatModel(MessageModel messageModel, String chatName, String image) {
        this.messageModel = messageModel;
        this.chatName = chatName;
        this.image = image;
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

    public String getImage() {
        return image;
    }
}
