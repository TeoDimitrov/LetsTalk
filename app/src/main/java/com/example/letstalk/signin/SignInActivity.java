package com.example.letstalk.signin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.letstalk.R;

public class SignInActivity extends AppCompatActivity implements OnClickListener {

    private EditText birthyear;
    private Button signUp;
    private Button signIn;

    private Button next2;
    private RadioButton male;
    private RadioButton female;
    private TextView textView;
    private RelativeLayout container;
    private Toolbar toolbar;

    private SignUpFragmentOne signUpFragmentOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        toolbar = (Toolbar) findViewById(R.id.sessionToolbar);
        //setSupportActionBar(toolbar);

        birthyear = (EditText) findViewById(R.id.enter_birth_year);

        this.signUp = (Button) findViewById(R.id.btn_signup);
        this.signUp.setOnClickListener(this);
        this.signIn = (Button) findViewById(R.id.btn_signin);
        this.signIn.setOnClickListener(this);

        this.container = (RelativeLayout) findViewById(R.id.sing_in_container);

        this.signUpFragmentOne = new SignUpFragmentOne();

        if (findViewById(R.id.sing_in_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            getSupportFragmentManager().beginTransaction().add(this.container.getId(), signUpFragmentOne).commit();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_signup) {
        }
    }

    private void onClickSignUp(Bundle savedInstanceState) {

    }
}
