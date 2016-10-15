package com.example.letstalk.sessions;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.letstalk.sessions.chat.ChatFragment;
import com.example.letstalk.sessions.talk.TalkFragment;


/**
 * Created by teodo on 12/10/2016.
 */
public class SessionsFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private static final int PAGE_COUNT = 2;

    private static final String[] TABS = new String[]{"Chat", "Talk"};

    private FragmentManager fragmentManager;

    public SessionsFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        this.setFragmentManager(fragmentManager);
    }

    public FragmentManager getFragmentManager() {
        return this.fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ChatFragment.newInstance();
            case 1:
                return TalkFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TABS[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}
