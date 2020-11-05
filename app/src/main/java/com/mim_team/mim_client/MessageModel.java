package com.mim_team.mim_client;

public class MessageModel {
    public String userName;
    public String message;
    public String time;

    public MessageModel(String userName, String message, String time) {
        this.userName = userName;
        this.message = message;
        this.time = time;
    }
}
