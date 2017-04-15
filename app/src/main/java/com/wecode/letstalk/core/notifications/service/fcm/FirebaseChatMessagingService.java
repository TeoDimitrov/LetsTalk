package com.wecode.letstalk.core.notifications.service.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wecode.letstalk.R;
import com.wecode.letstalk.activity.sessions.chat.ChatActivity;
import com.wecode.letstalk.configuration.Config;
import com.wecode.letstalk.domain.message.ChatMessage;
import com.wecode.letstalk.domain.user.User;

import org.json.JSONException;
import org.json.JSONObject;

public class FirebaseChatMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            JSONObject jsonData = new JSONObject(remoteMessage.getData());
            Gson gson = new GsonBuilder().create();
            ChatMessage chatMessage = null;
            String chatPath = null;
            User author = null;
            User recipient = null;
            try {
                chatMessage = gson.fromJson(jsonData.get(Config.CHAT_MESSAGE).toString(), ChatMessage.class);
                chatPath = gson.fromJson(jsonData.getString(Config.CHAT_PATH_EXTRA), String.class);
                author = gson.fromJson(jsonData.get(Config.USER_AUTHOR_EXTRA).toString(), User.class);
                recipient = gson.fromJson(jsonData.get(Config.USER_RECIPIENT_EXTRA).toString(), User.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            sendNotification(chatMessage, chatPath, author, recipient);
        }
    }

    private void sendNotification(ChatMessage chatMessage, String chatPath, User author, User recipient) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(Config.CHAT_PATH_EXTRA, chatPath);
        //The Recipient of the message will be the author in the activity!
        intent.putExtra(Config.USER_AUTHOR_EXTRA, recipient);
        intent.putExtra(Config.USER_RECIPIENT_EXTRA, author);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestId = chatPath.hashCode();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestId, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.letstalk_logo)
                .setContentTitle("FCM Message")
                .setContentText(chatMessage.getMessage())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(requestId, notificationBuilder.build());
    }
}
