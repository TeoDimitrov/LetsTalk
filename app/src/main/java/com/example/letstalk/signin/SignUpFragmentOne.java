package com.example.letstalk.signin;


import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.example.letstalk.R;

import static android.view.View.OnClickListener;

public class SignUpFragmentOne extends Fragment implements OnClickListener {

    private RadioButton male;
    private RadioButton female;
    private EditText etBirthYear;

    private Button next1;
    private RelativeLayout relativeLayout;
    private SignUpFragmentTwo signUpFragmentTwo;

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

        this.signUpFragmentTwo = new SignUpFragmentTwo();
        return this.relativeLayout;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_next1) {
           replaceFragment();
        }
    }

    private void replaceFragment(){

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.sing_in_container, this.signUpFragmentTwo);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.female_radio_button:
                if (checked)

                    break;
            case R.id.male_radio_button:
                if (checked)

                    break;
        }
    }
}