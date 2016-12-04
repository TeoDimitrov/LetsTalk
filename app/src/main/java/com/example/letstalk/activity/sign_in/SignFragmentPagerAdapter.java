package com.example.letstalk.activity.sign_in;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.letstalk.activity.sign_in.interfaces.TabFragmentListener;

public class SignFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private static final int PAGE_COUNT = 2;

    private static final String[] TABS = new String[]{"Sign Up", "Sign In"};

    private Fragment fragmentSignUp;

    private FragmentManager fragmentManager;

    public SignFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        this.setFragmentManager(fragmentManager);
    }

    public Fragment getFragmentSignUp() {
        return this.fragmentSignUp;
    }

    private void setFragmentSignUp(Fragment fragmentSignUp) {
        this.fragmentSignUp = fragmentSignUp;
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
                if (fragmentSignUp == null) {
                    fragmentSignUp = SignUpFragmentOne.newInstance(new TabFragmentListener() {
                        public void onSwitchToNextFragment() {
                            Bundle args = new Bundle();
                            args.putInt("birthYear", ((SignUpFragmentOne) fragmentSignUp).year);
                            args.putString("gender", ((SignUpFragmentOne) fragmentSignUp).gender);
                            getFragmentManager().beginTransaction().remove(fragmentSignUp).commit();
                            fragmentSignUp = SignUpFragmentTwo.newInstance();
                            fragmentSignUp.setArguments(args);
                            notifyDataSetChanged();
                        }
                    });
                }

                return getFragmentSignUp();
            case 1:
                return new SignInFragment().newInstance();
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

    @Override
    public int getItemPosition(Object object)
    {
        if (object instanceof SignUpFragmentOne && this.getFragmentSignUp() instanceof SignUpFragmentTwo) {
            return POSITION_NONE;
        }

        return POSITION_UNCHANGED;
    }
}
