package com.wecode.letstalk.activity.splashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wecode.letstalk.activity.authentication.AuthenticationActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent singInIntent = new Intent(this, AuthenticationActivity.class);
        startActivity(singInIntent);
        finish();
    }
}
