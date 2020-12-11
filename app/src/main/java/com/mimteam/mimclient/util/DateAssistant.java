package com.mimteam.mimclient.util;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateAssistant {
    public static @NotNull String dateTimeToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        return formatter.format(date);
    }

    public static @NotNull String dateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy MM dd", Locale.getDefault());
        return formatter.format(date);
    }

    public static @NotNull String timeToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH mm", Locale.getDefault());
        return formatter.format(date);
    }
}
