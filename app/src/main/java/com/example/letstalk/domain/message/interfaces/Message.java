package com.example.letstalk.domain.message.interfaces;

/**
 * Created by teodo on 16/09/2016.
 */
public interface Message {

    String getAuthor();

    String getMessage();

    String getDate();

    String getUTCDate();

    String getLocalTime();
}
