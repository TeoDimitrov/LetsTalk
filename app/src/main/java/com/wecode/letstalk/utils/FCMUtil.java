package com.wecode.letstalk.utils;


import com.google.firebase.messaging.FirebaseMessaging;
import com.wecode.letstalk.configuration.Config;
import com.wecode.letstalk.domain.user.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FCMUtil {

    public static String createTopicName(String username){
        String topicName = username.replace("@","~").replace("+","~");

        Pattern topicPattern = Pattern.compile(Config.VALID_TOPIC_REGEX);
        Matcher matcher = topicPattern.matcher(topicName);
        if(!matcher.find()){
            topicName = null;
        }

        return topicName;
    }

    public static void subscribe(User user){
        String username = user.getEmail();
        String topicName = FCMUtil.createTopicName(username);
        if(topicName != null) {
            FirebaseMessaging.getInstance().subscribeToTopic(topicName);
        }
    }

    public static void unsubscribe(User user){
        String username = user.getEmail();
        String topicName = FCMUtil.createTopicName(username);
        if(topicName != null) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topicName);
        }
    }
}

