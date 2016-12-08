package com.example.letstalk.activity.sessions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.letstalk.R;
import com.example.letstalk.domain.timeFrames.TimeFrame;
import com.example.letstalk.domain.user.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SessionChatAdapter extends ArrayAdapter<TimeFrame> {

    private final Context context;

    private final List<TimeFrame>  timeFrames;

    private CircleImageView circleImageView;

    private TextView infoTextView;

    private TextView scheduleTextView;

    public SessionChatAdapter(Context context, int resource, List<TimeFrame> timeFrames) {
        super(context, resource, timeFrames);
        this.context = context;
        this.timeFrames = timeFrames;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.session_item, parent, false);
        this.circleImageView = (CircleImageView) rowView.findViewById(R.id.profile_picture_id);
        this.infoTextView = (TextView) rowView.findViewById(R.id.info_text_id);
        this.infoTextView.setText(String.valueOf(this.timeFrames.get(position).getStatus()));
        this.scheduleTextView = (TextView) rowView.findViewById(R.id.schedule_text_id);
        String timeRange = this.timeFrames.get(position).getStartDateTime() + " - " + this.timeFrames.get(position).getEndDateTime();
        this.scheduleTextView.setText(timeRange);

        return rowView;
    }

    @Override
    public void add(TimeFrame timeFrame) {
        this.timeFrames.add(timeFrame);
    }

    @Override
    public int getPosition(TimeFrame item) {
        return this.timeFrames.indexOf(item);
    }

    @Override
    public TimeFrame getItem(int position) {
       return this.timeFrames.get(position);
    }
}
