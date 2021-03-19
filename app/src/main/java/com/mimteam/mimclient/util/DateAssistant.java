package com.mimteam.mimclient.util;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateAssistant {
    private static final String TIME_ZONE = "GMT+3";

    public static @NotNull String dateTimeToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        return formatter.format(date);
    }

    public static @NotNull String dateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy MM dd", Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        return formatter.format(date);
    }

    public static @NotNull String timeToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH mm", Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        return formatter.format(date);
    }
}
