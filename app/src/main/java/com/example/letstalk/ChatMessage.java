package com.example.letstalk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by teodo on 16/09/2016.
 */
public class ChatMessage implements Message {

    private String author;
    private String message;
    private String date;

    @SuppressWarnings("unused")
    private ChatMessage() {

    }

    public ChatMessage(String message, String author) {
        this.setAuthor(author);
        this.setMessage(message);
    }

    @Override
    public String getAuthor() {
        return this.author;
    }

    private void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String getMessage() {
        return this.message;
    }


    private void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getDate() {
        return this.date;
    }

    private void setDate(String date) {
        this.date = date;
    }


    @Override
    public String getUTCDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.setDate(dateFormat.format(calendar.getTime()));

        return this.getDate();
    }

    @Override
    public String getLocalTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = new Date();
        try {
            date = dateFormat.parse(this.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        timeFormat.setTimeZone(TimeZone.getDefault());
        String time = timeFormat.format(date);

        return time;
    }
}