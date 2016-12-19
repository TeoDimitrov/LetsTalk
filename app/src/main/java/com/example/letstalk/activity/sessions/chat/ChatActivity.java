package com.example.letstalk.activity.sessions.chat;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.letstalk.R;
import com.example.letstalk.activity.sessions.note.NotesActivity;
import com.example.letstalk.configuration.Config;
import com.example.letstalk.domain.message.ChatMessage;
import com.example.letstalk.domain.user.User;
import com.example.letstalk.repository.MessageRepository;
import com.example.letstalk.utils.BitmapUtil;
import com.example.letstalk.utils.SpeechUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.view.View.OnClickListener;

public class ChatActivity extends AppCompatActivity implements OnClickListener {

    private RelativeLayout mChatHolderRelativeLayout;

    private ListView mListView;

    private EditText mEditMessageText;

    private Button mSendMessageButton;

    private Button mCameraButton;

    private Button mMicrophoneButton;

    private PopupWindow mPopupWindow;

    private ChatArrayAdapter mChatArrayAdapter;

    private MessageRepository mMessageRepository;

    private String mChatPath;

    private User mUser;

    private User mClient;

    private Intent mNotesIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_holder);
        if (savedInstanceState == null) {
            Intent i = this.getIntent();
            Bundle extras = i.getExtras();
            this.mChatPath = extras.getString(Config.CHAT_EXTRA);
            this.mUser = extras.getParcelable(Config.USER_EXTRA);
            this.mClient = extras.getParcelable(Config.CLIENT_USER_EXTRA);

        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.setActionBarTitle(this.mClient);
        this.mChatHolderRelativeLayout = (RelativeLayout) findViewById(R.id.chat_holder_id);
        this.mNotesIntent = new Intent(this, NotesActivity.class);
        this.mEditMessageText = (EditText) findViewById(R.id.messageText);
        this.mSendMessageButton = (Button) findViewById(R.id.buttonSendMessage);
        this.mCameraButton = (Button) findViewById(R.id.btn_camera);
        this.mPopupWindow = new PopupWindow(200, 200);
        this.mMicrophoneButton = (Button) findViewById(R.id.btn_microphone);
        this.mSendMessageButton.setOnClickListener(this);
        this.mCameraButton.setOnClickListener(this);
        this.mMicrophoneButton.setOnClickListener(this);
        this.mChatArrayAdapter = new ChatArrayAdapter(this, R.layout.chat_message, this.mUser);
        this.mListView = (ListView) findViewById(R.id.listViewMessageHolder);
        this.mListView.setAdapter(this.mChatArrayAdapter);
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ChatMessage chatMessage = (ChatMessage) parent.getItemAtPosition(position);
                showEnlargedBitmap(chatMessage);
            }

            private void showEnlargedBitmap(ChatMessage chatMessage) {
                if (chatMessage.getEncodedImage() != null) {
                    Bitmap messageBitmap = chatMessage.getEncodedBitmapImage();
                    Bitmap enlargedBitmap = BitmapUtil.scaleBitmap(messageBitmap, 500, 500);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.chat_image_popup, null, false);
                    ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.enlarged_image_id);
                    imageView.setImageBitmap(enlargedBitmap);
                    Button backButton = (Button) relativeLayout.findViewById(R.id.enlarged_image_back_button_id);
                    backButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mPopupWindow.dismiss();
                        }
                    });

                    mPopupWindow.setContentView(relativeLayout);
                    mPopupWindow.showAtLocation(mChatHolderRelativeLayout, Gravity.CENTER, 0, 0);
                    mPopupWindow.update(500, 500, 500, 500);
                }
            }
        });

        this.mMessageRepository = new MessageRepository(Config.CHILD_CHATS, this.mChatPath);
        this.mMessageRepository.getmDatabaseReference().addChildEventListener(new ChildEventListener() {
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
        this.mMessageRepository.create(chatMessage);
    }

    private void sendMessage(Bitmap bitmapImage) {
        ChatMessage chatMessage = new ChatMessage(bitmapImage, mUser.getUsername(), new Date());
        this.mMessageRepository.create(chatMessage);
    }

    private void appendMessage(DataSnapshot dataSnapshot) {
        ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
        this.mChatArrayAdapter.add(chatMessage);
        ((BaseAdapter) this.mListView.getAdapter()).notifyDataSetChanged();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, Config.REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case Config.REQUEST_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mEditMessageText.setText(result.get(0));

                }
                break;
            case Config.REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    sendMessage(imageBitmap);
                }
                break;
            case Config.REQUEST_RETURN_CLIENT:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    this.mClient = extras.getParcelable(Config.CLIENT_USER_EXTRA);
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.mUser.getRole().getName().equals("AdvisorRole")) {
            menu.add(0, 1, 0, "Notes").setIcon(R.drawable.ic_note_add_white_24dp)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        if (item.getTitle() != null) {
            if (item.getTitle().equals("Notes")) {
                this.goToNotes();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToNotes() {
        this.mNotesIntent.putExtra(Config.CLIENT_USER_EXTRA, this.mClient);
        this.startActivityForResult(this.mNotesIntent, Config.REQUEST_RETURN_CLIENT);
    }

    private void setActionBarTitle(User client) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int age = currentYear - client.getBirthDate();
        StringBuilder title = new StringBuilder();
        title.append(client.getGender());
        title.append(", ");
        title.append(age);
        getSupportActionBar().setTitle(title);
    }
}