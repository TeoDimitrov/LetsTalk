package com.wecode.letstalk.repository;

import com.google.firebase.database.DatabaseReference;


//Temp Solution
public class AdvisorRepository {

    private DatabaseReference mDatabaseReference;

    public AdvisorRepository() {
        this.mDatabaseReference = com.google.firebase.database.FirebaseDatabase.getInstance().getReference().child("advisor");
    }

    public void saveAdvisorName(String advisorName) {
        this.mDatabaseReference.setValue(advisorName);
    }

    public DatabaseReference getmDatabaseReference() {
        return this.mDatabaseReference;
    }
}
