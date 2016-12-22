package com.example.letstalk.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.example.letstalk.R;
import com.example.letstalk.activity.sessions.chat.ChatActivity;
import com.example.letstalk.configuration.Config;
import com.example.letstalk.domain.message.ChatMessage;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.letstalk_logo)
                        .setContentTitle(Config.NOTIFICATION_NEW_CHAT_TEXT)
                        .setContentText("Chat message here");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ChatActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setVibrate(new long[] { 1000, 1000});
        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(Config.NOTIFICATION_CHAT, mBuilder.build());
    }
}
