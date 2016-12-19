package com.example.letstalk.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseEmailAuthenticator {

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mListener;

    private FirebaseUser firebaseUser;

    public FirebaseEmailAuthenticator() {
        this.mAuth = FirebaseAuth.getInstance();
        this.mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
            }
        };
    }

    public void createUser(String email, String password, final FragmentActivity activity, final Intent intent) {
        this.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            activity.startActivity(intent);
                            activity.finish();
                        }

                        if (!task.isSuccessful()) {
                        }
                    }
                });
    }

    public void signIn(String email, String password, final FragmentActivity activity, final Intent intent) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            activity.startActivity(intent);
                            activity.finish();
                        }

                        if (!task.isSuccessful()) {
                        }
                    }
                });
    }

    public void singOut() {
        this.mAuth.signOut();
    }

    public void addListener() {
        this.mAuth.addAuthStateListener(this.mListener);
    }

    public void removeListener() {
        if (this.mListener != null) {
            this.mAuth.removeAuthStateListener(this.mListener);
        }
    }
}
