package com.wecode.letstalk.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.wecode.letstalk.R;
import com.wecode.letstalk.activity.sessions.chat.ChatActivity;
import com.wecode.letstalk.configuration.Config;
import com.wecode.letstalk.domain.user.User;


public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String chatPath = extras.getString(Config.CHAT_EXTRA);
        User user = extras.getParcelable(Config.USER_EXTRA);
        User client = extras.getParcelable(Config.CLIENT_USER_EXTRA);
        String chatMessage = extras.getString(Config.CHAT_MESSAGE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.letstalk_logo)
                        .setContentTitle(Config.NOTIFICATION_NEW_CHAT_TEXT)
                        .setContentText(chatMessage);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ChatActivity.class);
        Intent chatActivityIntent = new Intent(context, ChatActivity.class);
        chatActivityIntent.putExtra(Config.CHAT_EXTRA, chatPath);
        chatActivityIntent.putExtra(Config.USER_EXTRA, client);
        chatActivityIntent.putExtra(Config.CLIENT_USER_EXTRA, user);
        chatActivityIntent.setAction(Config.NOTIFICATION_BROADCAST);
        chatActivityIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        stackBuilder.addNextIntent(chatActivityIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setVibrate(new long[]{1000, 1000});
        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(Config.NOTIFICATION_CHAT, mBuilder.build());
    }
}
