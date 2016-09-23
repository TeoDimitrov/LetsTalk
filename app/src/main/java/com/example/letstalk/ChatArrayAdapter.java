package com.example.letstalk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by teodo on 18/09/2016.
 */
public class ChatArrayAdapter extends ArrayAdapter<Message> {

    private Context activityContext;
    private int resource;
    private List<Message> messages;

    public ChatArrayAdapter(Context context, int resource) {
        super(context, resource);
        this.setActivityContext(context);
        this.setResource(resource);
        this.setMessages(new ArrayList<Message>());
    }

    private Context getActivityContext() {
        return this.activityContext;
    }

    private void setActivityContext(Context activityContext) {
        this.activityContext = activityContext;
    }

    private int getResource() {
        return this.resource;
    }

    private void setResource(int resource) {
        this.resource = resource;
    }

    private List<Message> getMessages() {
        return this.messages;
    }

    private void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public void add(Message message) {
        this.getMessages().add(message);
    }

    @Override
    public Message getItem(int position) {
        return this.getMessages().get(position);
    }

    @Override
    public int getPosition(Message item) {
        return this.getMessages().indexOf(item);
    }

    @Override
    public int getCount() {
        return this.getMessages().size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getActivityContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(resource ,null);
        }

        Message message = this.getItem(position);
        TextView textViewChatMessage = (TextView) convertView.findViewById(R.id.chatMessage);
        textViewChatMessage.setText(message.getMessage());
        TextView textViewTime = (TextView) convertView.findViewById(R.id.chatTime);
        textViewTime.setText(message.getTime());

        return convertView;
    }
}
