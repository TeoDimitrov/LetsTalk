package com.example.letstalk.repository;

import com.example.letstalk.domain.message.ChatMessage;
import com.google.firebase.database.DatabaseReference;

public class MessageRepository {

    private DatabaseReference mDatabaseReference;

    public MessageRepository(String url, String chat) {
        this.mDatabaseReference = com.google.firebase.database.FirebaseDatabase.getInstance().getReference().child(url).child(chat);
    }

    public void createWithKey(ChatMessage message){
        String key = this.mDatabaseReference.push().getKey();
        this.mDatabaseReference.child(key).setValue(message);
    }

    public DatabaseReference getmDatabaseReference() {
        return this.mDatabaseReference;
    }
}
