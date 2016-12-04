package com.example.letstalk.repository;

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

    public void create(User user){
        String username = user.getUsername();
        String userPath = this.clearUserName(username);
        this.mDatabaseReference.child(userPath).setValue(user);
    }

    public User findByUserName(String username){
        final String userPath = this.clearUserName(username);
        Query query = this.mDatabaseReference.orderByChild("username").equalTo(username);
        query.addValueEventListener(new ValueEventListener() {
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

    public boolean isUserExist(String username){
        User user = this.findByUserName(username);
        boolean isUserExist = true;
        if(user == null){
            isUserExist = false;
        }

        return isUserExist;
    }

    public DatabaseReference getmDatabaseReference() {
        return this.mDatabaseReference;
    }

    private String clearUserName(String userPath){
        return userPath.replace('.', ',');
    }
}
