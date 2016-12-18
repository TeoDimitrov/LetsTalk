package com.example.letstalk.activity.sessions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.letstalk.R;

public class SessionTalkFragment extends Fragment {

    private RelativeLayout relativeLayout;

    private ListView listView;

    private FloatingActionButton btnAddTalk;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_talk, container, false);
        this.listView = (ListView) this.relativeLayout.findViewById(R.id.session_talk_list_view_id);
        this.btnAddTalk = (FloatingActionButton) this.relativeLayout.findViewById(R.id.add_talk_id);
        return this.relativeLayout;
    }

    public static Fragment newInstance() {
        SessionTalkFragment talkFragment = new SessionTalkFragment();
        return talkFragment;
    }
}
