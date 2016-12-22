package com.example.letstalk.activity.sessions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.letstalk.R;
import com.example.letstalk.configuration.Config;
import com.example.letstalk.domain.user.User;
import com.example.letstalk.repository.UserRepository;

public class SessionsActivity extends AppCompatActivity {

    private ViewGroup chatContainer;

    private SessionsFragmentPagerAdapter mSessionsFragmentPagerAdapter;

    private TabLayout sessionTabLayout;

    private ViewPager sessionsViewPager;

    private UserRepository userRepository;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        this.mSessionsFragmentPagerAdapter = new SessionsFragmentPagerAdapter(getSupportFragmentManager());
        this.sessionsViewPager = ((ViewPager) findViewById(R.id.viewPagerSessions));
        this.sessionsViewPager.setAdapter(this.mSessionsFragmentPagerAdapter);
        this.sessionTabLayout = ((TabLayout) findViewById(R.id.tabLayoutSessions));
        this.sessionTabLayout.setupWithViewPager(this.sessionsViewPager);
        this.chatContainer = ((ViewGroup) findViewById(R.id.fragmentContainer));
        this.userRepository = new UserRepository(Config.CHILD_USERS);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            this.currentUser = extras.getParcelable(Config.USER_EXTRA);
        }

        this.registerNotificationBroadcastReceiver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public String getCurrentUserPath() {
        String userPath = this.userRepository.clearUserName(this.currentUser.getEmail());
        return userPath;
    }

    private void registerNotificationBroadcastReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Config.NOTIFICATION_BROADCAST);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.w("Check", "Inside On Receiver");
                Toast.makeText(getApplicationContext(), "received",
                        Toast.LENGTH_SHORT).show();
            }
        };

        registerReceiver(br, intentFilter);
    }
}

//if(intent.getAction().equals("NEW_MESSAGE_INTENT")) {
//        Toast.makeText(context, "Received", Toast.LENGTH_SHORT).show();
//        NotificationCompat.Builder mBuilder =
//        new NotificationCompat.Builder(context)
//        .setSmallIcon(R.drawable.letstalk_logo)
//        .setContentTitle(Config.NOTIFICATION_NEW_CHAT_TEXT)
//        .setContentText("Chat message here");
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addParentStack(ChatActivity.class);
//        stackBuilder.addNextIntent(intent);
//        PendingIntent resultPendingIntent =
//        stackBuilder.getPendingIntent(
//        0,
//        PendingIntent.FLAG_UPDATE_CURRENT
//        );
//        mBuilder.setContentIntent(resultPendingIntent);
//        mBuilder.setVibrate(new long[]{1000, 1000});
//        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
//        NotificationManager mNotificationManager =
//        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(Config.NOTIFICATION_CHAT, mBuilder.build());
//        }