package com.example.letstalk.firebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.example.letstalk.configuration.Config;
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
                            Toast.makeText(activity, Config.ERROR_EXISTING_USER, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signIn(String email, String password, final FragmentActivity activity, final Intent intent, final ProgressDialog progressDialog) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            activity.startActivity(intent);
                            activity.finish();
                        }

                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(activity, Config.ERROR_NO_SUCH_USER, Toast.LENGTH_SHORT).show();
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
