package com.example.letstalk.activity.sessions;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.letstalk.R;
import com.example.letstalk.activity.sessions.chat.ChatActivity;
import com.example.letstalk.configuration.Config;
import com.example.letstalk.domain.timeFrames.TimeFrame;
import com.example.letstalk.domain.timeFrames.TimeFrameStatus;
import com.example.letstalk.domain.timeFrames.TimeFrameType;
import com.example.letstalk.repository.TimeFrameRepository;
import com.example.letstalk.utils.HashUtil;
import com.example.letstalk.utils.SpeechUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Date;

import static android.view.View.*;


public class SessionChatFragment extends Fragment implements OnClickListener {

    private RelativeLayout relativeLayout;

    private ListView listView;

    private SessionChatAdapter sessionChatAdapter;

    private FloatingActionButton btnAddChat;

    private SessionsActivity sessionsActivity;

    private TimeFrameRepository timeFrameRepository;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_chat, container, false);
        this.sessionsActivity = (SessionsActivity) this.getActivity();
        this.listView = (ListView) this.relativeLayout.findViewById(R.id.session_chat_list_view_id);
        this.timeFrameRepository = new TimeFrameRepository(Config.CHILD_TIMEFRAMES);
        this.sessionChatAdapter = new SessionChatAdapter(this.getContext(), R.layout.session_item, new ArrayList<TimeFrame>());
        this.listView.setAdapter(this.sessionChatAdapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TimeFrame timeFrame = (TimeFrame) parent.getItemAtPosition(position);
                if (timeFrame.getStatus() == TimeFrameStatus.CONFIRMED) {
                    StringBuilder participants = new StringBuilder();
                    participants.append(timeFrame.getAdvisorName());
                    participants.append(timeFrame.getUsername());
                    String chat = HashUtil.getHashMD5(participants.toString());
                    Intent chatIntent = new Intent(getActivity().getBaseContext(), ChatActivity.class);
                    chatIntent.putExtra(Config.CHAT_EXTRA, chat);
                    chatIntent.putExtra(Config.USER_EXTRA, sessionsActivity.getCurrentUser());
                    getActivity().startActivity(chatIntent);
                }

                if (sessionsActivity.getCurrentUser().getRole().getName().equals("AdvisorRole") && timeFrame.getStatus() == TimeFrameStatus.UNCONFIRMED) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Confirm chat")
                            .setMessage("Are you sure you want to confirm this chat?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    timeFrame.setStatus(TimeFrameStatus.CONFIRMED);
                                    timeFrameRepository.save(timeFrame);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
        this.btnAddChat = (FloatingActionButton) this.relativeLayout.findViewById(R.id.add_chat_id);
        this.btnAddChat.setOnClickListener(this);
        this.timeFrameRepository
                .getmDatabaseReference()
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        appendTimeFrame(dataSnapshot);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
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
        switch (v.getId()) {
            case R.id.add_chat_id:
                this.addChatTimeFrame();
                break;
        }
    }

    private void addChatTimeFrame() {
        Date startDate = new Date();
        Date endDate = new Date();
        String userName = this.sessionsActivity.getCurrentUser().getUsername();
        TimeFrame timeFrame = new TimeFrame(startDate, endDate, TimeFrameType.CHAT, userName, "teodor.dimitrov.90@gmail.com");
        this.timeFrameRepository.save(timeFrame);
    }

    private void appendTimeFrame(DataSnapshot dataSnapshot) {
        TimeFrame timeFrame = dataSnapshot.getValue(TimeFrame.class);
        String role = this.sessionsActivity.getCurrentUser().getRole().getName();
        String userName = this.sessionsActivity.getCurrentUser().getUsername();
        String timeFrameUserName = timeFrame.getUsername();
        String timeFrameAdvisorName = timeFrame.getAdvisorName();
        if (role.equals("CustomerRole") && userName.equals(timeFrameUserName)) {
            this.sessionChatAdapter.add(timeFrame);
        } else if (role.equals("AdvisorRole") && userName.equals(timeFrameAdvisorName)) {
            this.sessionChatAdapter.add(timeFrame);
        }

        ((BaseAdapter) this.listView.getAdapter()).notifyDataSetChanged();
    }
}
