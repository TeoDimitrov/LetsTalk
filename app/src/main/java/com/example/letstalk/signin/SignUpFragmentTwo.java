package com.example.letstalk.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.letstalk.R;
import com.example.letstalk.SessionsActivity;
import com.example.letstalk.users.User;

public class SignUpFragmentTwo extends Fragment implements OnClickListener{

    private RelativeLayout relativeLayout;

    private EditText etUsername;
    private EditText etPassword;
    private EditText etConfirmPassword;

    private Intent nextActivity;
    private Button btnNext2;

    private String usernameValue;
    private String passwordValue;
    private String confirmPasswordValue;

    private String gender;
    private Integer birthYear;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_sign_up_fragment_two,container, false);

        this.etUsername = (EditText) this.relativeLayout.findViewById(R.id.username);
        this.etPassword = (EditText) this.relativeLayout.findViewById(R.id.password);
        this.etConfirmPassword = (EditText) this.relativeLayout.findViewById(R.id.confirm_password);

        this.btnNext2 = (Button) this.relativeLayout.findViewById(R.id.button_next2);
        this.btnNext2.setOnClickListener(this);

        Bundle args = getArguments();
        if (args != null) {
            this.gender = args.getString("gender");
            this.birthYear = args.getInt("birthYear");
        }

        return this.relativeLayout;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_next2) {

            this.usernameValue = this.etUsername.getText().toString();
            this.passwordValue = this.etPassword.getText().toString();
            this.confirmPasswordValue= this.etConfirmPassword.getText().toString();

            Log.e("Birth year", this.birthYear.toString());
            Log.e("Gender", this.gender);
            Log.e("Username", this.usernameValue);
            Log.e("Password", this.passwordValue);
            Log.e("ConfPassword", this.confirmPasswordValue);

            User user = new User(this.birthYear, this.gender, this.usernameValue, this.passwordValue);



            Intent intent = new Intent(getActivity(), SessionsActivity.class);
            startActivity(intent);
        }
    }
}