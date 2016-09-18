package com.example.letstalk;

/**
 * Created by teodo on 16/09/2016.
 */
public class ChatMessage implements Message{

    private String author;
    private String message;

    @SuppressWarnings("unused")
    private ChatMessage(){

    }

    public ChatMessage(String message, String author) {
        this.setAuthor(author);
        this.setMessage(message);
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
