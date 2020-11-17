package com.mimteam.mimclient.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MessageModel {
    private String userName;
    private String message;
    private Date dateTime;

    private final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH mm", Locale.ENGLISH);
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy MM dd", Locale.ENGLISH);

    public MessageModel(String userName, String message) {
        this(userName, message, new Date());
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
        return timeFormatter.format(dateTime);
    }

    public String getDateString() {
        return dateFormatter.format(dateTime);
    }
}
