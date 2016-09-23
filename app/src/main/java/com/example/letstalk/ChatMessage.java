package com.example.letstalk;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by teodo on 16/09/2016.
 */
public class ChatMessage implements Message{

    private String author;
    private String message;
    private String time;

    @SuppressWarnings("unused")
    private ChatMessage(){

    }

    public ChatMessage(String message, String author) {
        this.setAuthor(author);
        this.setMessage(message);
        this.setInitialTime();
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
    public String getTime() {
        return this.time;
    }

    private void setInitialTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        this.time = simpleDateFormat.format(new Date());
    }
}
