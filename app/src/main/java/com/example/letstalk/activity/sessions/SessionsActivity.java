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

public class SessionsActivity extends AppCompatActivity {

    private ViewGroup chatContainer;

    private TabLayout sessionTabLayout;

    private ViewPager sessionsViewPager;

    private Intent nextActivityChat;

    public Intent getNextActivityChat() {
        return nextActivityChat;
    }

    public void setNextActivityChat(Intent nextActivityChat) {
        this.nextActivityChat = nextActivityChat;
    }
    private ViewGroup getChatContainer() {
        return this.chatContainer;
    }

    private void setChatContainer(ViewGroup chatContainer) {
        this.chatContainer = chatContainer;
    }

    private void initChatContainer(){
        this.setChatContainer((ViewGroup) findViewById(R.id.fragmentContainer));
    }

    public TabLayout getSessionTabLayout() {
        return this.sessionTabLayout;
    }

    public void setSessionTabLayout(TabLayout sessionTabLayout) {
        this.sessionTabLayout = sessionTabLayout;
    }

    public ViewPager getSessionsViewPager() {
        return this.sessionsViewPager;
    }

    public void setSessionsViewPager(ViewPager sessionsViewPager) {
        this.sessionsViewPager = sessionsViewPager;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sessions_activity);

        this.setSessionsViewPager((ViewPager) findViewById(R.id.viewPagerSessions));
        this.getSessionsViewPager().setAdapter(new SessionsFragmentPagerAdapter(getSupportFragmentManager()));

        this.setSessionTabLayout((TabLayout) findViewById(R.id.tabLayoutSessions));
        this.getSessionTabLayout().setupWithViewPager(this.getSessionsViewPager());

        this.initChatContainer();
        this.setIntent(new Intent(this, ChatActivity.class));
        startActivity(this.getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    private void addFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().add(this.getChatContainer().getId(),fragment).commit();
    }

}