package com.mimteam.mimclient.models;

import com.mimteam.mimclient.util.DateAssistant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MessageModel {
    private String userName;
    private String message;
    private Date dateTime;

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
        return DateAssistant.timeToString(dateTime);
    }

    public String getDateString() {
        return DateAssistant.dateToString(dateTime);
    }

    public Date getDateTime() {
        return dateTime;
    }
}
