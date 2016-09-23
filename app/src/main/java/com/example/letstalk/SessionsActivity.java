package com.example.letstalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;


/**
 * Created by teodo on 13/09/2016.
 */
public class SessionsActivity extends AppCompatActivity {

    private Toolbar sessionToolbar;
    private Button btnChat;
    private Button btnTalk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sessions_activity);
        this.initToolbar();
        this.initBtnChat();
        this.initBtnTalk();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    private Toolbar getToolbar() {
        return this.sessionToolbar;
    }

    private void setToolbar(View toolbar) {
        this.sessionToolbar = (Toolbar) toolbar;
    }

    private void initToolbar(){
        this.setToolbar(findViewById(R.id.sessionsToolbar));
        setSupportActionBar(this.getToolbar());
    }

    public Button getBtnChat() {
        return this.btnChat;
    }

    public void setBtnChat(Button btnChat) {
        this.btnChat = btnChat;
    }

    public Button getBtnTalk() {
        return this.btnTalk;
    }

    public void setBtnTalk(Button btnTalk) {
        this.btnTalk = btnTalk;
    }

    public void initBtnChat(){
        this.setBtnChat((Button) findViewById(R.id.btn_chat));
    }

    public void initBtnTalk(){
        this.setBtnTalk((Button) findViewById(R.id.btn_talk));
    }
}