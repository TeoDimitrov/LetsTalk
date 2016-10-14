package com.example.letstalk.sign_in;


import android.annotation.SuppressLint;
import android.os.Bundle;
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

import com.example.letstalk.R;
import com.example.letstalk.sign_in.interfaces.TabFragmentListener;

import static android.view.View.OnClickListener;

public class SignUpFragmentOne extends Fragment implements OnClickListener {

    private RadioButton male;

    private RadioButton female;

    private RadioGroup genderRadioButtons;

    private EditText etBirthYear;

    private Button next1;

    private RelativeLayout relativeLayout;

    private SignUpFragmentTwo signUpFragmentTwo;

    public Integer year;

    public String gender;

    private TabFragmentListener tabListener;

    public SignUpFragmentOne() {
        super();
    }

    @SuppressLint("ValidFragment")
    public SignUpFragmentOne(TabFragmentListener tabFragmentListener) {
        this.setTabListener(tabFragmentListener);
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

        return this.relativeLayout;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_next1) {
            String etValue = etBirthYear.getText().toString();
            this.year = Integer.parseInt(etValue);
            Log.e("EditText", etValue);

            int radioButtonID = genderRadioButtons.getCheckedRadioButtonId();
            View radioButton = genderRadioButtons.findViewById(radioButtonID);
            int idx = genderRadioButtons.indexOfChild(radioButton);
            RadioButton r = (RadioButton) genderRadioButtons.getChildAt(idx);
            this.gender = r.getText().toString();
            Log.e("Gender", this.gender);

            replaceFragment();
        }
    }

    private void replaceFragment() {

        // Create fragment and give it an argument specifying the article it should show
        Bundle args = new Bundle();
        args.putInt("birthYear", this.year);
        args.putString("gender", this.gender);
        signUpFragmentTwo.setArguments(args);

        if(this.getTabListener() != null){
           this.getTabListener().onSwitchToNextFragment();
        }

//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.singInContainer, this.signUpFragmentTwo);
//        transaction.addToBackStack(null);
//        transaction.commit();
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
}