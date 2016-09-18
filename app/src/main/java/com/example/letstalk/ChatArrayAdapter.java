package com.example.letstalk;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by teodo on 18/09/2016.
 */
public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {


    public ChatArrayAdapter(Context context, int resource, int textViewResourceId, List<ChatMessage> objects) {
        super(context, resource, textViewResourceId, objects);
    }
}
