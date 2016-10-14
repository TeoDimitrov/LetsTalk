package com.example.letstalk.sign_in;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.letstalk.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by teodo on 14/10/2016.
 */
public class SignInFragment extends Fragment {

    private RelativeLayout relativeLayout;

    private FirebaseAuth mAuth;

    

    public RelativeLayout getRelativeLayout() {
        return this.relativeLayout;
    }

    public void setRelativeLayout(RelativeLayout relativeLayout) {
        this.relativeLayout = relativeLayout;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.setRelativeLayout((RelativeLayout) inflater.inflate(R.layout.fragment_sign_in,container, false));

        return this.getRelativeLayout();
    }

    public static Fragment newInstance() {
        SignInFragment signInFragment = new SignInFragment();
        return signInFragment;
    }
}
