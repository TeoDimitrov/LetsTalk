package com.example.letstalk.domain.message;

import android.graphics.Bitmap;

import com.example.letstalk.utils.BitmapUtil;
import com.example.letstalk.utils.DateTimeUtil;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class ChatMessage {

    private String author;

    private String message;

    private String encodedImage;

    private String messageDate;

    @SuppressWarnings("unused")
    public ChatMessage() {
    }

    public ChatMessage(String message, String author, Date messageDate) {
        this.setAuthor(author);
        this.setMessage(message);
        this.setUTCDate(messageDate);
    }

    public ChatMessage(Bitmap encodedImage,String author, Date messageDate) {
        this.setAuthor(author);
        this.setEncodedImageFromFile(encodedImage);
        this.setUTCDate(messageDate);
    }

    public String getAuthor() {
        return this.author;
    }

    private void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return this.message;
    }


    private void setMessage(String message) {
        this.message = message;
    }

    public String getEncodedImage() {
        return this.encodedImage;
    }

    @Exclude
    public Bitmap getEncodedBitmapImage() {
        Bitmap decodedImage = BitmapUtil.decodeImage(this.encodedImage);
        return decodedImage;
    }

    public void setEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
    }

    @Exclude
    public void setEncodedImageFromFile (Bitmap bitmapImage) {
        String encodedImage = BitmapUtil.encodeImage(bitmapImage);
        this.encodedImage = encodedImage;
    }

    public String getMessageDate() {
        return this.messageDate;
    }

    private void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    @Exclude
    public String setUTCDate(Date messageDate) {
        String utcDate = DateTimeUtil.getUTCDateTime(messageDate);
        return this.messageDate = utcDate;
    }

    @Exclude
    public String getLocalTime() {
        String localTime = DateTimeUtil.getLocalTime(this.messageDate);
        return localTime;
    }
}