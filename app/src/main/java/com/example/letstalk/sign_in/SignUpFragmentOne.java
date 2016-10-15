package com.example.letstalk.sign_in;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
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

import static android.view.View.OnClickListener;

public class SignUpFragmentOne extends Fragment implements OnClickListener {

    private RadioButton male;

    private RadioButton female;

    private RadioGroup genderRadioButtons;

    private EditText etBirthYear;

    private Button next1;

    private Button fbBtn;

    private RelativeLayout relativeLayout;

    private SignUpFragmentTwo signUpFragmentTwo;

    public Integer year;

    public String gender;

    private TabFragmentListener tabListener;

    private FirebaseAuth mAuth;

    private CallbackManager mCallbackManager;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_sign_in_fragment_one, container, false);
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
//        mCallbackManager = CallbackManager.Factory.create();
//        this.fbBtn.setReadPermissions("email", "public_profile");
//
//        fbBtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Log.d("T", "facebook:onSuccess:" + loginResult);
//                handleFacebookAccessToken(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel() {
//                Log.d("T", "facebook:onCancel");
//                // [START_EXCLUDE]
//                // [END_EXCLUDE]
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.d("T", "facebook:onError", error);
//                // [START_EXCLUDE]
//                // [END_EXCLUDE]
//            }
//        });
//        // [END initialize_fblogin]

        return this.relativeLayout;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button_next1:
                clickNext();
                break;
            case R.id.btn_fb:
                break;
        }
    }

    private void clickNext() {
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

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.female_radio_button:
                if (checked)

                    break;
            case R.id.male_radio_button:
                if (checked)

                    break;
        }
    }

    public static SignUpFragmentOne newInstance(TabFragmentListener tabFragmentListener){
        SignUpFragmentOne signUpFragmentOne = new SignUpFragmentOne(tabFragmentListener);
        return signUpFragmentOne;
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("t", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("T", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("T", "signInWithCredential", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}