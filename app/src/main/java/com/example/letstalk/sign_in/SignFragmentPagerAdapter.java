package com.example.letstalk.sign_in;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.letstalk.sign_in.interfaces.TabFragmentListener;


/**
 * Created by teodo on 12/10/2016.
 */
public class SignFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private static final int PAGE_COUNT = 2;

    private static final String[] TABS = new String[]{"Sign Up", "Sign In"};

    private Fragment fragmentSignIn;

    private FragmentManager fragmentManager;

    public SignFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        this.setFragmentManager(fragmentManager);
    }

    private Fragment getFragmentSignIn() {
        return this.fragmentSignIn;
    }

    private void setFragmentSignIn(Fragment fragmentSignIn) {
        this.fragmentSignIn = fragmentSignIn;
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
                if (fragmentSignIn == null) {
                    fragmentSignIn = SignUpFragmentOne.newInstance(new TabFragmentListener() {
                        public void onSwitchToNextFragment() {
                            Bundle args = new Bundle();
                            args.putInt("birthYear", ((SignUpFragmentOne)fragmentSignIn).year);
                            args.putString("gender", ((SignUpFragmentOne)fragmentSignIn).gender);
                            getFragmentManager().beginTransaction().remove(fragmentSignIn).commit();
                            fragmentSignIn = SignUpFragmentTwo.newInstance();
                            fragmentSignIn.setArguments(args);
                            notifyDataSetChanged();
                        }
                    });
                }

                return getFragmentSignIn();
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
        if (object instanceof SignUpFragmentOne && this.getFragmentSignIn() instanceof SignUpFragmentTwo) {
            return POSITION_NONE;
        }
        return POSITION_UNCHANGED;
    }
}
