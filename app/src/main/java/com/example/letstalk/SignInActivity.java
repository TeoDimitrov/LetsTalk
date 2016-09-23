package com.example.letstalk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class SignInActivity extends Activity implements OnClickListener {

    private EditText birthyear;
    private Button signUp;
    private Button signIn;
    private RadioButton male;
    private RadioButton female;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        birthyear = (EditText) findViewById(R.id.enter_birth_year);

        this.signUp = (Button) findViewById(R.id.btn_signup);
        this.signUp.setOnClickListener(this);
        this.signIn = (Button) findViewById(R.id.btn_signin);
        this.signIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}
