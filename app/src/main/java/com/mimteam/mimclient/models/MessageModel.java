package com.mimteam.mimclient.models;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageModel {
    private String userName;
    private String message;
    private Date dateTime;

    public MessageModel(String userName, String message) {
        this.userName = userName;
        this.message = message;
        this.dateTime = new Date();
    }

    public MessageModel(String userName, String message, Date dateTime) {
        this.userName = userName;
        this.message = message;
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public String getUserName() {
        return userName;
    }

    public String getTimeString() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("HH mm");
        return formatter.format(dateTime);
    }

    public String getDateString() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy MM dd");
        return formatter.format(dateTime);
    }
}
