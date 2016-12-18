package com.example.letstalk.activity.sessions;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;

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
        setContentView(R.layout.sessions_activity);
        this.mSessionsFragmentPagerAdapter = new SessionsFragmentPagerAdapter(getSupportFragmentManager());
        this.sessionsViewPager = ((ViewPager) findViewById(R.id.viewPagerSessions));
        this.sessionsViewPager.setAdapter(this.mSessionsFragmentPagerAdapter);
        this.sessionTabLayout = ((TabLayout) findViewById(R.id.tabLayoutSessions));
        this.sessionTabLayout.setupWithViewPager(this.sessionsViewPager);
        this.chatContainer = ((ViewGroup) findViewById(R.id.fragmentContainer));
        this.userRepository = new UserRepository(Config.CHILD_USERS);
        if (savedInstanceState == null) {
            Intent i = getIntent();
            Bundle extras = i.getExtras();
            this.currentUser = extras.getParcelable(Config.USER_EXTRA);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public String getCurrentUserPath() {
        String userPath = this.userRepository.clearUserName(this.currentUser.getUsername());
        return userPath;
    }
}