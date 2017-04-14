package com.wecode.letstalk.activity.sessions.schedule;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wecode.letstalk.R;
import com.wecode.letstalk.configuration.Config;
import com.wecode.letstalk.domain.schedule.WorkDay;
import com.wecode.letstalk.domain.user.User;
import com.wecode.letstalk.utils.FirebaseUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    private TextView mDayTextViewMON;

    private EditText mStartDateEditViewMON;

    private EditText mEndDateEditViewMON;

    private CheckBox mDayCheckBoxMON;

    private TextView mDayTextViewTUE;

    private EditText mStartDateEditViewTUE;

    private EditText mEndDateEditViewTUE;

    private CheckBox mDayCheckBoxTUE;

    private TextView mDayTextViewWED;

    private EditText mStartDateEditViewWED;

    private EditText mEndDateEditViewWED;

    private CheckBox mDayCheckBoxWED;

    private TextView mDayTextViewTHU;

    private EditText mStartDateEditViewTHU;

    private EditText mEndDateEditViewTHU;

    private CheckBox mDayCheckBoxTHU;

    private TextView mDayTextViewFRI;

    private EditText mStartDateEditViewFRI;

    private EditText mEndDateEditViewFRI;

    private CheckBox mDayCheckBoxFRI;

    private TextView mDayTextViewSAT;

    private EditText mStartDateEditViewSAT;

    private EditText mEndDateEditViewSAT;

    private CheckBox mDayCheckBoxSAT;

    private TextView mDayTextViewSUN;

    private EditText mStartDateEditViewSUN;

    private EditText mEndDateEditViewSUN;

    private CheckBox mDayCheckBoxSUN;

    private SimpleDateFormat mTimeFormat;

    private User mCurrentUser;

    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        this.receiveAuthenticatedUser(savedInstanceState);
        this.prepareTimeFormat();
        this.prepareViews();
        this.loadWorkDays();
        this.prepareFirebase();
    }

    private void receiveAuthenticatedUser(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            this.mCurrentUser = extras.getParcelable(Config.USER_AUTHOR_EXTRA);
        }
    }

    private void prepareTimeFormat(){
        mTimeFormat = new SimpleDateFormat("HH:mm");
    }

    private void prepareViews() {
        //Mon
        this.mDayTextViewMON = (TextView) findViewById(R.id.dayNameMON);
        this.mStartDateEditViewMON = (EditText) findViewById(R.id.startDateMON);
        this.mEndDateEditViewMON = (EditText) findViewById(R.id.endDateMON);
        this.mDayCheckBoxMON = (CheckBox) findViewById(R.id.isEnabledMON);
        //Tue
        this.mDayTextViewTUE = (TextView) findViewById(R.id.dayNameTUE);
        this.mStartDateEditViewTUE = (EditText) findViewById(R.id.startDateTUE);
        this.mEndDateEditViewTUE = (EditText) findViewById(R.id.endDateTUE);
        this.mDayCheckBoxTUE = (CheckBox) findViewById(R.id.isEnabledTUE);
        //Wed
        this.mDayTextViewWED = (TextView) findViewById(R.id.dayNameWED);
        this.mStartDateEditViewWED = (EditText) findViewById(R.id.startDateWED);
        this.mEndDateEditViewWED = (EditText) findViewById(R.id.endDateWED);
        this.mDayCheckBoxWED = (CheckBox) findViewById(R.id.isEnabledWED);
        //Thu
        this.mDayTextViewTHU = (TextView) findViewById(R.id.dayNameTHU);
        this.mStartDateEditViewTHU = (EditText) findViewById(R.id.startDateTHU);
        this.mEndDateEditViewTHU = (EditText) findViewById(R.id.endDateTHU);
        this.mDayCheckBoxTHU = (CheckBox) findViewById(R.id.isEnabledTHU);
        //Fri
        this.mDayTextViewFRI = (TextView) findViewById(R.id.dayNameFRI);
        this.mStartDateEditViewFRI = (EditText) findViewById(R.id.startDateFRI);
        this.mEndDateEditViewFRI = (EditText) findViewById(R.id.endDateFRI);
        this.mDayCheckBoxFRI = (CheckBox) findViewById(R.id.isEnabledFRI);
        //Sat
        this.mDayTextViewSAT = (TextView) findViewById(R.id.dayNameSAT);
        this.mStartDateEditViewSAT = (EditText) findViewById(R.id.startDateSAT);
        this.mEndDateEditViewSAT = (EditText) findViewById(R.id.endDateSAT);
        this.mDayCheckBoxSAT = (CheckBox) findViewById(R.id.isEnabledSAT);
        //Sun
        this.mDayTextViewSUN = (TextView) findViewById(R.id.dayNameSUN);
        this.mStartDateEditViewSUN = (EditText) findViewById(R.id.startDateSUN);
        this.mEndDateEditViewSUN = (EditText) findViewById(R.id.endDateSUN);
        this.mDayCheckBoxSUN = (CheckBox) findViewById(R.id.isEnabledSUN);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void loadWorkDays() {
        List<WorkDay> workDays = this.mCurrentUser.getSchedule().getWorkDays();
        //Mon
        WorkDay workDayMON = workDays.get(0);
        this.mDayTextViewMON.setText(workDayMON.getName());
        this.mStartDateEditViewMON.setText(mTimeFormat.format(workDayMON.getStartTime()));
        this.mEndDateEditViewMON.setText(mTimeFormat.format(workDayMON.getEndTime()));
        this.mDayCheckBoxMON.setChecked(workDayMON.isEnabled());
        //Tue
        WorkDay workDayTUE = workDays.get(1);
        this.mDayTextViewTUE.setText(workDayTUE.getName());
        this.mStartDateEditViewTUE.setText(mTimeFormat.format(workDayTUE.getStartTime()));
        this.mEndDateEditViewTUE.setText(mTimeFormat.format(workDayTUE.getEndTime()));
        this.mDayCheckBoxTUE.setChecked(workDayTUE.isEnabled());
        //Wed
        WorkDay workDayWED = workDays.get(2);
        this.mDayTextViewWED.setText(workDayWED.getName());
        this.mStartDateEditViewWED.setText(mTimeFormat.format(workDayWED.getStartTime()));
        this.mEndDateEditViewWED.setText(mTimeFormat.format(workDayWED.getEndTime()));
        this.mDayCheckBoxWED.setChecked(workDayWED.isEnabled());
        //Thu
        WorkDay workDayTHU = workDays.get(3);
        this.mDayTextViewTHU.setText(workDayTHU.getName());
        this.mStartDateEditViewTHU.setText(mTimeFormat.format(workDayTHU.getStartTime()));
        this.mEndDateEditViewTHU.setText(mTimeFormat.format(workDayTHU.getEndTime()));
        this.mDayCheckBoxTHU.setChecked(workDayTHU.isEnabled());
        //Fri
        WorkDay workDayFRI = workDays.get(4);
        this.mDayTextViewFRI.setText(workDayFRI.getName());
        this.mStartDateEditViewFRI.setText(mTimeFormat.format(workDayFRI.getStartTime()));
        this.mEndDateEditViewFRI.setText(mTimeFormat.format(workDayFRI.getEndTime()));
        this.mDayCheckBoxFRI.setChecked(workDayFRI.isEnabled());
        //Sat
        WorkDay workDaySAT = workDays.get(5);
        this.mDayTextViewSAT.setText(workDaySAT.getName());
        this.mStartDateEditViewSAT.setText(mTimeFormat.format(workDaySAT.getStartTime()));
        this.mEndDateEditViewSAT.setText(mTimeFormat.format(workDaySAT.getEndTime()));
        this.mDayCheckBoxSAT.setChecked(workDaySAT.isEnabled());
        //Sun
        WorkDay workDaySUN = workDays.get(6);
        this.mDayTextViewSUN.setText(workDaySUN.getName());
        this.mStartDateEditViewSUN.setText(mTimeFormat.format(workDaySUN.getStartTime()));
        this.mEndDateEditViewSUN.setText(mTimeFormat.format(workDaySUN.getEndTime()));
        this.mDayCheckBoxSUN.setChecked(workDaySUN.isEnabled());
    }

    private void prepareFirebase() {
        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Config.CHILD_USERS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveWorkDays();
                returnToSessionActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getWorkDaysValues() throws ParseException {
        List<WorkDay> workDays = this.mCurrentUser.getSchedule().getWorkDays();
        //Mon
        WorkDay workDayMON = workDays.get(0);
        workDayMON.setName(this.mDayTextViewMON.getText().toString());
        workDayMON.setStartTime(this.mTimeFormat.parse(this.mStartDateEditViewMON.getText().toString()));
        workDayMON.setEndTime(this.mTimeFormat.parse(this.mEndDateEditViewMON.getText().toString()));
        workDayMON.setEnabled(this.mDayCheckBoxMON.isChecked());
        //Tue
        WorkDay workDayTUE = workDays.get(1);
        workDayTUE.setName(this.mDayTextViewTUE.getText().toString());
        workDayTUE.setStartTime(this.mTimeFormat.parse(this.mStartDateEditViewTUE.getText().toString()));
        workDayTUE.setEndTime(this.mTimeFormat.parse(this.mEndDateEditViewTUE.getText().toString()));
        workDayTUE.setEnabled(this.mDayCheckBoxTUE.isChecked());
        //Wed
        WorkDay workDayWED = workDays.get(2);
        workDayWED.setName(this.mDayTextViewWED.getText().toString());
        workDayWED.setStartTime(this.mTimeFormat.parse(this.mStartDateEditViewWED.getText().toString()));
        workDayWED.setEndTime(this.mTimeFormat.parse(this.mEndDateEditViewWED.getText().toString()));
        workDayWED.setEnabled(this.mDayCheckBoxWED.isChecked());
        //Thu
        WorkDay workDayTHU = workDays.get(3);
        workDayTHU.setName(this.mDayTextViewTHU.getText().toString());
        workDayTHU.setStartTime(this.mTimeFormat.parse(this.mStartDateEditViewTHU.getText().toString()));
        workDayTHU.setEndTime(this.mTimeFormat.parse(this.mEndDateEditViewTHU.getText().toString()));
        workDayTHU.setEnabled(this.mDayCheckBoxTHU.isChecked());
        //Fri
        WorkDay workDayFRI = workDays.get(4);
        workDayMON.setName(this.mDayTextViewMON.getText().toString());
        workDayMON.setStartTime(this.mTimeFormat.parse(this.mStartDateEditViewMON.getText().toString()));
        workDayMON.setEndTime(this.mTimeFormat.parse(this.mEndDateEditViewMON.getText().toString()));
        workDayMON.setEnabled(this.mDayCheckBoxMON.isChecked());
        //Sat
        WorkDay workDaySAT = workDays.get(5);
        workDaySAT.setName(this.mDayTextViewSAT.getText().toString());
        workDaySAT.setStartTime(this.mTimeFormat.parse(this.mStartDateEditViewSAT.getText().toString()));
        workDaySAT.setEndTime(this.mTimeFormat.parse(this.mEndDateEditViewSAT.getText().toString()));
        workDaySAT.setEnabled(this.mDayCheckBoxSAT.isChecked());
        //Sun
        WorkDay workDaySUN = workDays.get(6);
        workDaySUN.setName(this.mDayTextViewSUN.getText().toString());
        workDaySUN.setStartTime(this.mTimeFormat.parse(this.mStartDateEditViewSUN.getText().toString()));
        workDaySUN.setEndTime(this.mTimeFormat.parse(this.mEndDateEditViewSUN.getText().toString()));
        workDaySUN.setEnabled(this.mDayCheckBoxSUN.isChecked());
    }

    private void saveWorkDays(){
        try {
            getWorkDaysValues();
        } catch (ParseException e) {
            Toast.makeText(this, R.string.schedule_fail, Toast.LENGTH_SHORT).show();
        }

        String email = this.mCurrentUser.getEmail();
        final String userPath = FirebaseUtils.clearUserName(email);
        this.mDatabaseReference.child(userPath).setValue(mCurrentUser);
    }

    private void returnToSessionActivity(){
        setResult(Activity.RESULT_OK,
                new Intent().putExtra(Config.USER_AUTHOR_EXTRA, this.mCurrentUser));
        finish();
    }
}
