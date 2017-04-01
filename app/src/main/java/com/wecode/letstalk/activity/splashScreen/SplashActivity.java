package com.wecode.letstalk.activity.splashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wecode.letstalk.activity.authentication.AuthenticationActivity;
import com.wecode.letstalk.activity.sessions.SessionsActivity;
import com.wecode.letstalk.configuration.Config;
import com.wecode.letstalk.domain.user.User;
import com.wecode.letstalk.utils.FirebaseUtils;

public class SplashActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.prepareFirebase();
    }

    private void prepareFirebase(){
        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Config.CHILD_USERS);
        this.mAuth = FirebaseAuth.getInstance();
        this.mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Auto Login Configuration
                mAuth.removeAuthStateListener(mListener);
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    String email = firebaseUser.getEmail();
                    autoLogin(email);
                    return;
                }

                goToAuthenticationPage();
            }
        };
    }

    private void autoLogin(String email) {
        final String userPath = FirebaseUtils.clearUserName(email);
        Query query = this.mDatabaseReference.orderByChild(Config.CHILD_USERS_EMAIL).equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.child(userPath).getValue(User.class);
                if (user != null) {
                    Intent sessionsActivityIntent = new Intent(getApplicationContext(), SessionsActivity.class);
                    sessionsActivityIntent.putExtra(Config.USER_EXTRA, user);
                    startActivity(sessionsActivityIntent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void goToAuthenticationPage(){
        Intent authenticationIntent = new Intent(this, AuthenticationActivity.class);
        startActivity(authenticationIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.mAuth.addAuthStateListener(this.mListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.mAuth != null) {
            this.mAuth.removeAuthStateListener(this.mListener);
        }
    }
}
