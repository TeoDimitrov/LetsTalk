package com.example.letstalk.activity.sign_in;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.letstalk.configuration.Config;
import com.example.letstalk.R;
import com.example.letstalk.activity.sessions.SessionsActivity;
import com.example.letstalk.domain.user.User;
import com.example.letstalk.firebase.FirebaseEmailAuthenticator;
import com.example.letstalk.repository.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragmentTwo extends Fragment implements OnClickListener{

    private RelativeLayout relativeLayout;

    private EditText etUsername;

    private EditText etPassword;

    private EditText etConfirmPassword;

    private Button btnNext;

    private ProgressDialog pdCreateUser;

    private String usernameValue;

    private String passwordValue;

    private String confirmPasswordValue;

    private String gender;

    private Integer birthYear;

    private User currentUser;

    private Intent sessionActivityIntent;

    private FirebaseEmailAuthenticator firebaseEmailAuthenticator;

    private UserRepository userRepository;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_sign_up_fragment_two,container, false);
        this.etUsername = (EditText) this.relativeLayout.findViewById(R.id.username);
        this.etPassword = (EditText) this.relativeLayout.findViewById(R.id.password);
        this.etConfirmPassword = (EditText) this.relativeLayout.findViewById(R.id.confirm_password);
        this.btnNext = (Button) this.relativeLayout.findViewById(R.id.button_next2);
        this.btnNext.setOnClickListener(this);
        this.sessionActivityIntent = new Intent(getActivity(), SessionsActivity.class);
        Bundle args = getArguments();
        if (args != null) {
            this.gender = args.getString("gender");
            this.birthYear = args.getInt("birthYear");
        }

        this.firebaseEmailAuthenticator = new FirebaseEmailAuthenticator();
        this.userRepository = new UserRepository(Config.CHILD_USERS);
        this.initializePdLogin();

        return this.relativeLayout;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_next2:
                clickNextButton();
                break;
        }
    }

    private void clickNextButton() {
        this.usernameValue = this.etUsername.getText().toString();
        this.passwordValue = this.etPassword.getText().toString();
        this.confirmPasswordValue = this.etConfirmPassword.getText().toString();
        this.currentUser = new User(this.birthYear, this.gender, this.usernameValue, this.passwordValue);
        this.showProgressDialog();
        this.sessionActivityIntent.putExtra(Config.USER_EXTRA, this.currentUser);
        this.createAccount(this.currentUser );
        this.userRepository.create(this.currentUser );
        this.hideProgressDialog();
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = this.usernameValue;
        if (TextUtils.isEmpty(email)) {
            this.etUsername.setError("Username is required.");
            valid = false;
        } else {
            this.etUsername.setError(null);
        }

        String password = this.passwordValue;
        if (TextUtils.isEmpty(password)) {
            this.etPassword.setError("Password is required.");
            valid = false;
        } else {
            this.etPassword.setError(null);
        }

        String confirmPassword = this.confirmPasswordValue;
        if (TextUtils.isEmpty(confirmPassword)) {
            this.etConfirmPassword.setError("Confirm password is required.");
            valid = false;
        }
        else if(!confirmPassword.equals(password)){
            this.etConfirmPassword.setError("Passwords does not match.");
            valid = false;
        }
        else {
            this.etConfirmPassword.setError(null);
        }

        return valid;
    }

    private void createAccount(final User user) {
        if (!validateForm()) {
            return;
        }

        String email = user.getUsername();
        String password = user.getPassword();
        this.firebaseEmailAuthenticator.createUser(email,password,this.getActivity(),this.sessionActivityIntent);
    }

    public static SignUpFragmentTwo newInstance(){
        SignUpFragmentTwo signUpFragmentTwo = new SignUpFragmentTwo();
        return signUpFragmentTwo;
    }

    private void initializePdLogin() {
        this.pdCreateUser = new ProgressDialog(getActivity());
        this.pdCreateUser.setTitle("Authentication");
        this.pdCreateUser.setMessage("Creating User...");
        this.pdCreateUser.setProgress(0);
        this.pdCreateUser.setMax(20);
    }

    private void hideProgressDialog() {
        this.pdCreateUser.dismiss();
    }

    private void showProgressDialog() {
        this.pdCreateUser.show();
    }

    @Override
    public void onDestroy() {
        this.hideProgressDialog();
        super.onDestroy();
    }
}