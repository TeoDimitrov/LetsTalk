package com.example.letstalk;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by teodo on 25/09/2016.
 */
public class ChatFragment extends Fragment implements View.OnClickListener{

    private RelativeLayout chatHolderRelativeLayout;
    private ListView listView;
    private EditText editMessageText;
    private Button sendMessageButton;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.setChatHolderRelativeLayout((RelativeLayout) inflater.inflate(R.layout.chat_holder, container, false));
        this.setDatabaseReference(FirebaseDatabase.getInstance().getReference().child(Config.CHILD_CHATS).child("advisor_user"));
        this.setListView((ListView) this.getChatHolderRelativeLayout().findViewById(R.id.listViewMessageHolder));
        this.setEditMessageText( (EditText) this.getChatHolderRelativeLayout().findViewById(R.id.messageText));
        this.setSendMessageButton((Button) this.getChatHolderRelativeLayout().findViewById(R.id.buttonSendMessage));
        this.setChatArrayAdapter(new ChatArrayAdapter(this.getActivity(), R.layout.chat_message));

        this.getSendMessageButton().setOnClickListener(this);
        this.getListView().setAdapter(this.getChatArrayAdapter());

        authenticateUser();

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

        return this.getChatHolderRelativeLayout();
    }

    private void authenticateUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("LogIn");
        final EditText editText = new EditText(this.getActivity());
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
