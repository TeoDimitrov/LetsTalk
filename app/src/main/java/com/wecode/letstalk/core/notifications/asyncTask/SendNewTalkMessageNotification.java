package com.wecode.letstalk.core.notifications.asyncTask;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wecode.letstalk.configuration.Config;
import com.wecode.letstalk.domain.message.ChatMessage;
import com.wecode.letstalk.domain.user.User;
import com.wecode.letstalk.utils.FCMUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendNewTalkMessageNotification extends AsyncTask<String, Void, JSONObject> {

    private String mTalkPath;

    private User mAuthor;

    private User mRecipient;

    private Gson mGson;

    public SendNewTalkMessageNotification(String talkPath, User author, User recipient) {
        this.mTalkPath = talkPath;
        this.mAuthor = author;
        this.mRecipient = recipient;
        this.mGson = new GsonBuilder().create();
    }

    @Override
    protected JSONObject doInBackground(String... urls) {
        JSONObject jsonMessage = null;
        try {

            URL url = new URL(Config.FCM_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", Config.FCM_CONTENT_TYPE);
            urlConnection.setRequestProperty("Authorization", Config.FCM_AUTHORIZATION);
            urlConnection.setRequestMethod("POST");
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            jsonMessage = this.getJSONMessage();
            OutputStreamWriter out = new OutputStreamWriter(
                    urlConnection.getOutputStream());
            out.write(jsonMessage.toString());
            out.flush();
            out.close();
            urlConnection.getResponseCode();
            urlConnection.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return jsonMessage;
    }

    private JSONObject getJSONMessage() throws JSONException {
        //New JSON Object with full to & data
        JSONObject jsonRemoteMessage = new JSONObject();
        String topicName = FCMUtil.createTopicName(this.mRecipient.getEmail());
        //to recipient topic
        jsonRemoteMessage.put(Config.FCM_REMOTE_MESSAGE_TO, Config.FCM_TOPIC_SUBSCRIBE + topicName);
        //New JSON Object to hold the message itself
        JSONObject jsonData = new JSONObject();
        //Type
        jsonData.put(Config.FCM_MESSAGE_TYPE, Config.FCM_MESSAGE_TYPE_TALK);
        //Talk Path
        jsonData.put(Config.TALK_PATH_EXTRA, this.mTalkPath);
        //Author
        String jsonAuthor = this.mGson.toJson(this.mAuthor);
        jsonData.put(Config.USER_AUTHOR_EXTRA, jsonAuthor);
        //Recipient
        String jsonRecipient = this.mGson.toJson(this.mRecipient);
        jsonData.put(Config.USER_RECIPIENT_EXTRA, jsonRecipient);
        //Put the message data
        jsonRemoteMessage.put(Config.FCM_REMOTE_MESSAGE_DATA, jsonData);

        return jsonRemoteMessage;
    }
}
