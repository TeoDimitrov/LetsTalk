package com.example.letstalk.activity.sign_in;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.letstalk.R;
import com.example.letstalk.activity.sessions.SessionsActivity;
import com.example.letstalk.configuration.Config;
import com.example.letstalk.domain.user.User;
import com.example.letstalk.firebase.FirebaseEmailAuthenticator;
import com.example.letstalk.repository.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.letstalk.configuration.Config.VALID_EMAIL_ADDRESS_PATTERN;

public class SignUpFragmentTwo extends Fragment implements OnClickListener {

    private RelativeLayout relativeLayout;

    private EditText etEmail;

    private EditText etPassword;

    private EditText etConfirmPassword;

    private Button btnNext;

    private ProgressDialog pdCreateUser;

    private String emailValue;

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
        this.relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_sign_up_fragment_two, container, false);
        this.etEmail = (EditText) this.relativeLayout.findViewById(R.id.email);
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
        switch (view.getId()) {
            case R.id.button_next2:
                clickNextButton();
                break;
        }
    }

    private void clickNextButton() {
        this.emailValue = this.etEmail.getText().toString();
        this.passwordValue = this.etPassword.getText().toString();
        this.confirmPasswordValue = this.etConfirmPassword.getText().toString();
        this.currentUser = new User(this.birthYear, this.gender, this.emailValue, this.passwordValue);
        this.sessionActivityIntent.putExtra(Config.USER_EXTRA, this.currentUser);
        this.createAccount(this.currentUser);
    }

    private boolean validateEmail() {
        String email = this.etEmail.getText().toString();
        boolean isEmailValid = true;
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile(VALID_EMAIL_ADDRESS_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);

        if (TextUtils.isEmpty(email)) {
            this.etEmail.setError(Config.ERROR_EMAIL_IS_REQUIRED);
            isEmailValid = false;
        } else if (email.matches(".*[#$\\[\\]].*")) {
            this.etEmail.setError(Config.ERROR_EMAIL_SHOULD_NOT_CONTAIN);
            isEmailValid = false;
        } else if (!matcher.find()) {
            this.etEmail.setError(Config.ERROR_EMAIL_NOT_VALID);
            isEmailValid = false;
        } else {
            this.etEmail.setError(null);
        }

        return isEmailValid;
    }

    private boolean validatePassword() {
        String password = this.etPassword.getText().toString();
        boolean isPasswordValid = true;
        if (TextUtils.isEmpty(password)) {
            this.etPassword.setError(Config.ERROR_PASSWORD_IS_REQUIRED);
            isPasswordValid = false;
        } else if (password.length() < Config.MIN_PASSWORD_LENGTH) {
            this.etPassword.setError(Config.ERROR_PASSWORD_SHORT);
            isPasswordValid = false;
        } else {
            this.etPassword.setError(null);
        }

        String confirmPassword = this.etConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(confirmPassword)) {
            this.etConfirmPassword.setError(Config.ERROR_CONFIRM_PASSWORD_IS_REQUIRED);
            isPasswordValid = false;
        } else if (!confirmPassword.equals(password)) {
            this.etConfirmPassword.setError(Config.ERROR_PASSWORDS_DOESNT_MATCH);
            isPasswordValid = false;
        } else {
            this.etConfirmPassword.setError(null);
        }

        return isPasswordValid;
    }

    private boolean validateForm() {
        return validateEmail() && validatePassword();
    }

    private void createAccount(final User user) {
        if (!validateForm()) {
            return;
        }

        this.showProgressDialog();
        String email = user.getEmail();
        String password = user.getPassword();
        this.firebaseEmailAuthenticator.createUser(email, password, this.getActivity(), this.sessionActivityIntent);
        this.userRepository.save(this.currentUser);
    }

    public static SignUpFragmentTwo newInstance() {
        SignUpFragmentTwo signUpFragmentTwo = new SignUpFragmentTwo();
        return signUpFragmentTwo;
    }

    private void initializePdLogin() {
        this.pdCreateUser = new ProgressDialog(getActivity());
        this.pdCreateUser.setTitle(Config.MESSAGE_AUTHENTICATION);
        this.pdCreateUser.setMessage(Config.MESSAGE_CREATING_USER);
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
        super.onDestroy();
        this.hideProgressDialog();
    }
}