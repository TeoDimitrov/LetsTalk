package com.example.letstalk.activity.sessions.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.letstalk.R;
import com.example.letstalk.domain.message.ChatMessage;
import com.example.letstalk.domain.user.User;

import java.util.ArrayList;
import java.util.List;

public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

    private Context activityContext;

    private int resource;

    private List<ChatMessage> messages;

    private User user;

    public ChatArrayAdapter(Context context, int resource, User user) {
        super(context, resource);
        this.activityContext = context;
        this.resource = resource;
        this.messages = new ArrayList<>();
        this.user = user;
    }


    @Override
    public void add(ChatMessage message) {
        this.messages.add(message);
    }

    @Override
    public ChatMessage getItem(int position) {
        return this.messages.get(position);
    }

    @Override
    public int getPosition(ChatMessage item) {
        return this.messages.indexOf(item);
    }

    @Override
    public int getCount() {
        return this.messages.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.activityContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(resource, null);
        }

        ChatMessage chatMessage = this.getItem(position);
        LinearLayout messageLinearLayout = (LinearLayout) convertView.findViewById(R.id.message_linear_layout_id);
        RelativeLayout messageRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.message_relative_layout_id);
        ImageView picture = (ImageView) convertView.findViewById(R.id.picture_id);
        TextView textViewTime = (TextView) convertView.findViewById(R.id.chatTime);
        textViewTime.setText(chatMessage.getLocalTime());
        TextView textViewChatMessage = (TextView) convertView.findViewById(R.id.chat_message_id);
        if (this.user.getEmail().equals(chatMessage.getAuthor())) {
            messageLinearLayout.setGravity(Gravity.RIGHT);
            messageRelativeLayout.setBackgroundResource(R.drawable.chat_message_shape_current_user);
            textViewChatMessage.setTextColor(ContextCompat.getColor(activityContext, R.color.white));
            textViewTime.setTextColor(ContextCompat.getColor(activityContext, R.color.white));
        } else {
            messageLinearLayout.setGravity(Gravity.LEFT);
            messageRelativeLayout.setBackgroundResource(R.drawable.chat_message_shape_other_user);
            textViewChatMessage.setTextColor(ContextCompat.getColor(activityContext, R.color.darkGray));
            textViewTime.setTextColor(ContextCompat.getColor(activityContext, R.color.gray));
        }

        if (chatMessage.getEncodedImage() != null) {
            Bitmap bitmapImage = chatMessage.getEncodedBitmapImage();
            picture.setImageBitmap(bitmapImage);
            textViewChatMessage.setText(null);
        } else {
            textViewChatMessage.setText(chatMessage.getMessage());
            picture.setImageBitmap(null);
        }

        return convertView;
    }
}
