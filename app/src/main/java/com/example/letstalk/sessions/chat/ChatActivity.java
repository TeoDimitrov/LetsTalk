package com.example.letstalk.sessions.chat;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.letstalk.Config;
import com.example.letstalk.R;
import com.example.letstalk.messages.ChatMessage;
import com.example.letstalk.messages.interfaces.Message;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.*;

/**
 * Created by teodo on 25/09/2016.
 */
public class ChatActivity extends AppCompatActivity implements OnClickListener{

    static final int REQUEST_IMAGE_CAPTURE = 1;

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

    private RelativeLayout getChatHolderRelativeLayout() {
        return this.chatHolderRelativeLayout;
    }

    private void setChatHolderRelativeLayout(RelativeLayout chatHolderRelativeLayout) {
        this.chatHolderRelativeLayout = chatHolderRelativeLayout;
    }

    private ListView getListView() {
        return this.listView;
    }

    private void setListView(ListView listView) {
        this.listView = listView;
    }

    private EditText getEditMessageText() {
        return this.editMessageText;
    }

    private void setEditMessageText(EditText editMessageText) {
        this.editMessageText = editMessageText;
    }

    private Button getSendMessageButton() {
        return this.sendMessageButton;
    }

    private void setSendMessageButton(Button sendMessageButton) {
        this.sendMessageButton = sendMessageButton;
    }
    public Button getGeolocationButton() {
        return geolocationButton;
    }

    public void setGeolocationButton(Button geolocationButton) {
        this.geolocationButton = geolocationButton;
    }

    public Button getCameraButton() {
        return cameraButton;
    }

    public void setCameraButton(Button cameraButton) {
        this.cameraButton = cameraButton;
    }

    public ImageView getPicture() {
        return picture;
    }

    public void setPicture(ImageView picture) {
        this.picture = picture;
    }


    private ChatArrayAdapter getChatArrayAdapter() {
        return this.chatArrayAdapter;
    }

    private void setChatArrayAdapter(ChatArrayAdapter chatArrayAdapter) {
        this.chatArrayAdapter = chatArrayAdapter;
    }

    private DatabaseReference getDatabaseReference() {
        return this.databaseReference;
    }

    private void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_holder);

        this.setDatabaseReference(FirebaseDatabase.getInstance().getReference().child(Config.CHILD_CHATS).child("advisor_user"));

        this.setListView((ListView)findViewById(R.id.listViewMessageHolder));
        this.setEditMessageText( (EditText)findViewById(R.id.messageText));
        this.setSendMessageButton((Button)findViewById(R.id.buttonSendMessage));
        this.setCameraButton((Button)findViewById(R.id.btn_camera));
        this.setGeolocationButton((Button)findViewById(R.id.btn_geolocation));
        this.setChatArrayAdapter(new ChatArrayAdapter(this, R.layout.chat_message));
        this.setPicture((ImageView)findViewById(R.id.imageView_picture));

        this.getSendMessageButton().setOnClickListener(this);
        this.getCameraButton().setOnClickListener(this);
        this.getGeolocationButton().setOnClickListener(this);

        this.getListView().setAdapter(this.getChatArrayAdapter());

        this.getDatabaseReference().addChildEventListener(new ChildEventListener() {
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
                dispatchTakePictureIntent();
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            this.getPicture().setImageBitmap(imageBitmap);
        }
    }

}
