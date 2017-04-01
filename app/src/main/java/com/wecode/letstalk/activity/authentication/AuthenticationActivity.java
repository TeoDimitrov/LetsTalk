package com.wecode.letstalk.activity.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.facebook.FacebookSdk;
import com.wecode.letstalk.R;

import static android.view.View.*;

public class AuthenticationActivity extends AppCompatActivity implements OnTouchListener {

    private RelativeLayout mRelativeLayout;

    private ViewPager mSignViewPager;

    private TabLayout mSignTabLayout;

    private AuthenticationPagerAdapter mSignFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_authentication);
        this.prepareLayout();
        this.prepareViews();
        this.prepareListeners();
    }

    private void prepareLayout(){
        this.mRelativeLayout = (RelativeLayout) findViewById(R.id.activity_authnetication);
    }

    private void prepareViews(){
        this.mSignFragmentPagerAdapter = new AuthenticationPagerAdapter(getSupportFragmentManager());
        this.mSignViewPager = (ViewPager) findViewById(R.id.viewPagerSign);
        this.mSignViewPager.setAdapter(this.mSignFragmentPagerAdapter);
        this.mSignTabLayout = (TabLayout) findViewById(R.id.tabLayoutSign);
        this.mSignTabLayout.setupWithViewPager(this.mSignViewPager);
    }

    private void prepareListeners(){
        this.mRelativeLayout.setOnTouchListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.mSignFragmentPagerAdapter.getFragmentSignUp().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.activity_authnetication :
                this.hideSoftKeyboard(this);
                break;
        }

        return false;
    }

    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus()
                        .getWindowToken(), 0);
    }
}
