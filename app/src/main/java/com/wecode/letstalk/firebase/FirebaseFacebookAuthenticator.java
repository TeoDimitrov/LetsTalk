package com.wecode.letstalk.firebase;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.wecode.letstalk.configuration.Config;
import com.wecode.letstalk.repository.UserRepository;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseFacebookAuthenticator {

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mListener;

    private FirebaseUser firebaseUser;

    private UserRepository userRepository;

    private Activity mActivity;

    public FirebaseFacebookAuthenticator(Activity activity) {
        this.mActivity = activity;
        this.mAuth = FirebaseAuth.getInstance();
        this.mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                userRepository = new UserRepository(Config.CHILD_USERS);
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    //userRepository.findByUserNameAndLogIn(firebaseUser.getEmail(), mActivity);
                }
            }
        };
    }

    /**
     * The method handles Facebook Authentications.
     * Originally it is described as handleFacebookAccessToken by the Firebase community.
     *
     * @param token
     * @param activity
     * @param intent
     */
    public void signIn(AccessToken token, final FragmentActivity activity, final Intent intent) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
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
        LoginManager.getInstance().logOut();
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
