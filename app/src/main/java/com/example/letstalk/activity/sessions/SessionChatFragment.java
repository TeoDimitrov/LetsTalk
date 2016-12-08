package com.example.letstalk.activity.sessions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.letstalk.R;
import com.example.letstalk.activity.sessions.chat.ChatArrayAdapter;
import com.example.letstalk.domain.timeFrames.TimeFrame;
import com.example.letstalk.domain.timeFrames.TimeFrameType;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Date;
import java.util.List;

import static android.view.View.*;


public class SessionChatFragment extends Fragment implements OnClickListener {

    private RelativeLayout relativeLayout;

    private ListView listView;

    private SessionChatAdapter sessionChatAdapter;

    private FloatingActionButton btnAddChat;

    private SessionsActivity sessionsActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_chat, container, false);
        this.sessionsActivity = (SessionsActivity) this.getActivity();
        this.listView = (ListView) this.relativeLayout.findViewById(R.id.session_chat_list_view_id);
        List<TimeFrame> timeFrames = this.sessionsActivity.getCurrentUser().getSchedule();
        this.sessionChatAdapter = new SessionChatAdapter(this.getContext(),R.layout.session_item, timeFrames);
        this.listView.setAdapter(this.sessionChatAdapter);
        this.btnAddChat = (FloatingActionButton) this.relativeLayout.findViewById(R.id.add_chat_id);
        this.btnAddChat.setOnClickListener(this);
        this.sessionsActivity
                .getUserRepository()
                .getmDatabaseReference()
                .child(this.sessionsActivity.getCurrentUserPath())
                .child("schedule")
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                appendTimeFrame(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return this.relativeLayout;
    }

    public static Fragment newInstance() {
        SessionChatFragment chatFragment = new SessionChatFragment();
        return chatFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_chat_id:
                this.addChatTimeFrame();
                break;
        }
    }

    private void addChatTimeFrame() {
        Date startDate = new Date();
        Date endDate = new Date();
        TimeFrame timeFrame = new TimeFrame(startDate, endDate, TimeFrameType.CHAT, "Kai");
        this.sessionsActivity.updateUser(timeFrame);
    }

    private void appendTimeFrame(DataSnapshot dataSnapshot) {
        TimeFrame timeFrame = dataSnapshot.getValue(TimeFrame.class);
        ((BaseAdapter)this.listView.getAdapter()).notifyDataSetChanged();
    }
}
