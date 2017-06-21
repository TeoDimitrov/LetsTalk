package com.wecode.letstalk.activity.sessions.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wecode.letstalk.R;
import com.wecode.letstalk.configuration.Config;
import com.wecode.letstalk.domain.user.User;

public class AccountActivity extends AppCompatActivity {

    private User mCurrentUser;

    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        this.receiveAuthenticatedUser(savedInstanceState);
        this.prepareViews();
        this.prepareFirebase();
    }

    private void prepareViews() {
        TextView chatsTextView = (TextView) findViewById(R.id.chats_number);
        chatsTextView.setText(Integer.toString(mCurrentUser.getChats()));
        TextView paidChatsTextView = (TextView) findViewById(R.id.paid_chats_number);
        paidChatsTextView.setText(Integer.toString(mCurrentUser.getPaidChats()));

        TextView talksTextView = (TextView) findViewById(R.id.talks_number);
        talksTextView.setText(Integer.toString(mCurrentUser.getTalks()));
        TextView paidTalksTextView = (TextView) findViewById(R.id.paid_talks_number);
        paidTalksTextView.setText(Integer.toString(mCurrentUser.getPaidTalks()));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void receiveAuthenticatedUser(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            this.mCurrentUser = extras.getParcelable(Config.USER_AUTHOR_EXTRA);
        }
    }

    private void prepareFirebase() {
        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Config.CHILD_USERS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                returnToSessionActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void returnToSessionActivity() {
        setResult(Activity.RESULT_OK,
                new Intent().putExtra(Config.USER_AUTHOR_EXTRA, this.mCurrentUser));
        finish();
    }
}
