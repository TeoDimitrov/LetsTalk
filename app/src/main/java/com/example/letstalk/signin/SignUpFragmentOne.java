package com.example.letstalk.signin;


import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.letstalk.R;

import java.util.Locale;

import static android.view.View.OnClickListener;

public class SignUpFragmentOne extends Fragment implements OnClickListener {

    private RadioButton male;
    private RadioButton female;
    private RadioGroup genderRadioButtons;
    private EditText etBirthYear;

    private Button next1;
    private RelativeLayout relativeLayout;
    private SignUpFragmentTwo signUpFragmentTwo;

    private Integer year;
    private String gender;


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
        //ArticleFragment newFragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putInt("birthYear", this.year);
        args.putString("gender",this.gender);
        signUpFragmentTwo.setArguments(args);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.sing_in_container, this.signUpFragmentTwo);
        transaction.addToBackStack(null);
        transaction.commit();
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
}