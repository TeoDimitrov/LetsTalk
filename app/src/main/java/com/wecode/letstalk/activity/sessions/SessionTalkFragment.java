package com.wecode.letstalk.activity.sessions;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wecode.letstalk.R;
import com.wecode.letstalk.activity.sessions.talk.TalkActivity;
import com.wecode.letstalk.configuration.Config;
import com.wecode.letstalk.core.advisor.AdvisorSwitchingCenter;
import com.wecode.letstalk.domain.timeFrames.TimeFrame;
import com.wecode.letstalk.domain.timeFrames.TimeFrameStatus;
import com.wecode.letstalk.domain.timeFrames.TimeFrameType;
import com.wecode.letstalk.domain.user.User;
import com.wecode.letstalk.repository.TimeFrameRepository;
import com.wecode.letstalk.repository.UserRepository;
import com.wecode.letstalk.utils.DateTimeUtil;
import com.wooplr.spotlight.SpotlightView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.OnClickListener;

public class SessionTalkFragment extends Fragment implements OnClickListener {

    private RelativeLayout mRelativeLayout;

    private ListView mListView;

    private SessionChatAdapter mSessionChatAdapter;

    private FloatingActionButton mBtnAddTalk;

    private SessionsActivity mSessionsActivity;

    private DatabaseReference mUserDatabaseReference;

    private DatabaseReference mTimeFrameReference;

    private TimeFrameRepository mTimeFrameRepository;

    private UserRepository mUserRepository;

