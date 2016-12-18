package com.example.letstalk.activity.sessions.chat;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.letstalk.Utilities.Camera;
import com.example.letstalk.configuration.Config;
import com.example.letstalk.R;
import com.example.letstalk.domain.message.ChatMessage;
import com.example.letstalk.domain.message.interfaces.Message;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.*;

public class ChatActivity extends AppCompatActivity implements OnClickListener{

    private RelativeLayout chatHolderRelativeLayout;
    private ListView listView;
    private EditText editMessageText;
    private Button sendMessageButton;
    private Button cameraButton;
    private Button geolocationButton;
    private ImageView picture;
    private ChatArrayAdapter chatArrayAdapter;
    private DatabaseReference databaseReference;
    private String user;
    private Camera camera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_holder);

        this.databaseReference = (FirebaseDatabase.getInstance().getReference().child(Config.CHILD_CHATS).child("advisor_user"));

        this.listView = ((ListView)findViewById(R.id.listViewMessageHolder));
        this.editMessageText = ( (EditText)findViewById(R.id.messageText));
        this.sendMessageButton = ((Button)findViewById(R.id.buttonSendMessage));
        this.cameraButton = ((Button)findViewById(R.id.btn_camera));
        this.geolocationButton = ((Button)findViewById(R.id.btn_geolocation));
        this.chatArrayAdapter = (new ChatArrayAdapter(this, R.layout.chat_message));
        this.picture = ((ImageView)findViewById(R.id.imageView_picture));

        this.sendMessageButton.setOnClickListener(this);
        this.cameraButton.setOnClickListener(this);
        this.geolocationButton.setOnClickListener(this);

        this.listView.setAdapter(this.chatArrayAdapter);

        this.databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                appendMessage(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                appendMessage(dataSnapshot);
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
        switch (v.getId()){
            case R.id.buttonSendMessage:
                sendMessage();
                eraseText();
                break;
            case R.id.btn_camera:
                this.camera = new Camera();
                File photo = this.camera.dispatchTakePictureIntent(this);
            break;
            case R.id.btn_geolocation:

                break;
        }
    }

    private void eraseText() {
        this.editMessageText.setText(null);
    }

    private void sendMessage() {
        String messageKey = databaseReference.push().getKey();
        databaseReference.child(messageKey);
        Message message = new ChatMessage(editMessageText.getText().toString(), user);
        Map<String, Object> messageDetails = new HashMap<>();
        messageDetails.put("message", message.getMessage());
        messageDetails.put("author", message.getAuthor());
        messageDetails.put("date", message.getUTCDate());

        DatabaseReference messageRootDatabase = databaseReference.child(messageKey);
        messageRootDatabase.updateChildren(messageDetails);
    }

    private void appendMessage(DataSnapshot dataSnapshot) {
        Message message = dataSnapshot.getValue(ChatMessage.class);
        this.chatArrayAdapter.add(message);
        ((BaseAdapter)this.listView.getAdapter()).notifyDataSetChanged();
    }





}
