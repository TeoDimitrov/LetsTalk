package com.example.letstalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SessionsActivity extends AppCompatActivity {

    private Toolbar sessionToolbar;
    private Button btnChat;
    private Button btnTalk;
    private ViewGroup chatContainer;
    private Intent nextActivityChat;
    private Toolbar getToolbar() {
        return this.sessionToolbar;
    }

    private void setToolbar(View toolbar) {
        this.sessionToolbar = (Toolbar) toolbar;
    }

    public Intent getNextActivityChat() {
        return nextActivityChat;
    }

    public void setNextActivityChat(Intent nextActivityChat) {
        this.nextActivityChat = nextActivityChat;
    }

    private void initToolbar(){
        this.setToolbar(findViewById(R.id.sessionToolbar));

        setSupportActionBar(this.getToolbar());
    }

    private Button getBtnChat() {
        return this.btnChat;
    }

    private void setBtnChat(Button btnChat) {
        this.btnChat = btnChat;
    }

    private Button getBtnTalk() {
        return this.btnTalk;
    }

    private void setBtnTalk(Button btnTalk) {
        this.btnTalk = btnTalk;
    }

    private void initBtnChat(){
        this.setBtnChat((Button) findViewById(R.id.buttonChat));
    }

    private void initBtnTalk(){
        this.setBtnTalk((Button) findViewById(R.id.buttonTalk));
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sessions_activity);
        this.initBtnChat();
        this.initBtnTalk();
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