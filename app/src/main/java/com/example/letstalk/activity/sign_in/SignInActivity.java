package com.example.letstalk.activity.sign_in;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.XmlRes;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.letstalk.R;

public class SignInActivity extends AppCompatActivity {

    private ViewPager mSignViewPager;

    private TabLayout mSignTabLayout;

    private SignFragmentPagerAdapter mSignFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        this.mSignFragmentPagerAdapter = new SignFragmentPagerAdapter(getSupportFragmentManager());
        this.mSignViewPager = (ViewPager) findViewById(R.id.viewPagerSign);
        this.mSignViewPager.setAdapter(this.mSignFragmentPagerAdapter);
        this.mSignTabLayout = (TabLayout) findViewById(R.id.tabLayoutSign);
        this.mSignTabLayout.setupWithViewPager(this.mSignViewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.mSignFragmentPagerAdapter.getFragmentSignUp().onActivityResult(requestCode, resultCode, data);
    }
}
