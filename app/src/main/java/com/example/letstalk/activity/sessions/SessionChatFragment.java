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
import com.example.letstalk.repository.UserRepository;
import com.example.letstalk.utils.HashUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Date;

import static android.view.View.OnClickListener;


public class SessionChatFragment extends Fragment implements OnClickListener {

    private RelativeLayout mRelativeLayout;

    private ListView mListView;

    private SessionChatAdapter mSessionChatAdapter;

    private FloatingActionButton mBtnAddChat;

    private SessionsActivity mSessionsActivity;

    private Intent mChatIntent;

    private TimeFrameRepository mTimeFrameRepository;

    private UserRepository mUserRepository;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mRelativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_chat, container, false);
        this.mSessionsActivity = (SessionsActivity) this.getActivity();
        this.mListView = (ListView) this.mRelativeLayout.findViewById(R.id.session_chat_list_view_id);
        this.mChatIntent = new Intent(getActivity().getBaseContext(), ChatActivity.class);
        this.mTimeFrameRepository = new TimeFrameRepository(Config.CHILD_TIMEFRAMES);
        this.mUserRepository = new UserRepository(Config.CHILD_USERS);
        this.mSessionChatAdapter = new SessionChatAdapter(this.getContext(), R.layout.session_item, new ArrayList<TimeFrame>());
        this.mListView.setAdapter(this.mSessionChatAdapter);
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TimeFrame timeFrame = (TimeFrame) parent.getItemAtPosition(position);
                if (timeFrame.getStatus() == TimeFrameStatus.CONFIRMED) {
                    StringBuilder participants = new StringBuilder();
                    participants.append(timeFrame.getAdvisorName());
                    participants.append(timeFrame.getUsername());
                    String chat = HashUtil.getHashMD5(participants.toString());
                    mChatIntent.putExtra(Config.CHAT_EXTRA, chat);
                    mChatIntent.putExtra(Config.USER_EXTRA, mSessionsActivity.getCurrentUser());
                    String clientUserName = timeFrame.getUsername();
                    mUserRepository.findByUserName(clientUserName, mChatIntent, getActivity());
                }

                if (mSessionsActivity.getCurrentUser().getRole().getName().equals("AdvisorRole")
                        && timeFrame.getStatus() == TimeFrameStatus.UNCONFIRMED
                        && mSessionsActivity.getCurrentUser().getEmail().equals(timeFrame.getAdvisorName())) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Confirm chat")
                            .setMessage("Are you sure you want to confirm this chat?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    timeFrame.setStatus(TimeFrameStatus.CONFIRMED);
                                    mTimeFrameRepository.save(timeFrame);
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
        this.mBtnAddChat = (FloatingActionButton) this.mRelativeLayout.findViewById(R.id.add_chat_id);
        this.mBtnAddChat.setOnClickListener(this);
        this.mTimeFrameRepository
                .getmDatabaseReference()
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        appendTimeFrame(dataSnapshot);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
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

        return this.mRelativeLayout;
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
        String userName = this.mSessionsActivity.getCurrentUser().getEmail();
        TimeFrame timeFrame = new TimeFrame(startDate, endDate, TimeFrameType.CHAT, userName, "teodor.dimitrov.90@gmail.com");
        this.mTimeFrameRepository.save(timeFrame);
    }

    private void appendTimeFrame(DataSnapshot dataSnapshot) {
        TimeFrame timeFrame = dataSnapshot.getValue(TimeFrame.class);
        if (timeFrame.getType() == TimeFrameType.CHAT) {
            String role = this.mSessionsActivity.getCurrentUser().getRole().getName();
            String userName = this.mSessionsActivity.getCurrentUser().getEmail();
            String timeFrameUserName = timeFrame.getUsername();
            String timeFrameAdvisorName = timeFrame.getAdvisorName();
            if (role.equals("CustomerRole") && userName.equals(timeFrameUserName)) {
                this.mSessionChatAdapter.add(timeFrame);
            } else if (role.equals("AdvisorRole")) {
                if (userName.equals(timeFrameAdvisorName) || userName.equals(timeFrameUserName)) {
                    this.mSessionChatAdapter.add(timeFrame);
                }
            }

            ((BaseAdapter) this.mListView.getAdapter()).notifyDataSetChanged();
        }
    }
}
