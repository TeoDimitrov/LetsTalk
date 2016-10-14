package com.example.letstalk.sign_in;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;


import com.example.letstalk.R;

public class SignInActivity extends AppCompatActivity{

    private ViewPager signViewPager;

    private TabLayout signTabLayout;

    private RelativeLayout container;

    private SignUpFragmentOne signUpFragmentOne;

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
        this.getSignViewPager().setAdapter(new SignFragmentPagerAdapter(getSupportFragmentManager()));

        this.setSignTabLayout((TabLayout) findViewById(R.id.tabLayoutSign));
        this.getSignTabLayout().setupWithViewPager(this.getSignViewPager());


//        this.setSignUpFragmentOne(new SignUpFragmentOne());
//
//        if (findViewById(R.id.singInContainer) != null) {
//
//            if (savedInstanceState != null) {
//                return;
//            }
//
//            getSupportFragmentManager().beginTransaction().add(this.container.getId(), signUpFragmentOne).commit();
//        }
    }
}
