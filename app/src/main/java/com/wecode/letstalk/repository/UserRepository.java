package com.wecode.letstalk.repository;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.wecode.letstalk.activity.sessions.SessionsActivity;
import com.wecode.letstalk.configuration.Config;
import com.wecode.letstalk.domain.user.User;
import com.wecode.letstalk.firebase.FirebaseEmailAuthenticator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserRepository {

    private DatabaseReference mDatabaseReference;

    private User user;

    public UserRepository(String url) {
        this.mDatabaseReference = com.google.firebase.database.FirebaseDatabase.getInstance().getReference().child(url);
    }

    public void save(User user) {
        String username = user.getEmail();
        String userPath = this.clearUserName(username);
        this.mDatabaseReference.child(userPath).setValue(user);
    }

    public User findByUserName(final String username, final String gender, final int birthyear, final Intent intent) {
        final String userPath = this.clearUserName(username);
        Query query = this.mDatabaseReference.orderByChild(Config.CHILD_USERS_EMAIL).equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.child(userPath).getValue(User.class);
                if (user == null) {
                    user = new User();
                    user.setEmail(username);
                    user.setGender(gender);
                    user.setBirthDate(birthyear);
                    save(user);
                }

                intent.putExtra(Config.USER_AUTHOR_EXTRA, user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return this.user;
    }

    public User findByUserName(String username) {
        final String userPath = this.clearUserName(username);
        Query query = this.mDatabaseReference.orderByChild(Config.CHILD_USERS_EMAIL).equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.child(userPath).getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return this.user;
    }

    public User findByUserName(final String username, final Intent intent, final Activity activity) {
        final String userPath = this.clearUserName(username);
        Query query = this.mDatabaseReference.orderByChild(Config.CHILD_USERS_EMAIL).equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.child(userPath).getValue(User.class);
                intent.putExtra(Config.USER_RECIPIENT_EXTRA, user);
                activity.startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return this.user;
    }

    public User findByUserName(final String email, final String password, final FragmentActivity activity, final Intent intent, final FirebaseEmailAuthenticator firebaseEmailAuthenticator, final ProgressDialog progressDialog) {
        final String userPath = this.clearUserName(email);
        Query query = this.mDatabaseReference.orderByChild(Config.CHILD_USERS_EMAIL).equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.child(userPath).getValue(User.class);
                intent.putExtra(Config.USER_AUTHOR_EXTRA, user);
                firebaseEmailAuthenticator.signIn(email, password, activity, intent, progressDialog);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return this.user;
    }

    public User findByUserNameAndLogIn(final String username, final Activity activity) {
        final String userPath = this.clearUserName(username);
        Query query = this.mDatabaseReference.orderByChild(Config.CHILD_USERS_EMAIL).equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.child(userPath).getValue(User.class);
                Intent intent = new Intent(activity, SessionsActivity.class);
                intent.putExtra(Config.USER_AUTHOR_EXTRA, user);
                activity.startActivity(intent);
                activity.finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return this.user;
    }

    public void updateUser(User user) {
        final String userPath = this.clearUserName(user.getEmail());
        this.mDatabaseReference.child(userPath).setValue(user);
    }

    public DatabaseReference getmDatabaseReference() {
        return this.mDatabaseReference;
    }

    public String clearUserName(String userPath) {
        return userPath.replace('.', ',');
    }
}