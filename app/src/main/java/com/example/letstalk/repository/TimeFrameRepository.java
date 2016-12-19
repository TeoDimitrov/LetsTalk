package com.example.letstalk.repository;

import com.example.letstalk.domain.timeFrames.TimeFrame;
import com.example.letstalk.utils.DateTimeUtil;
import com.example.letstalk.utils.HashUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeFrameRepository {

    private DatabaseReference mDatabaseReference;

    public TimeFrameRepository(String url) {
        this.mDatabaseReference = com.google.firebase.database.FirebaseDatabase.getInstance().getReference().child(url);
    }

    public void save(TimeFrame timeFrame) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(timeFrame.getAdvisorName());
        keyBuilder.append(timeFrame.getUsername());
        keyBuilder.append(timeFrame.getLocalStartDateTime());
        keyBuilder.append(timeFrame.getLocalEndDateTime());
        String key = HashUtil.getHashMD5(keyBuilder.toString());
        this.mDatabaseReference.child(key).setValue(timeFrame);
    }

    public List<TimeFrame> findByUserName(String username) {
        final List<TimeFrame> timeFrames = new ArrayList<>();
        Query query = this.mDatabaseReference.orderByChild("username").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot timeFrameChild : dataSnapshot.getChildren()) {
                        TimeFrame timeFrame = timeFrameChild.getValue(TimeFrame.class);
                        timeFrames.add(timeFrame);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return timeFrames;
    }

    public List<TimeFrame> findByAdvisorName(String advisorName) {
        final List<com.example.letstalk.domain.timeFrames.TimeFrame> timeFrames = new ArrayList<>();
        Query query = this.mDatabaseReference.orderByChild("advisorname").equalTo(advisorName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot timeFrameChild : dataSnapshot.getChildren()) {
                        com.example.letstalk.domain.timeFrames.TimeFrame timeFrame = timeFrameChild.getValue(com.example.letstalk.domain.timeFrames.TimeFrame.class);
                        timeFrames.add(timeFrame);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return timeFrames;
    }

    public List<String> findByUserDate(final String date) {
        final List<String> advisorNames = new ArrayList<>();
        this.mDatabaseReference
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot timeFrameChild : dataSnapshot.getChildren()) {
                            TimeFrame timeFrame = timeFrameChild.getValue(TimeFrame.class);
                            Date startDate = DateTimeUtil.getUTCDateTime(timeFrame.getStartDateTime());
                            Date endDate = DateTimeUtil.getUTCDateTime(timeFrame.getEndDateTime());
                            Date searchDate = DateTimeUtil.getUTCDateTime(date);
                            if (searchDate.after(startDate) && searchDate.before(endDate)) {
                                advisorNames.add(timeFrame.getAdvisorName());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return advisorNames;
    }

    public DatabaseReference getmDatabaseReference() {
        return this.mDatabaseReference;
    }
}
