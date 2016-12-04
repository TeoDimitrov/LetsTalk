package com.example.letstalk.activity.sessions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;

import com.example.letstalk.activity.sessions.chat.ChatActivity;
import com.example.letstalk.R;
import com.example.letstalk.configuration.Config;
import com.example.letstalk.domain.user.User;

public class SessionsActivity extends AppCompatActivity {

    private ViewGroup chatContainer;

    private TabLayout sessionTabLayout;

    private ViewPager sessionsViewPager;

    private Intent nextActivityChat;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sessions_activity);
        this.sessionsViewPager = ((ViewPager) findViewById(R.id.viewPagerSessions));
        this.sessionsViewPager.setAdapter(new SessionsFragmentPagerAdapter(getSupportFragmentManager()));
        this.sessionTabLayout = ((TabLayout) findViewById(R.id.tabLayoutSessions));
        this.sessionTabLayout.setupWithViewPager(this.sessionsViewPager);
        this.chatContainer = ((ViewGroup) findViewById(R.id.fragmentContainer));
        if (savedInstanceState == null) {
            Intent i = getIntent();
            Bundle extras = i.getExtras();
            this.currentUser = extras.getParcelable(Config.USER_EXTRA);
            System.out.println();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    private void addFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().add(this.chatContainer.getId(),fragment).commit();
    }
}