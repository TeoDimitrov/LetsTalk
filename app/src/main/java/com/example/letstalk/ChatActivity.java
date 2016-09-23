package com.example.letstalk;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private EditText editMessageText;
    private Button sendMessageButton;
    private ChatArrayAdapter chatArrayAdapter;
    private DatabaseReference databaseReference;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_holder);

        this.databaseReference = FirebaseDatabase.getInstance().getReference().child(Config.CHILD_CHATS).child("advisor_user");

        this.listView = (ListView) findViewById(R.id.listViewMessageHolder);
        this.editMessageText = (EditText) this.findViewById(R.id.messageText);
        this.sendMessageButton = (Button) findViewById(R.id.buttonSendMessage);
        this.sendMessageButton.setOnClickListener(this);

        this.chatArrayAdapter = new ChatArrayAdapter(this, R.layout.chat_message);
        listView.setAdapter(this.chatArrayAdapter);

        authenticateUser();

        databaseReference.addChildEventListener(new ChildEventListener() {
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

    private void authenticateUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("LogIn");
        final EditText editText = new EditText(this);
        builder.setView(editText);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                user = editText.getText().toString() + Config.USER_SUFIX;
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                authenticateUser();
            }
        });

        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonSendMessage:
                sendMessage();
                eraseText();
                Toast.makeText(ChatActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
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