package com.example.letstalk.activity.sign_in;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.letstalk.R;
import com.example.letstalk.activity.sessions.SessionsActivity;
import com.example.letstalk.configuration.Config;
import com.example.letstalk.domain.user.User;
import com.example.letstalk.firebase.FirebaseEmailAuthenticator;
import com.example.letstalk.firebase.FirebaseFacebookAuthenticator;
import com.example.letstalk.repository.UserRepository;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class SignInFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout relativeLayout;

    private EditText etUsername;

    private EditText etPassword;

    private Button btnNext;

    private Button btnFb;

    private LoginButton fbLoginButton;

    private CallbackManager mCallbackManager;

    private ProgressDialog pdSignUser;

    private String usernameValue;

    private String passwordValue;

    private Intent sessionsActivityIntent;

    private FirebaseEmailAuthenticator firebaseEmailAuthenticator;

    private FirebaseFacebookAuthenticator firebaseFacebookAuthenticator;

    private UserRepository userRepository;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_sign_in, container, false);
        this.etUsername = (EditText) this.relativeLayout.findViewById(R.id.username_sing_in);
        this.etPassword = (EditText) this.relativeLayout.findViewById(R.id.password_sign_in);
        this.btnNext = (Button) this.relativeLayout.findViewById(R.id.button_next_sign_in);
        this.btnNext.setOnClickListener(this);
        this.btnFb = (Button) this.relativeLayout.findViewById(R.id.btn_fb_sign_in);
        this.btnFb.setOnClickListener(this);
        this.sessionsActivityIntent = new Intent(getActivity(), SessionsActivity.class);
        this.firebaseEmailAuthenticator = new FirebaseEmailAuthenticator();
        this.userRepository = new UserRepository(Config.CHILD_USERS);
        this.initializePdSignUser();
        this.firebaseFacebookAuthenticator = new FirebaseFacebookAuthenticator();
        this.initializeFbButton();

        //Only For Debugging
        this.firebaseEmailAuthenticator.singOut();
        this.firebaseFacebookAuthenticator.singOut();

        return this.relativeLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.firebaseEmailAuthenticator.addListener();
        this.firebaseFacebookAuthenticator.addListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.firebaseEmailAuthenticator.removeListener();
        this.firebaseFacebookAuthenticator.removeListener();
    }

    public static Fragment newInstance() {
        SignInFragment signInFragment = new SignInFragment();
        return signInFragment;
    }

    private void signIn(String email, String password) {
        this.firebaseEmailAuthenticator.signIn(email, password, this.getActivity(), this.sessionsActivityIntent);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = this.etUsername.getText().toString();
        if (TextUtils.isEmpty(email)) {
            this.etUsername.setError("Username is required.");
            valid = false;
        } else {
            this.etUsername.setError(null);
        }

        String password = this.etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            this.etPassword.setError("Password is required.");
            valid = false;
        } else {
            this.etPassword.setError(null);
        }

        return valid;
    }

    private void initializeFbButton() {

        this.mCallbackManager = CallbackManager.Factory.create();
        this.fbLoginButton = (LoginButton) this.relativeLayout.findViewById(R.id.facebook_button_sign_in);
        this.fbLoginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday"));
        this.fbLoginButton.setOnClickListener(this);
        this.fbLoginButton.registerCallback(this.mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                showProgressDialog();
                //Get FB Data
                setFacebookData(loginResult);
                //Authneticate
                firebaseFacebookAuthenticator.signIn(loginResult.getAccessToken(), getActivity(), sessionsActivityIntent);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
    }

    private void setFacebookData(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {
                            String email = response.getJSONObject().getString("email");
                            String gender = response.getJSONObject().getString("gender");
                            String bday = response.getJSONObject().getString("birthday");
                            int birthyear = Integer.parseInt(bday.substring(bday.length() - 4));
                            userRepository.findByUserName(email, gender, birthyear, sessionsActivityIntent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void fbLogin() {
        this.fbLoginButton.performClick();
        this.hideProgressDialog();
    }

    private void signInClick() {
        if (!validateForm()) {
            return;
        }

        this.showProgressDialog();
        this.usernameValue = this.etUsername.getText().toString();
        this.passwordValue = this.etPassword.getText().toString();
        User user = this.userRepository.findByUserName(this.usernameValue);
        this.sessionsActivityIntent.putExtra(Config.USER_EXTRA, user);
        this.signIn(this.usernameValue, passwordValue);
        this.hideProgressDialog();
    }

    public void initializePdSignUser() {
        this.pdSignUser = new ProgressDialog(getActivity());
        this.pdSignUser.setTitle("Sign In");
        this.pdSignUser.setMessage("Signing User...");
        this.pdSignUser.setProgress(0);
        this.pdSignUser.setMax(20);
    }

    private void hideProgressDialog() {
        this.pdSignUser.dismiss();
    }

    private void showProgressDialog() {
        this.pdSignUser.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_next_sign_in:
                this.signInClick();
                break;
            case R.id.btn_fb_sign_in:
                this.fbLogin();
                break;
        }
    }

    @Override
    public void onDestroy() {
        this.hideProgressDialog();
        super.onDestroy();
    }
}
