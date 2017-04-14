package com.wecode.letstalk.activity.sessions;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.wecode.letstalk.R;
import com.wecode.letstalk.activity.sessions.chat.ChatActivity;
import com.wecode.letstalk.configuration.Config;
import com.wecode.letstalk.domain.timeFrames.TimeFrame;
import com.wecode.letstalk.domain.timeFrames.TimeFrameStatus;
import com.wecode.letstalk.domain.timeFrames.TimeFrameType;
import com.wecode.letstalk.domain.user.User;
import com.wecode.letstalk.repository.AdvisorRepository;
import com.wecode.letstalk.repository.TimeFrameRepository;
import com.wecode.letstalk.repository.UserRepository;
import com.wecode.letstalk.utils.DateTimeUtil;
import com.wecode.letstalk.utils.HashUtil;
import com.wooplr.spotlight.SpotlightView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
                    String chatPath = HashUtil.getHashMD5(participants.toString());
                    mChatIntent.putExtra(Config.CHAT_PATH_EXTRA, chatPath);
                    mChatIntent.putExtra(Config.USER_AUTHOR_EXTRA, mSessionsActivity.getmCurrentUser());
                    String recipientUserName = null;
                    if(mSessionsActivity.getmCurrentUser().getRole().getName().equals("AdvisorRole")) {
                        recipientUserName = timeFrame.getUsername();
                    } else {
                        recipientUserName = timeFrame.getAdvisorName();
                    }

                    mUserRepository.findByUserName(recipientUserName, mChatIntent, getActivity());
                }

                if (mSessionsActivity.getmCurrentUser().getRole().getName().equals("AdvisorRole")
                        && timeFrame.getStatus() == TimeFrameStatus.UNCONFIRMED
                        && mSessionsActivity.getmCurrentUser().getEmail().equals(timeFrame.getAdvisorName())) {
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
                        deleteChangedTimeFrame(dataSnapshot);
                        appendTimeFrame(dataSnapshot);
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

        showHint();
        return this.mRelativeLayout;
    }

    protected void showHint() {
        String usageId = "chat";
        new SpotlightView.Builder(getActivity())
                .introAnimationDuration(400)
                .performClick(true)
                .fadeinTextDuration(400)
                .headingTvColor(Color.parseColor("#eb273f"))
                .headingTvSize(32)
                .headingTvText("Add chat")
                .subHeadingTvColor(Color.parseColor("#ffffff"))
                .subHeadingTvSize(16)
                .subHeadingTvText("Click here to start a new chat session. The advisor will confirm it when they are available.")
                .maskColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryTransparent))
                .target(mBtnAddChat)
                .lineAnimDuration(400)
                .lineAndArcColor(Color.parseColor("#ed3c52"))
                .dismissOnTouch(true)
                .dismissOnBackPress(true)
                .enableDismissAfterShown(true)
                .usageId(usageId) //UNIQUE ID
                .show();
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
        if (!hasUserPaid(this.mSessionsActivity.getmCurrentUser())) {
            Toast.makeText(mSessionsActivity, "You have to pay for more chats", Toast.LENGTH_SHORT).show();
            this.payChat(this.mSessionsActivity.getmCurrentUser());
            return;
        }

        this.addChat(this.mSessionsActivity.getmCurrentUser());
        final Date startDate = new Date();
        final Date endDate = new Date();
        final String userName = this.mSessionsActivity.getmCurrentUser().getEmail();

        //TempSolution
        AdvisorRepository advisorRepository = new AdvisorRepository();
        advisorRepository.getmDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String advisorName = dataSnapshot.getValue(String.class);
                TimeFrame timeFrame = new TimeFrame(startDate, endDate, TimeFrameType.CHAT, userName, advisorName);
                mTimeFrameRepository.save(timeFrame);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        enableButton();
    }

    private void enableButton() {
        this.mBtnAddChat.setEnabled(false);
        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                mSessionsActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBtnAddChat.setEnabled(true);
                    }
                });
            }
        }, 2000);
    }

    private void addChat(User user) {
        user.addChat(1);
        this.mUserRepository.updateUser(user);
    }

    private void payChat(final User user) {
        new AlertDialog.Builder(getContext())
                .setTitle("Buy Chat")
                .setMessage("Would you like to buy a chat?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        user.addPaidChat(1);
                        mUserRepository.updateUser(user);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void appendTimeFrame(DataSnapshot dataSnapshot) {
        TimeFrame timeFrame = dataSnapshot.getValue(TimeFrame.class);
        if (isOldSession(timeFrame)) {
            return;
        }

        if (timeFrame.getType() == TimeFrameType.CHAT) {
            String role = this.mSessionsActivity.getmCurrentUser().getRole().getName();
            String userName = this.mSessionsActivity.getmCurrentUser().getEmail();
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

    private void deleteChangedTimeFrame(DataSnapshot dataSnapshot) {
        TimeFrame timeFrame = dataSnapshot.getValue(TimeFrame.class);
        List<TimeFrame> timeFrameList = this.mSessionChatAdapter.getTimeFrames();
        List<TimeFrame> timeFrameDeleteList = new ArrayList<>();
        for (TimeFrame frame : timeFrameList) {
            boolean isAdvisorSame = frame.getAdvisorName().equals(timeFrame.getAdvisorName());
            boolean isUserSame = frame.getUsername().equals(timeFrame.getUsername());
            boolean isStartDateSame = frame.getStartDateTime().equals(timeFrame.getStartDateTime());
            boolean isEndDateSame = frame.getEndDateTime().equals(timeFrame.getEndDateTime());
            if (isAdvisorSame && isUserSame && isStartDateSame && isEndDateSame) {
                timeFrameDeleteList.add(frame);
            }
        }

        for (TimeFrame frame : timeFrameDeleteList) {
            mSessionChatAdapter.remove(frame);
        }
    }

    private boolean isOldSession(TimeFrame timeFrame) {
        boolean isOld = false;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -7);
        Date dateBefore7Days = cal.getTime();
        Date timeFrameDate = DateTimeUtil.getUTCDateTime(timeFrame.getStartDateTime());
        if (timeFrameDate.before(dateBefore7Days)) {
            isOld = true;
        }

        return isOld;
    }

    private boolean hasUserPaid(User user) {
        boolean hasPaid = true;
        if (user.getChats() >= user.getPaidChats()) {
            hasPaid = false;
        }

        return hasPaid;
    }
}
