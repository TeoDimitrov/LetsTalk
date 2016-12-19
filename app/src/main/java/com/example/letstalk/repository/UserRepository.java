package com.example.letstalk.repository;

import android.app.Activity;
import android.content.Intent;

import com.example.letstalk.configuration.Config;
import com.example.letstalk.domain.user.User;
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
        Query query = this.mDatabaseReference.orderByChild(Config.CHILD_USERS_USERNAME).equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.child(userPath).getValue(User.class);
                if (user == null) {
                    user = new User();
                    user.setUsername(username);
                    user.setGender(gender);
                    user.setBirthDate(birthyear);
                    save(user);
                }

                intent.putExtra(Config.USER_EXTRA, user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return this.user;
    }

    public User findByUserName(String username) {
        final String userPath = this.clearUserName(username);
        Query query = this.mDatabaseReference.orderByChild(Config.CHILD_USERS_USERNAME).equalTo(username);
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
        Query query = this.mDatabaseReference.orderByChild(Config.CHILD_USERS_USERNAME).equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.child(userPath).getValue(User.class);
                intent.putExtra(Config.CLIENT_USER_EXTRA, user);
                activity.startActivity(intent);
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
