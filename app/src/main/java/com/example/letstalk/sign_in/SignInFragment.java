package com.example.letstalk.sign_in;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.letstalk.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by teodo on 14/10/2016.
 */
public class SignInFragment extends Fragment {

    private RelativeLayout relativeLayout;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mListener;

    public RelativeLayout getRelativeLayout() {
        return this.relativeLayout;
    }

    public void setRelativeLayout(RelativeLayout relativeLayout) {
        this.relativeLayout = relativeLayout;
    }

    public FirebaseAuth getmAuth() {
        return this.mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public FirebaseAuth.AuthStateListener getmListener() {
        return this.mListener;
    }

    public void setmListener(FirebaseAuth.AuthStateListener mListener) {
        this.mListener = mListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.setRelativeLayout((RelativeLayout) inflater.inflate(R.layout.fragment_sign_in,container, false));
        this.setmAuth(FirebaseAuth.getInstance());
        this.setmListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                }
            }
        });

        return this.getRelativeLayout();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.getmAuth().addAuthStateListener(this.getmListener());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (this.getmListener() != null) {
            this.getmAuth().removeAuthStateListener(this.getmListener());
        }
    }

    public static Fragment newInstance() {
        SignInFragment signInFragment = new SignInFragment();
        return signInFragment;
    }
}