    private SpotlightView.Builder mSpotlightBuilder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mRelativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_talk, container, false);
        this.mSessionsActivity = (SessionsActivity) this.getActivity();
        this.mUserRepository = new UserRepository(Config.CHILD_USERS);
        this.mListView = (ListView) this.mRelativeLayout.findViewById(R.id.session_talk_list_view_id);
        this.mTimeFrameRepository = new TimeFrameRepository(Config.CHILD_TIMEFRAMES);
        this.mSessionChatAdapter = new SessionChatAdapter(this.getContext(), R.layout.session_item, new ArrayList<TimeFrame>());
        this.mListView.setAdapter(this.mSessionChatAdapter);
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TimeFrame timeFrame = (TimeFrame) parent.getItemAtPosition(position);
                if (timeFrame.getStatus() == TimeFrameStatus.CONFIRMED) {
                    Intent talkActivityIntent = new Intent(getContext(), TalkActivity.class);
                    String callerId = null;
                    String recipientId = null;
                    if (mSessionsActivity.getmCurrentUser().getRole().getName().equals("AdvisorRole")) {
                        callerId = timeFrame.getAdvisorName();
                        recipientId = timeFrame.getUsername();
                    } else {
                        callerId = timeFrame.getUsername();
                        recipientId = timeFrame.getAdvisorName();
                    }

                    talkActivityIntent.putExtra(Config.SINCH_CALLER_ID, callerId);
                    talkActivityIntent.putExtra(Config.SINCH_RECIPIENT_ID, recipientId);
                    getActivity().startActivity(talkActivityIntent);
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
        this.mBtnAddTalk = (FloatingActionButton) this.mRelativeLayout.findViewById(R.id.add_talk_id);
        this.mBtnAddTalk.setOnClickListener(this);
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

        this.prepareFirebase();

        return this.mRelativeLayout;
    }

    private void prepareFirebase() {
        this.mUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Config.CHILD_USERS);
        this.mTimeFrameReference = FirebaseDatabase.getInstance().getReference().child(Config.CHILD_TIMEFRAMES);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_talk_id:
                askForBooking();
                break;
        }
    }

    private void askForBooking() {
        if (this.mSessionsActivity.getmCurrentUser().hasToPayForTalk()) {
            this.mSessionsActivity.goToAccount();
            return;
        }

        int currentDate = Calendar.getInstance().get(Calendar.DATE);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, final int year, final int month, final int date) {
                LayoutInflater factory = getLayoutInflater(null);
                RelativeLayout relativeLayout = (RelativeLayout) factory.inflate(R.layout.booking_popup, null);
                LinearLayout linearLayout = (LinearLayout) relativeLayout.findViewById(R.id.datetime_holder);
                final EditText hourText = (EditText) linearLayout.findViewById(R.id.booking_hour);

                int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                hourText.setText(String.valueOf(currentHour));
                final EditText minutesText = (EditText) linearLayout.findViewById(R.id.booking_minutes);
                int currentMinutes = Calendar.getInstance().get(Calendar.MINUTE);
                minutesText.setText(String.valueOf(currentMinutes));

                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert
                        .setTitle(R.string.app_name)
                        .setMessage("Choose Time:")
                        .setPositiveButton("Ok", null)
                        .setNegativeButton("Cancel", null)
                        .setView(relativeLayout);

                final AlertDialog alertDialog = alert.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                System.out.println("positive");
                                boolean isTimeCorrectFormat = true;
                                int hour = Integer.parseInt(hourText.getText().toString());
                                if (hour < 0 || hour > 23) {
                                    hourText.setError("Invalid hour");
                                    isTimeCorrectFormat = false;
                                }

                                int minutes = Integer.parseInt(minutesText.getText().toString());
                                if (minutes < 0 || minutes > 59) {
                                    minutesText.setError("Invalid minutes");
                                    isTimeCorrectFormat = false;
                                }

                                if(isTimeCorrectFormat) {
                                    final Calendar bookingStartDate = Calendar.getInstance();
                                    bookingStartDate.set(year, month + 1, date, hour, minutes);
                                    addTalkTimeFrame(bookingStartDate.getTime());
                                    alertDialog.dismiss();
                                }
                            }
                        });

                        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        negativeButton.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });
                    }
                });

                alertDialog.show();
            }
        }, currentYear, currentMonth, currentDate);

        datePickerDialog.show();
    }

    private void addTalkTimeFrame(final Date startBookingDate) {
        final Date endBookingDate = new Date(startBookingDate.getTime() + 15 * 60000);
        final String userName = this.mSessionsActivity.getmCurrentUser().getEmail();

        this.mUserDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AdvisorSwitchingCenter advisorSwitchingCenter = new AdvisorSwitchingCenter();
                final Set<User> workingAdvisors = advisorSwitchingCenter.getWorkingAdvisors(dataSnapshot, startBookingDate);
                mTimeFrameReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User availableAdvisor = null;
                        for (User workingAdvisor : workingAdvisors) {
                            if (availableAdvisor != null) {
                                break;
                            }

                            availableAdvisor = workingAdvisor;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                TimeFrame advisorTimeFrame = snapshot.getValue(TimeFrame.class);
                                Date advisorTimeFrameStartDate = DateTimeUtil.getUTCDateTime(advisorTimeFrame.getStartDateTime());
                                Date advisorTimeFrameEndDate = DateTimeUtil.getUTCDateTime(advisorTimeFrame.getEndDateTime());

                                if (!workingAdvisor.getEmail().equals(advisorTimeFrame.getAdvisorName())) {
                                    continue;
                                }

                                if (startBookingDate.after(advisorTimeFrameStartDate) && startBookingDate.before(advisorTimeFrameEndDate)) {
                                    availableAdvisor = null;
                                    break;
                                }
                            }
                        }

                        if (availableAdvisor == null) {
                            Toast.makeText(getContext(), Config.NO_AVAILABLE_ADVISOR, Toast.LENGTH_LONG).show();
                            return;
                        }

                        String advisorName = availableAdvisor.getEmail();
                        TimeFrame timeFrame = new TimeFrame(startBookingDate, endBookingDate, TimeFrameType.VOICE, userName, advisorName);
                        mTimeFrameRepository.save(timeFrame);

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

        this.addTalk(this.mSessionsActivity.getmCurrentUser());
        enableButton();
    }

    private void enableButton() {
        this.mBtnAddTalk.setEnabled(false);
        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                mSessionsActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBtnAddTalk.setEnabled(true);
                    }
                });
            }
        }, 2000);
    }

    private void addTalk(User user) {
        user.addTalk(1);
        this.mUserRepository.updateUser(user);
    }

    private void payTalk(final User user) {
        new AlertDialog.Builder(getContext())
                .setTitle("Buy Talk")
                .setMessage("Would you like to buy a talk?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        user.addPaidTalk(1);
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

        if (timeFrame.getType() == TimeFrameType.VOICE) {
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
        }

        ((BaseAdapter) this.mListView.getAdapter()).notifyDataSetChanged();
    }

    public static Fragment newInstance() {
        SessionTalkFragment talkFragment = new SessionTalkFragment();
        return talkFragment;
    }

    private void deleteChangedTimeFrame(DataSnapshot dataSnapshot) {
        TimeFrame timeFrame = dataSnapshot.getValue(TimeFrame.class);
        List<TimeFrame> timeFrameDeleteList = new ArrayList<>();
        List<TimeFrame> timeFrameList = this.mSessionChatAdapter.getTimeFrames();
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
        if (user.getTalks() >= user.getPaidTalks()) {
            hasPaid = false;
        }

        return hasPaid;
    }
}
