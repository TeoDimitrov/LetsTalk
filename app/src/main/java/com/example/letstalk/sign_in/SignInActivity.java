package com.example.letstalk.sign_in;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;


import com.example.letstalk.R;

public class SignInActivity extends AppCompatActivity{

    private final static String FRAGMENT_SIGN_UP_TAG = "FRAGMENT_SING_UP";

    private ViewPager signViewPager;

    private TabLayout signTabLayout;

    private RelativeLayout container;

    private SignUpFragmentOne signUpFragmentOne;

    private SignFragmentPagerAdapter signFragmentPagerAdapter;

    private ViewPager getSignViewPager() {
        return this.signViewPager;
    }

    private void setSignViewPager(ViewPager signViewPager) {
        this.signViewPager = signViewPager;
    }

    private TabLayout getSignTabLayout() {
        return this.signTabLayout;
    }

    private void setSignTabLayout(TabLayout signTabLayout) {
        this.signTabLayout = signTabLayout;
    }

    private RelativeLayout getContainer() {
        return this.container;
    }

    private void setContainer(RelativeLayout container) {
        this.container = container;
    }

    private SignUpFragmentOne getSignUpFragmentOne() {
        return this.signUpFragmentOne;
    }

    private void setSignUpFragmentOne(SignUpFragmentOne signUpFragmentOne) {
        this.signUpFragmentOne = signUpFragmentOne;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //this.setContainer((RelativeLayout) findViewById(R.id.singInContainer));
        this.setSignViewPager((ViewPager) findViewById(R.id.viewPagerSign));
        this.signFragmentPagerAdapter = new SignFragmentPagerAdapter(getSupportFragmentManager());
        this.getSignViewPager().setAdapter(this.signFragmentPagerAdapter);

        this.setSignTabLayout((TabLayout) findViewById(R.id.tabLayoutSign));
        this.getSignTabLayout().setupWithViewPager(this.getSignViewPager());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.signFragmentPagerAdapter.getFragmentSignUp().onActivityResult(requestCode, resultCode, data);
    }
}
