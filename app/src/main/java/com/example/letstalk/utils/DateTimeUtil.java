package com.example.letstalk.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtil {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

    public static String getUTCTime(Date date){
        DateTimeUtil.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String utcDate = DateTimeUtil.dateFormat.format(date);
        return utcDate;
    }

    public static String getLocalTime(String date){
        DateTimeUtil.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date newDate = null;
        try {
            newDate = DateTimeUtil.dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateTimeUtil.timeFormat.setTimeZone(TimeZone.getDefault());
        String time = DateTimeUtil.timeFormat.format(date);
        return time;
    }
}
