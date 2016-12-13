package com.example.letstalk.activity.sessions.chat;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.letstalk.configuration.Config;
import com.example.letstalk.R;
import com.example.letstalk.domain.message.ChatMessage;
import com.example.letstalk.domain.user.User;
import com.example.letstalk.repository.MessageRepository;
import com.example.letstalk.utils.SpeechUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Date;

import static android.view.View.*;

public class ChatActivity extends AppCompatActivity implements OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private RelativeLayout mChatHolderRelativeLayout;

    private ListView mListView;

    private EditText mEditMessageText;

    private Button mSendMessageButton;

    private Button mCameraButton;

    private Button mMicrophoneButton;

    private ImageView mPicture;

    private ChatArrayAdapter mChatArrayAdapter;

    private MessageRepository messageRepository;

    private String mChatPath;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Intent i = this.getIntent();
            Bundle extras = i.getExtras();
            this.mChatPath = extras.getString(Config.CHAT_EXTRA);
            this.mUser = extras.getParcelable(Config.USER_EXTRA);
        }
        setContentView(R.layout.chat_holder);
        this.mListView = (ListView) findViewById(R.id.listViewMessageHolder);
        this.mEditMessageText = (EditText) findViewById(R.id.messageText);
        this.mSendMessageButton = (Button) findViewById(R.id.buttonSendMessage);
        this.mCameraButton = (Button) findViewById(R.id.btn_camera);
        this.mMicrophoneButton = (Button) findViewById(R.id.btn_microphone);
        this.mChatArrayAdapter = new ChatArrayAdapter(this, R.layout.chat_message, this.mUser);
        this.mPicture = (ImageView) findViewById(R.id.imageView_picture);
        this.mSendMessageButton.setOnClickListener(this);
        this.mCameraButton.setOnClickListener(this);
        this.mMicrophoneButton.setOnClickListener(this);
        this.mListView.setAdapter(this.mChatArrayAdapter);
        this.messageRepository = new MessageRepository(Config.CHILD_CHATS, this.mChatPath);
        this.messageRepository.getmDatabaseReference().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                appendMessage(dataSnapshot);
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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSendMessage:
                this.sendMessage();
                this.eraseText();
                break;
            case R.id.btn_camera:
                this.dispatchTakePictureIntent();
                break;
            case R.id.btn_microphone:
                this.recordMessage();
                break;
        }
    }

    private void recordMessage() {
        SpeechUtil.promptSpeechInput(this);
    }

    private void eraseText() {
        this.mEditMessageText.setText(null);
    }

    private void sendMessage() {
        ChatMessage chatMessage = new ChatMessage(mEditMessageText.getText().toString(), mUser.getUsername(), new Date());
        this.messageRepository.createWithKey(chatMessage);
    }

    private void appendMessage(DataSnapshot dataSnapshot) {
        ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
        this.mChatArrayAdapter.add(chatMessage);
        ((BaseAdapter) this.mListView.getAdapter()).notifyDataSetChanged();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case Config.REQ_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mEditMessageText.setText(result.get(0));
                    break;
                }

                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    this.mPicture.setImageBitmap(imageBitmap);
                }
        }
    }
}
