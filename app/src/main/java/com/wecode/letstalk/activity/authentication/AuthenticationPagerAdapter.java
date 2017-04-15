package com.wecode.letstalk.activity.authentication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wecode.letstalk.activity.authentication.signIn.SignInFragment;
import com.wecode.letstalk.activity.authentication.interfaces.TabFragmentListener;
import com.wecode.letstalk.activity.authentication.signUp.SignUpFragmentOne;
import com.wecode.letstalk.activity.authentication.signUp.SignUpFragmentTwo;
import com.wecode.letstalk.configuration.Config;

public class AuthenticationPagerAdapter extends FragmentStatePagerAdapter {

    private static final int PAGE_COUNT = 2;

    private static final String[] TABS = new String[]{"Sign In", "Sign Up"};

    private Fragment fragmentSignUp;

    private FragmentManager fragmentManager;

    public AuthenticationPagerAdapter(FragmentManager fragmentManager) {
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
                return new SignInFragment().newInstance();

            case 1:
                if (fragmentSignUp == null) {
                    fragmentSignUp = SignUpFragmentOne.newInstance(new TabFragmentListener() {
                        public void onSwitchToNextFragment() {
                            Bundle args = new Bundle();
                            args.putInt(Config.USER_BIRTHYEAR, ((SignUpFragmentOne) fragmentSignUp).mYear);
                            args.putString(Config.USER_GENDER, ((SignUpFragmentOne) fragmentSignUp).mGender);
                            getFragmentManager().beginTransaction().remove(fragmentSignUp).commit();
                            fragmentSignUp = SignUpFragmentTwo.newInstance();
                            fragmentSignUp.setArguments(args);
                            notifyDataSetChanged();
                        }
                    });
                }

                return getFragmentSignUp();
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
    public int getItemPosition(Object object) {
        if (object instanceof SignUpFragmentOne && this.getFragmentSignUp() instanceof SignUpFragmentTwo) {
            return POSITION_NONE;
        }

        return POSITION_UNCHANGED;
    }
}
