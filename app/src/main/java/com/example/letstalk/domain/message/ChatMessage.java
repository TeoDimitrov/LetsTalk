package com.example.letstalk.domain.message;

import com.example.letstalk.domain.message.interfaces.Message;
import com.example.letstalk.domain.timeFrames.TimeFrame;
import com.example.letstalk.utils.DateTimeUtil;

import java.util.Calendar;

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
        this.setDate(DateTimeUtil.getUTCTime(calendar.getTime()));
        return this.getDate();
    }

    @Override
    public String getLocalTime() {
        String time = DateTimeUtil.getLocalTime(this.getDate());
        return time;
    }
}