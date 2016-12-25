package com.wecode.letstalk.activity.splash_screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wecode.letstalk.activity.signIn.SignInActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent singInIntent = new Intent(this, SignInActivity.class);
        startActivity(singInIntent);
        finish();
    }
}
