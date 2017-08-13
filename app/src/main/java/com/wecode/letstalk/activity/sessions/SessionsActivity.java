package com.wecode.letstalk.activity.sessions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wecode.letstalk.R;
import com.wecode.letstalk.activity.authentication.AuthenticationActivity;
import com.wecode.letstalk.activity.sessions.account.AccountActivity;
import com.wecode.letstalk.activity.sessions.schedule.ScheduleActivity;
import com.wecode.letstalk.configuration.Config;
import com.wecode.letstalk.domain.roles.AdvisorRole;
import com.wecode.letstalk.domain.schedule.Schedule;
import com.wecode.letstalk.domain.user.User;
import com.wecode.letstalk.utils.FCMUtil;
import com.wecode.letstalk.utils.FirebaseUtils;

public class SessionsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewGroup mChatContainer;

    private SessionsFragmentPageAdapter mSessionsFragmentPagerAdapter;

    private TabLayout mSessionTabLayout;

    private ViewPager mSessionsViewPager;

    private User mCurrentUser;

    private DatabaseReference mDatabaseReference;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_session_drawer);
        this.receiveAuthenticatedUser(savedInstanceState);
        this.prepareViews();
        this.prepareDrawer();
        this.prepareFirebase();
    }

    private void prepareViews() {
        this.mSessionsFragmentPagerAdapter = new SessionsFragmentPageAdapter(getSupportFragmentManager());
        this.mSessionsViewPager = ((ViewPager) findViewById(R.id.viewPagerSessions));
        this.mSessionsViewPager.setAdapter(this.mSessionsFragmentPagerAdapter);
        this.mSessionTabLayout = ((TabLayout) findViewById(R.id.tabLayoutSessions));
        this.mSessionTabLayout.setupWithViewPager(this.mSessionsViewPager);
        this.mChatContainer = ((ViewGroup) findViewById(R.id.fragmentContainer));
    }

    private void prepareDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_session_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_sessions);
        TextView drawerEmailTextView = (TextView) headerView.findViewById(R.id.drawer_email);
        //Display email
        if (this.mCurrentUser != null) {
            String email = this.mCurrentUser.getEmail();
            drawerEmailTextView.setText(email);


            Menu menu = navigationView.getMenu();
            if (this.mCurrentUser.getRole().getName().equals("AdvisorRole")) {
                menu.findItem(R.id.nav_become_advisor).setVisible(false);
            } else {
                menu.findItem(R.id.nav_schedule).setVisible(false);
            }
        }
    }

    private void receiveAuthenticatedUser(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            this.mCurrentUser = extras.getParcelable(Config.USER_AUTHOR_EXTRA);
        }

        //If the user is not received go back to the authentication window
        if (this.mCurrentUser == null) {
            Intent authenticationIntent = new Intent(this, AuthenticationActivity.class);
            startActivity(authenticationIntent);
            finish();
        }
    }

    private void prepareFirebase() {
        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Config.CHILD_USERS);
        this.mAuth = FirebaseAuth.getInstance();
        this.mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.mAuth.addAuthStateListener(this.mListener);
        FCMUtil.subscribe(this.mCurrentUser);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.mAuth != null) {
            this.mAuth.removeAuthStateListener(this.mListener);
        }
    }

    private void becomeAdvisor() {
        this.mCurrentUser.setRole(new AdvisorRole());
        this.mCurrentUser.setSchedule(new Schedule());
        String userPath = FirebaseUtils.clearUserName(this.mCurrentUser.getEmail());
        this.mDatabaseReference.child(userPath).setValue(this.mCurrentUser);
        Toast.makeText(this, Config.ADVISOR_ACTIVATION, Toast.LENGTH_LONG).show();
    }

    public User getmCurrentUser() {
        return this.mCurrentUser;
    }

    private void signOut() {
        this.mAuth.signOut();
        LoginManager.getInstance().logOut();
        Intent authenticationActivityIntent = new Intent(this, AuthenticationActivity.class);
        startActivity(authenticationActivityIntent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_become_advisor:
                this.becomeAdvisor();
                break;
            case R.id.nav_schedule:
                this.goToSchedule();
                break;
            case R.id.nav_account:
                this.goToAccount();
                break;
            case R.id.nav_signout:
                FCMUtil.unsubscribe(this.mCurrentUser);
                this.signOut();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_session_drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToSchedule() {
        Intent scheduleIntent = new Intent(this, ScheduleActivity.class);
        scheduleIntent.putExtra(Config.USER_AUTHOR_EXTRA, this.mCurrentUser);
        startActivityForResult(scheduleIntent, Config.REQUEST_RETURN_SCHEDULE);
    }

    public void goToAccount() {
        Intent scheduleIntent = new Intent(this, AccountActivity.class);
        scheduleIntent.putExtra(Config.USER_AUTHOR_EXTRA, this.mCurrentUser);
        startActivityForResult(scheduleIntent, Config.REQUEST_RETURN_ACCOUNT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Config.REQUEST_RETURN_SCHEDULE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    this.mCurrentUser = extras.getParcelable(Config.USER_AUTHOR_EXTRA);
                }

                break;

            case Config.REQUEST_RETURN_ACCOUNT:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    this.mCurrentUser = extras.getParcelable(Config.USER_AUTHOR_EXTRA);
                }

                break;
        }
    }
}