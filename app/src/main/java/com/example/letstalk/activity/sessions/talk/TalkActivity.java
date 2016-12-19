package com.example.letstalk.activity.sessions.talk;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
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


public class TalkActivity extends AppCompatActivity implements View.OnClickListener {

    private SinchClient mSinchClient;

    private Call mCall;

    private ImageButton mTalk;

    private ImageButton mDisconnect;

    private TextView mInfoText;

    private String mCallerId;

    private String mRecipientId;

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
                //this.mSinchClient.getCallClient().callUser(mRecipientId);
                if (this.mCall == null) {
                    this.mCall = this.mSinchClient.getCallClient().callUser(mRecipientId);
                    this.mInfoText.setText("Hang Up");
                }
                break;
            case R.id.button_hang_up_id:
                if (this.mCall != null) {
                    this.mCall.hangup();
                    this.mCall = null;
                    this.mInfoText.setText("Call");
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
        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            //incoming mCall was picked up
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
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
            mCall = incomingCall;
            mCall.answer();
            mCall.addCallListener(new SinchCallListener());
            mInfoText.setText("Hang Up");
        }
    }
}
