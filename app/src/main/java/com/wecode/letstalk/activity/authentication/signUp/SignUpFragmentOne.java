package com.wecode.letstalk.activity.authentication.signUp;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.wecode.letstalk.R;
import com.wecode.letstalk.activity.authentication.interfaces.TabFragmentListener;
import com.wecode.letstalk.configuration.Config;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.Calendar;

import static android.view.View.OnClickListener;

public class SignUpFragmentOne extends Fragment implements OnClickListener, View.OnTouchListener {

    public Integer mYear;

    public String mGender;

    private RadioButton mMaleOption;

    private RadioButton mFemaleOption;

    private RadioGroup mGenderRadioButtons;

    private EditText mBirthYearEditText;

    private Button mButtonNextPage;

    private RelativeLayout mRelativeLayout;

    private SignUpFragmentTwo mSignUpFragmentTwo;

    private TabFragmentListener mTabListener;

    public SignUpFragmentOne() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(this.getActivity().getApplicationContext());
        AppEventsLogger.activateApp(this.getContext());
        this.prepareLayout(inflater, container);
        this.prepareViews();
        this.prepareListeners();

        return this.mRelativeLayout;
    }

    private void prepareLayout(LayoutInflater inflater, ViewGroup container){
        this.mRelativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_sign_up_fragment_one, container, false);
    }

    private void prepareViews(){
        this.mButtonNextPage = (Button) this.mRelativeLayout.findViewById(R.id.button_next_page);
        this.mBirthYearEditText = (EditText) this.mRelativeLayout.findViewById(R.id.enter_birth_year);
        this.mGenderRadioButtons = (RadioGroup) this.mRelativeLayout.findViewById(R.id.gender_radio_buttons);
        this.mMaleOption = (RadioButton) this.mRelativeLayout.findViewById(R.id.male_radio_button);
        this.mMaleOption.setChecked(true);
        this.mFemaleOption = (RadioButton) this.mRelativeLayout.findViewById(R.id.female_radio_button);
        this.mSignUpFragmentTwo = new SignUpFragmentTwo();
    }

    private void prepareListeners(){
        this.mButtonNextPage.setOnClickListener(this);
        this.mMaleOption.setOnClickListener(this);
        this.mFemaleOption.setOnClickListener(this);
        this.mRelativeLayout.setOnTouchListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_next_page:
                clickNext();
                break;
        }
    }

    private void clickNext() {
        if (!validateForm()) {
            return;
        }

        String etValue = mBirthYearEditText.getText().toString();
        this.mYear = Integer.parseInt(etValue);
        int radioButtonID = mGenderRadioButtons.getCheckedRadioButtonId();
        View radioButton = mGenderRadioButtons.findViewById(radioButtonID);
        int idx = mGenderRadioButtons.indexOfChild(radioButton);
        RadioButton r = (RadioButton) mGenderRadioButtons.getChildAt(idx);
        this.mGender = r.getText().toString();

        replaceFragment();
    }

    private void replaceFragment() {
        Bundle args = new Bundle();
        args.putInt("birthYear", this.mYear);
        args.putString("gender", this.mGender);
        mSignUpFragmentTwo.setArguments(args);

        if (this.mTabListener != null) {
            this.mTabListener.onSwitchToNextFragment();
        }
    }

    private boolean validateBirthYear() {
        boolean isBirthYearValid = true;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int birthYear = 0;
        try {
            birthYear = Integer.parseInt(mBirthYearEditText.getText().toString());
        } catch (Exception ex) {
            this.mBirthYearEditText.setError(Config.ERROR_BIRTHYEAR_NOT_NUMBER);
        }

        int age = currentYear - birthYear;
        if (TextUtils.isEmpty(this.mBirthYearEditText.getText().toString())) {
            this.mBirthYearEditText.setError(Config.ERROR_BIRTHYEAR_IS_REQUIRED);
            isBirthYearValid = false;
        } else if (age < 15) {
            this.mBirthYearEditText.setError(Config.ERROR_YOUNGER_THAN_15);
            isBirthYearValid = false;
        } else if (age > 100) {
            this.mBirthYearEditText.setError(Config.ERROR_OLDER_THAN_100);
            isBirthYearValid = false;
        } else {
            this.mBirthYearEditText.setError(null);
        }

        return isBirthYearValid;
    }

    private boolean validateForm() {
        return validateBirthYear();
    }

    @SuppressLint("ValidFragment")
    public SignUpFragmentOne(TabFragmentListener tabFragmentListener) {
        this.mTabListener = tabFragmentListener;
    }

    public static SignUpFragmentOne newInstance(TabFragmentListener tabFragmentListener) {
        SignUpFragmentOne signUpFragmentOne = new SignUpFragmentOne(tabFragmentListener);
        return signUpFragmentOne;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.fragment_sign_up_one:
                this.hideSoftKeyboard(getActivity());
                break;
        }

        return false;
    }

    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus()
                        .getWindowToken(), 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}