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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.wecode.letstalk.R;
import com.wecode.letstalk.activity.authentication.AuthenticationActivity;
import com.wecode.letstalk.configuration.Config;
import com.wecode.letstalk.domain.roles.AdvisorRole;
import com.wecode.letstalk.domain.user.User;
import com.wecode.letstalk.repository.AdvisorRepository;
import com.wecode.letstalk.repository.UserRepository;

public class SessionsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewGroup mChatContainer;

    private SessionsFragmentPagerAdapter mSessionsFragmentPagerAdapter;

    private TabLayout mSessionTabLayout;

    private ViewPager mSessionsViewPager;

    private UserRepository mUserRepository;

    private User mCurrentUser;

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
        this.mUserRepository = new UserRepository(Config.CHILD_USERS);
    }

    private void prepareViews() {
        this.mSessionsFragmentPagerAdapter = new SessionsFragmentPagerAdapter(getSupportFragmentManager());
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
        String email = this.mCurrentUser.getEmail();
        drawerEmailTextView.setText(email);
    }

    private void receiveAuthenticatedUser(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            this.mCurrentUser = extras.getParcelable(Config.USER_EXTRA);
        }

        //If the user is not received go back to the authentication window
        if (this.mCurrentUser == null) {
            Intent authenticationIntent = new Intent(this, AuthenticationActivity.class);
            startActivity(authenticationIntent);
            finish();
        }
    }

    private void prepareFirebase() {
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.mAuth != null) {
            this.mAuth.removeAuthStateListener(this.mListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void becomeAdvisor() {
        this.mCurrentUser.setRole(new AdvisorRole());
        this.mUserRepository.updateUser(this.mCurrentUser);
        AdvisorRepository advisorRepository = new AdvisorRepository();
        advisorRepository.saveAdvisorName(this.mCurrentUser.getEmail());
        Toast.makeText(this, "Reopen the app to become advisor", Toast.LENGTH_LONG).show();
    }

    public User getmCurrentUser() {
        return this.mCurrentUser;
    }

    public String getCurrentUserPath() {
        String userPath = this.mUserRepository.clearUserName(this.mCurrentUser.getEmail());
        return userPath;
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
            case R.id.nav_signout:
                this.signOut();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_session_drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}