package com.example.letstalk.repository;

import com.example.letstalk.domain.user.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private DatabaseReference mDatabaseReference;

    public UserRepository(String url) {
        this.mDatabaseReference = com.google.firebase.database.FirebaseDatabase.getInstance().getReference().child(url);
    }

    public void create(User user){
        String username = user.getUsername();
        String userPath = this.clearUserName(username);
        this.mDatabaseReference.child(userPath).setValue(user);
    }

    public User findByUserName(String userName){
        final List<User> users = new ArrayList<>();
        Query query = this.mDatabaseReference.orderByChild("username").equalTo(userName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        users.add(user.getValue(User.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        User user = users.stream().findFirst().get();
        return user;
    }

    public DatabaseReference getmDatabaseReference() {
        return this.mDatabaseReference;
    }

    private String clearUserName(String userPath){
        return userPath.replace('.', ',');
    }
}
