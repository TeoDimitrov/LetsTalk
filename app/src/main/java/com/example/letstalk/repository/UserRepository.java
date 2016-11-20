package com.example.letstalk.repository;

import com.example.letstalk.domain.user.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserRepository {

    private DatabaseReference mDatabaseReference;

    private User user = new User();

    public UserRepository(String url) {
        this.mDatabaseReference = com.google.firebase.database.FirebaseDatabase.getInstance().getReference().child(url);
    }

    public void create(User user){
        String username = user.getUsername();
        String userPath = this.clearUserName(username);
        this.mDatabaseReference.child(userPath).setValue(user);
    }

    public User findByUserName(String userName){
        Query query = this.mDatabaseReference.orderByChild("username").equalTo(userName);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return this.user;
    }

    public DatabaseReference getmDatabaseReference() {
        return this.mDatabaseReference;
    }

    private String clearUserName(String userPath){
        return userPath.replace('.', ',');
    }
}
