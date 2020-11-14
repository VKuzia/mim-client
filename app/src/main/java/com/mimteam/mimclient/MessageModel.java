package com.mimteam.mimclient;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageModel {
    private String userName;
    private String message;
    private LocalDateTime dateTime;

    public MessageModel(String userName, String message, LocalDateTime dateTime) {
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getTimeString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH mm");
        return dateTime.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDateString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd");
        return dateTime.format(formatter);
    }
}
