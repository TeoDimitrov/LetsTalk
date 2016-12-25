package com.wecode.letstalk.domain.message;

import android.graphics.Bitmap;

import com.wecode.letstalk.utils.BitmapUtil;
import com.wecode.letstalk.utils.DateTimeUtil;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class ChatMessage {

    private String author;

    private String message;

    private String encodedImage;

    private String messageDate;

    private String recipient;

    private ChatMessageStatus chatMessageStatus;

    @SuppressWarnings("unused")
    public ChatMessage() {
    }

    public ChatMessage(String message, String author, String recipient, Date messageDate) {
        this.chatMessageStatus = ChatMessageStatus.NEW;
        this.setAuthor(author);
        this.setMessage(message);
        this.setUTCDate(messageDate);
        this.setRecipient(recipient);
    }

    public ChatMessage(Bitmap encodedImage, String author, String recipient, Date messageDate) {
        this.chatMessageStatus = ChatMessageStatus.NEW;
        this.setAuthor(author);
        this.setEncodedImageFromFile(encodedImage);
        this.setUTCDate(messageDate);
        this.setRecipient(recipient);
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
    public void setEncodedImageFromFile(Bitmap bitmapImage) {
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

    public ChatMessageStatus getChatMessageStatus() {
        return this.chatMessageStatus;
    }

    public void setChatMessageStatus(ChatMessageStatus chatMessageStatus) {
        this.chatMessageStatus = chatMessageStatus;
    }

    public String getRecipient() {
        return this.recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}