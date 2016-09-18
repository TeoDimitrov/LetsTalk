package com.example.letstalk;

import android.content.DialogInterface;
import android.os.*;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private EditText editMessageText;
    private Button sendMessageButton;
    ArrayAdapter<ChatMessage> chatMessageArrayAdapter;
    List<ChatMessage> chatMessages = new ArrayList<>();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_holder);

        this.listView = (ListView) findViewById(R.id.listViewMessageHolder);
        this.editMessageText = (EditText) this.findViewById(R.id.messageText);
        this.sendMessageButton = (Button) findViewById(R.id.buttonSendMessage);
        this.sendMessageButton.setOnClickListener(this);

        this.chatMessageArrayAdapter = new ArrayAdapter<ChatMessage>(this, R.layout.chat_message, chatMessages);
        listView.setAdapter(chatMessageArrayAdapter);
        authenticateUser();
    }

    private String user;
    private void authenticateUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("LogIn");
        final EditText editText = new EditText(this);
        builder.setView(editText);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                user = editText.getText().toString();
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
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                Map<String, Object> message = new HashMap<>();
                message.put(this.editMessageText.getText().toString(),"");
                databaseReference.updateChildren(message);
        }
    }
}
