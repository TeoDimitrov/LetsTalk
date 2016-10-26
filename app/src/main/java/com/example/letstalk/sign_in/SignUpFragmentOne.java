package com.example.letstalk.sign_in;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letstalk.sessions.SessionsActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.example.letstalk.R;
import com.example.letstalk.sign_in.interfaces.TabFragmentListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.view.View.OnClickListener;

public class SignUpFragmentOne extends Fragment implements OnClickListener {

    private RadioButton male;

    private RadioButton female;

    private RadioGroup genderRadioButtons;

    private EditText etBirthYear;

    private Button next1;

    private Button fbBtn;

    private LoginButton loginButton;

    private RelativeLayout relativeLayout;

    private SignUpFragmentTwo signUpFragmentTwo;

    public Integer year;

    public String gender;

    private TabFragmentListener tabListener;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private CallbackManager mCallbackManager;

    private Intent sessionsIntent;

    public SignUpFragmentOne() {
        super();
    }

    @SuppressLint("ValidFragment")
    public SignUpFragmentOne(TabFragmentListener tabFragmentListener) {
        this.setTabListener(tabFragmentListener);
    }

    public FirebaseAuth getmAuth() {
        return this.mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public TabFragmentListener getTabListener() {
        return this.tabListener;
    }

    public void setTabListener(TabFragmentListener tabListener) {
        this.tabListener = tabListener;
    }

    public RadioButton getMale() {
        return male;
    }

    public void setMale(RadioButton male) {
        this.male = male;
    }

    public RadioButton getFemale() {
        return female;
    }

    public void setFemale(RadioButton female) {
        this.female = female;
    }

    public RadioGroup getGenderRadioButtons() {
        return genderRadioButtons;
    }

    public void setGenderRadioButtons(RadioGroup genderRadioButtons) {
        this.genderRadioButtons = genderRadioButtons;
    }

    public EditText getEtBirthYear() {
        return etBirthYear;
    }

    public void setEtBirthYear(EditText etBirthYear) {
        this.etBirthYear = etBirthYear;
    }

    public Button getFbBtn() {
        return fbBtn;
    }

    public void setFbBtn(Button fbBtn) {
        this.fbBtn = fbBtn;
    }

    public Button getNext1() {
        return next1;
    }

    public void setNext1(Button next1) {
        this.next1 = next1;
    }

    public RelativeLayout getRelativeLayout() {
        return relativeLayout;
    }

    public void setRelativeLayout(RelativeLayout relativeLayout) {
        this.relativeLayout = relativeLayout;
    }

    public SignUpFragmentTwo getSignUpFragmentTwo() {
        return signUpFragmentTwo;
    }

    public void setSignUpFragmentTwo(SignUpFragmentTwo signUpFragmentTwo) {
        this.signUpFragmentTwo = signUpFragmentTwo;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(this.getActivity().getApplicationContext());
        AppEventsLogger.activateApp(this.getContext());

        this.relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_sign_up_fragment_one, container, false);
        this.next1 = (Button) this.relativeLayout.findViewById(R.id.button_next1);
        this.next1.setOnClickListener(this);

        this.male = (RadioButton) this.relativeLayout.findViewById(R.id.male_radio_button);
        this.male.setOnClickListener(this);
        this.female = (RadioButton) this.relativeLayout.findViewById(R.id.female_radio_button);
        this.female.setOnClickListener(this);
        this.genderRadioButtons = (RadioGroup) this.relativeLayout.findViewById(R.id.gender_radio_buttons);

        this.etBirthYear = (EditText) this.relativeLayout.findViewById(R.id.enter_birth_year);

        this.signUpFragmentTwo = new SignUpFragmentTwo();

        this.setmAuth(FirebaseAuth.getInstance());

        this.fbBtn = (Button) this.relativeLayout.findViewById(R.id.btn_fb);
        this.fbBtn.setOnClickListener(this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    goToSessions();
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        };

        this.mCallbackManager = CallbackManager.Factory.create();
        this.loginButton = (LoginButton) this.relativeLayout.findViewById(R.id.facebook_button);
        this.loginButton.setReadPermissions("email", "public_profile");
        this.loginButton.setOnClickListener(this);
        this.loginButton.registerCallback(this.mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("T", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("T", "facebook:onCancel");
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("T", "facebook:onError", error);
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        });
        // [END initialize_fblogin]

        return this.relativeLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button_next1:
                clickNext();
                break;
            case R.id.btn_fb:
                loginButton.performClick();
                break;
        }
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

        if(this.getTabListener() != null){
           this.getTabListener().onSwitchToNextFragment();
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        if (TextUtils.isEmpty(this.getEtBirthYear().getText().toString())) {
            this.getEtBirthYear().setError("Birth year is required.");
            valid = false;
        } else {
            this.getEtBirthYear().setError(null);
        }

        return valid;
    }

    public static SignUpFragmentOne newInstance(TabFragmentListener tabFragmentListener){
        SignUpFragmentOne signUpFragmentOne = new SignUpFragmentOne(tabFragmentListener);
        return signUpFragmentOne;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TAG", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "signInWithCredential", task.getException());
                        }

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }

    private void goToSessions(){
        sessionsIntent = new Intent(getActivity(), SessionsActivity.class);
        startActivity(sessionsIntent);
    }

    public void signOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
    }
}