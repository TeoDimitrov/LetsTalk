package com.wecode.letstalk.activity.admin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wecode.letstalk.configuration.Config;
import com.wecode.letstalk.domain.roles.Role;
import com.wecode.letstalk.domain.user.User;
import com.wecode.letstalk.utils.FirebaseUtils;

public class AdminActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.prepareFirebase();
    }

    private void prepareFirebase() {
        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Config.CHILD_USERS);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Set advisors
        //this.setAdvisorRole("test@abv.bg");
    }

    private void setAdvisorRole(String email) {
        final String userPath = FirebaseUtils.clearUserName(email);
        Query query = this.mDatabaseReference.orderByChild(Config.CHILD_USERS_EMAIL).equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User currentUser = dataSnapshot.child(userPath).getValue(User.class);
                String advisorEmail = "teodor.dimitrov.90@gmail.com";
                final String advisorUserPath = FirebaseUtils.clearUserName(advisorEmail);
                Query query = mDatabaseReference.orderByChild(Config.CHILD_USERS_EMAIL).equalTo(advisorEmail);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final User advisorUser = dataSnapshot.child(advisorUserPath).getValue(User.class);
                        currentUser.setSchedule(advisorUser.getSchedule());
                        currentUser.setRole(new Role("AdvisorRole"));
                        mDatabaseReference.child(userPath).setValue(currentUser);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
