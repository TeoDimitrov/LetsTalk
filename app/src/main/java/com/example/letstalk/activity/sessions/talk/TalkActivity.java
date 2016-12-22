package com.example.letstalk.activity.sessions.talk;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.letstalk.R;
import com.example.letstalk.configuration.Config;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class TalkActivity extends AppCompatActivity implements View.OnClickListener {

    private SinchClient mSinchClient;

    private Call mCall;

    private ImageButton mTalk;

    private ImageButton mDisconnect;

    private TextView mInfoText;

    private TextView mStatusInfo;

    private String mCallerId;

    private String mRecipientId;

    private int mCallDuration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_talk);
        checkSystemWritePermission();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            this.mCallerId = extras.getString(Config.SINCH_CALLER_ID);
            this.mRecipientId = extras.getString(Config.SINCH_RECIPIENT_ID);
        }

        this.mInfoText = (TextView) findViewById(R.id.info_text_id);
        this.mStatusInfo = (TextView) findViewById(R.id.talk_status_id);
        this.mTalk = (ImageButton) findViewById(R.id.button_talk_id);
        this.mTalk.setOnClickListener(this);
        this.mDisconnect = (ImageButton) findViewById(R.id.button_hang_up_id);
        this.mDisconnect.setOnClickListener(this);
        this.mSinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(mCallerId)
                .applicationKey(Config.SINCH_KEY)
                .applicationSecret(Config.SINCH_SECRET)
                .environmentHost(Config.SINCH_HOSTNAME)
                .build();
        //Listen for calls
        this.mSinchClient.startListeningOnActiveConnection();
        //Start client
        this.mSinchClient.setSupportCalling(true);
        this.mSinchClient.start();
        //Listen
        this.mSinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_talk_id:
                if (this.mCall == null) {
                    mStatusInfo.setText("Calling...");
                    this.mCall = this.mSinchClient.getCallClient().callUser(mRecipientId);
                    this.mCall.addCallListener(new SinchCallListener());
                    this.mInfoText.setText("Hang Up");
                } else {
                    this.mCall.answer();
                    this.countDuration();
                }

                break;
            case R.id.button_hang_up_id:
                if (this.mCall != null) {
                    this.mStatusInfo.setText(null);
                    this.mCall.hangup();
                    this.mCall = null;
                    this.mInfoText.setText("Call");
                    finish();
                }
                break;
        }
    }

    private void checkSystemWritePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    Config.REQUEST_MICROPHONE);
        }
    }

    private class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            //mCall ended by either party
            mCall = null;
            mInfoText.setText("Call");
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            finish();
        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            //incoming mCall was picked up
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            countDuration();
        }

        @Override
        public void onCallProgressing(Call progressingCall) {
            //mCall is ringing
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            //don't worry about this right now
        }
    }

    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            //Pick up the mCall!
            mStatusInfo.setText("Incoming call...");
            mCall = incomingCall;
            //mCall.answer();
            mCall.addCallListener(new SinchCallListener());
            mInfoText.setText("Hang Up");
        }
    }

    private void countDuration() {
        mCallDuration = 0;
        final Handler handler = new Handler();
        Timer timer = new Timer(false);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        int minutes = mCallDuration / 60;
                        int seconds = mCallDuration - minutes * 60;
                        mStatusInfo.setText(String.valueOf(String.format("%02d:%02d", minutes, seconds)));
                        mCallDuration++;
                    }
                });
            }
        };

        timer.schedule(timerTask, 1000, 1000);
    }
}
