package com.example.letstalk.repository;

import android.content.Intent;
import android.util.Log;

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

    public void create(User user) {
        String username = user.getUsername();
        String userPath = this.clearUserName(username);
        this.mDatabaseReference.child(userPath).setValue(user);
    }

    public User findByUserName(final String username, final String gender, final int birthyear, final Intent intent) {
        final String userPath = this.clearUserName(username);
        Query query = this.mDatabaseReference.orderByChild("username").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.child(userPath).getValue(User.class);
                if (user == null) {
                    user = new User();
                    user.setUsername(username);
                    user.setGender(gender);
                    user.setBirthDate(birthyear);
                    create(user);
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
        Query query = this.mDatabaseReference.orderByChild("username").equalTo(username);
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

    public void updateUser(User user) {
        final String userPath = this.clearUserName(user.getUsername());
        this.mDatabaseReference.child(userPath).setValue(user);
    }

    public DatabaseReference getmDatabaseReference() {
        return this.mDatabaseReference;
    }

    public String clearUserName(String userPath) {
        return userPath.replace('.', ',');
    }
}
