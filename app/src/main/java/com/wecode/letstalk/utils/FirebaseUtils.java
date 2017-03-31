package com.wecode.letstalk.utils;

public class FirebaseUtils {

    public static String clearUserName(String userPath) {
        return userPath.replace('.', ',');
    }
}
