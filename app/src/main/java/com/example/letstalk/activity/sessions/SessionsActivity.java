package com.example.letstalk.activity.sessions;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.example.letstalk.R;
import com.example.letstalk.configuration.Config;
import com.example.letstalk.domain.user.User;
import com.example.letstalk.receiver.NotificationReceiver;
import com.example.letstalk.repository.UserRepository;

public class SessionsActivity extends AppCompatActivity {

    private ViewGroup chatContainer;

    private SessionsFragmentPagerAdapter mSessionsFragmentPagerAdapter;

    private TabLayout sessionTabLayout;

    private ViewPager sessionsViewPager;

    private UserRepository userRepository;

    private User currentUser;

    private BroadcastReceiver mBroadcastReceiver;

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

        this.enableNotificationBroadcastReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.registerNotificationBroadcastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerNotificationBroadcastReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.registerNotificationBroadcastReceiver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        //TODO
        //menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public String getCurrentUserPath() {
        String userPath = this.userRepository.clearUserName(this.currentUser.getEmail());
        return userPath;
    }

    private void registerNotificationBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Config.NOTIFICATION_BROADCAST);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        mBroadcastReceiver = new NotificationReceiver();
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void enableNotificationBroadcastReceiver() {
        ComponentName component = new ComponentName(this, NotificationReceiver.class);
        getPackageManager()
                .setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}