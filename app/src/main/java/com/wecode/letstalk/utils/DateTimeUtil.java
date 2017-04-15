package com.wecode.letstalk.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtil {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MMM hh:mm a", Locale.ENGLISH);

    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

    public static String getUTCDateTime(Date date) {
        DateTimeUtil.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String utcDate = DateTimeUtil.dateFormat.format(date);
        return utcDate;
    }

    public static Date getUTCDateTimeFromDate(Date date) {
        DateTimeUtil.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String utcStringDate = DateTimeUtil.dateFormat.format(date);
        Date utcDate = null;
        try {
            utcDate = dateFormat.parse(utcStringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return utcDate;
    }

    public static String getLocalDateTime(String date) {
        DateTimeUtil.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date newDate = null;
        try {
            newDate = DateTimeUtil.dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String dateTime = DateTimeUtil.dateTimeFormat.format(newDate);
        return dateTime;
    }

    public static String getLocalTime(String date) {
        DateTimeUtil.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date newDate = null;
        try {
            newDate = DateTimeUtil.dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateTimeUtil.timeFormat.setTimeZone(TimeZone.getDefault());
        String time = DateTimeUtil.timeFormat.format(newDate);
        return time;
    }

    public static Date getUTCDateTime(String date) {
        DateTimeUtil.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date newDate = null;
        try {
            newDate = DateTimeUtil.dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return newDate;
    }
}
