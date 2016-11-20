package com.example.letstalk.repository;

import com.example.letstalk.domain.message.ChatMessage;
import com.example.letstalk.domain.message.interfaces.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class MessageRepository {

    private DatabaseReference mDatabaseReference;

    public MessageRepository(String url) {
        this.mDatabaseReference = com.google.firebase.database.FirebaseDatabase.getInstance().getReference().child(url);
    }

    public void createWithKey(Message message){
        String key = this.mDatabaseReference.push().getKey();
        this.mDatabaseReference.child(key).setValue(message);
    }

    public Message getMessage(DataSnapshot dataSnapshot){
        Message message = dataSnapshot.getValue(ChatMessage.class);
        return message;
    }

    public DatabaseReference getmDatabaseReference() {
        return this.mDatabaseReference;
    }
}
