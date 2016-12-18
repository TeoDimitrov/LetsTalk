package com.example.letstalk.activity.sessions.note;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.letstalk.R;
import com.example.letstalk.configuration.Config;
import com.example.letstalk.domain.user.User;
import com.example.letstalk.repository.UserRepository;


public class NotesActivity extends AppCompatActivity {

    private EditText mNotesText;

    private UserRepository userRepository;

    private User mClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.notes_layout);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            this.mClient = extras.getParcelable(Config.CLIENT_USER_EXTRA);
        }

        this.mNotesText = (EditText) findViewById(R.id.notes_edit_text_id);
        this.setInitialNotes();
        this.userRepository = new UserRepository(Config.CHILD_USERS);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            menu.add(0, 1, 0, "Save").setIcon(R.drawable.ic_save_white_24dp)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                setResult(Activity.RESULT_OK,
                        new Intent().putExtra(Config.CLIENT_USER_EXTRA, this.mClient));
                finish();
                break;
        }

        if(item.getTitle() != null) {
            if (item.getTitle().equals("Save")) {
                String notes = this.mNotesText.getText().toString();
                this.mClient.setNotes(notes);
                this.userRepository.save(this.mClient);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void setInitialNotes(){
        String notes = this.mClient.getNotes();
        if (notes != null){
            this.mNotesText.setText(notes);
        }
    }
}
