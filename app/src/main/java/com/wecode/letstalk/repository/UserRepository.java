package com.wecode.letstalk.repository;

import android.app.Activity;
import android.content.Intent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wecode.letstalk.configuration.Config;
import com.wecode.letstalk.domain.user.User;
import com.wecode.letstalk.utils.FirebaseUtils;

public class UserRepository {

    private DatabaseReference mDatabaseReference;

    private User user;

    public UserRepository(String url) {
        this.mDatabaseReference = com.google.firebase.database.FirebaseDatabase.getInstance().getReference().child(url);
    }

    public void save(User user) {
        String username = user.getEmail();
        String userPath = FirebaseUtils.clearUserName(username);
        this.mDatabaseReference.child(userPath).setValue(user);
    }


    public User findByUserName(final String username, final Intent intent, final Activity activity) {
        final String userPath = FirebaseUtils.clearUserName(username);
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

    public void updateUser(User user) {
        final String userPath = FirebaseUtils.clearUserName(user.getEmail());
        this.mDatabaseReference.child(userPath).setValue(user);
    }
}