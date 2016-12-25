package com.wecode.letstalk.activity.signIn;


import android.annotation.SuppressLint;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.wecode.letstalk.R;
import com.wecode.letstalk.activity.sessions.SessionsActivity;
import com.wecode.letstalk.activity.signIn.interfaces.TabFragmentListener;
import com.wecode.letstalk.configuration.Config;
import com.wecode.letstalk.firebase.FirebaseFacebookAuthenticator;
import com.wecode.letstalk.repository.UserRepository;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;

import static android.view.View.OnClickListener;

public class SignUpFragmentOne extends Fragment implements OnClickListener {

    public Integer year;

    public String gender;

    private RadioButton male;

    private RadioButton female;

    private RadioGroup genderRadioButtons;

    private EditText etBirthYear;

    private Button btnNext;

    private Button btnFb;

    private LoginButton fbLoginButton;

    private RelativeLayout relativeLayout;

    private SignUpFragmentTwo signUpFragmentTwo;

    private Intent sessionsActivityIntent;

    private TabFragmentListener tabListener;

    private ProgressDialog pdCreateUser;

    private CallbackManager mCallbackManager;

    private FirebaseFacebookAuthenticator firebaseFacebookAuthenticator;

    private UserRepository userRepository;

    public SignUpFragmentOne() {
        super();
    }

    @SuppressLint("ValidFragment")
    public SignUpFragmentOne(TabFragmentListener tabFragmentListener) {
        this.tabListener = tabFragmentListener;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(this.getActivity().getApplicationContext());
        AppEventsLogger.activateApp(this.getContext());
        this.relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_sign_up_fragment_one, container, false);
        this.btnNext = (Button) this.relativeLayout.findViewById(R.id.button_next1);
        this.btnNext.setOnClickListener(this);
        this.etBirthYear = (EditText) this.relativeLayout.findViewById(R.id.enter_birth_year);
        this.male = (RadioButton) this.relativeLayout.findViewById(R.id.male_radio_button);
        this.male.setChecked(true);
        this.male.setOnClickListener(this);
        this.female = (RadioButton) this.relativeLayout.findViewById(R.id.female_radio_button);
        this.female.setOnClickListener(this);
        this.genderRadioButtons = (RadioGroup) this.relativeLayout.findViewById(R.id.gender_radio_buttons);
        this.signUpFragmentTwo = new SignUpFragmentTwo();
        this.btnFb = (Button) this.relativeLayout.findViewById(R.id.btn_fb);
        this.btnFb.setOnClickListener(this);
        this.sessionsActivityIntent = new Intent(getActivity(), SessionsActivity.class);
        this.firebaseFacebookAuthenticator = new FirebaseFacebookAuthenticator(this.getActivity());
        this.initializePdLogin();
        this.initializeFbButton();
        this.userRepository = new UserRepository(Config.CHILD_USERS);

        return this.relativeLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.firebaseFacebookAuthenticator.addListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.firebaseFacebookAuthenticator.removeListener();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_next1:
                clickNext();
                break;
            case R.id.btn_fb:
                this.fbLogin();
                break;
        }
    }

    private void fbLogin() {
        fbLoginButton.performClick();
        this.hideProgressDialog();
    }

    private void initializeFbButton() {

        this.mCallbackManager = CallbackManager.Factory.create();
        this.fbLoginButton = (LoginButton) this.relativeLayout.findViewById(R.id.facebook_button_sign_up);
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

    private void clickNext() {
        if (!validateForm()) {
            return;
        }

        String etValue = etBirthYear.getText().toString();
        this.year = Integer.parseInt(etValue);
        int radioButtonID = genderRadioButtons.getCheckedRadioButtonId();
        View radioButton = genderRadioButtons.findViewById(radioButtonID);
        int idx = genderRadioButtons.indexOfChild(radioButton);
        RadioButton r = (RadioButton) genderRadioButtons.getChildAt(idx);
        this.gender = r.getText().toString();

        replaceFragment();
    }

    private void replaceFragment() {
        Bundle args = new Bundle();
        args.putInt("birthYear", this.year);
        args.putString("gender", this.gender);
        signUpFragmentTwo.setArguments(args);

        if (this.tabListener != null) {
            this.tabListener.onSwitchToNextFragment();
        }
    }

    private boolean validateBirthYear() {
        boolean isBirthYearValid = true;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int birthYear = 0;
        try {
            birthYear = Integer.parseInt(etBirthYear.getText().toString());
        } catch (Exception ex) {
            this.etBirthYear.setError(Config.ERROR_BIRTHYEAR_NOT_NUMBER);
        }

        int age = currentYear - birthYear;
        if (TextUtils.isEmpty(this.etBirthYear.getText().toString())) {
            this.etBirthYear.setError(Config.ERROR_BIRTHYEAR_IS_REQUIRED);
            isBirthYearValid = false;
        } else if (age < 15) {
            this.etBirthYear.setError(Config.ERROR_YOUNGER_THAN_15);
            isBirthYearValid = false;
        } else if (age > 100) {
            this.etBirthYear.setError(Config.ERROR_OLDER_THAN_100);
            isBirthYearValid = false;
        } else {
            this.etBirthYear.setError(null);
        }

        return isBirthYearValid;
    }

    private boolean validateForm() {
        return validateBirthYear();
    }

    public static SignUpFragmentOne newInstance(TabFragmentListener tabFragmentListener) {
        SignUpFragmentOne signUpFragmentOne = new SignUpFragmentOne(tabFragmentListener);
        return signUpFragmentOne;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void initializePdLogin() {
        this.pdCreateUser = new ProgressDialog(getActivity());
        this.pdCreateUser.setTitle(Config.MESSAGE_AUTHENTICATION);
        this.pdCreateUser.setMessage(Config.MESSAGE_AUTHENTICATING);
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