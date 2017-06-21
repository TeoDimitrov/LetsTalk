package com.wecode.letstalk.activity.authentication.signIn;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wecode.letstalk.R;
import com.wecode.letstalk.activity.sessions.SessionsActivity;
import com.wecode.letstalk.configuration.Config;
import com.wecode.letstalk.domain.user.User;
import com.wecode.letstalk.utils.FirebaseUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static android.view.View.OnClickListener;
import static android.view.View.OnTouchListener;

public class SignInFragment extends Fragment implements OnClickListener, OnTouchListener {

    private RelativeLayout mRelativeLayout;

    private EditText mEmailEditText;

    private EditText mPasswordEditText;

    private Button mNextButton;

    private Button mFacebookLoginButton;

    private LoginButton mFacebookHiddenLoginButton;

    private CallbackManager mCallbackManager;

    private ProgressDialog mProgressDialog;

    private String mUsername;

    private String mPassword;

    private Intent sessionsActivityIntent;

    private DatabaseReference mDatabaseReference;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());
        this.prepareLayout(inflater, container);
        this.prepareViews();
        this.initializeProgressDialog();
        this.prepareListeners();
        this.prepareIntents();
        this.prepareFirebase();
        this.prepareFacebookAuthentication();

        return this.mRelativeLayout;
    }

    private void prepareLayout(LayoutInflater inflater, ViewGroup container) {
        this.mRelativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    private void prepareViews() {
        this.mEmailEditText = (EditText) this.mRelativeLayout.findViewById(R.id.email_sing_in);
        this.mPasswordEditText = (EditText) this.mRelativeLayout.findViewById(R.id.password_sign_in);
        this.mNextButton = (Button) this.mRelativeLayout.findViewById(R.id.button_next_sign_in);
        this.mFacebookLoginButton = (Button) this.mRelativeLayout.findViewById(R.id.btn_fb_sign_in);
        this.mFacebookHiddenLoginButton = (LoginButton) this.mRelativeLayout.findViewById(R.id.facebook_button_sign_in);
    }

    public void initializeProgressDialog() {
        this.mProgressDialog = new ProgressDialog(getActivity());
        this.mProgressDialog.setTitle("Sign In");
        this.mProgressDialog.setMessage("Signing User...");
        this.mProgressDialog.setProgress(0);
        this.mProgressDialog.setMax(20);
    }

    private void prepareListeners() {
        this.mNextButton.setOnClickListener(this);
        this.mFacebookLoginButton.setOnClickListener(this);
        this.mFacebookHiddenLoginButton.setOnClickListener(this);
        this.mRelativeLayout.setOnTouchListener(this);
    }

    private void prepareIntents() {
        this.sessionsActivityIntent = new Intent(getActivity(), SessionsActivity.class);
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

    private void prepareFacebookAuthentication() {
        this.mCallbackManager = CallbackManager.Factory.create();
        this.mFacebookHiddenLoginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday"));
        this.mFacebookHiddenLoginButton.setFragment(this);
        this.mFacebookHiddenLoginButton.registerCallback(this.mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                processLoginResults(loginResult);
            }

            @Override
            public void onCancel() {
                hideProgressDialog();
            }

            @Override
            public void onError(FacebookException error) {
                hideProgressDialog();
                Toast.makeText(getContext(), "Unsuccessful Facebook Login", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processLoginResults(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            AccessToken facebookAccessToken = loginResult.getAccessToken();
                            String email = response.getJSONObject().getString("email");
                            String gender = response.getJSONObject().getString("gender");
                            String birthDateStringValue = response.getJSONObject().getString("birthday");
                            int birthDate = 0;
                            if (birthDateStringValue != null) {
                                birthDate = Integer.parseInt(birthDateStringValue.substring(birthDateStringValue.length() - 4));
                            }

                            loginAndRegisterFacebookUser(email, gender, birthDate, facebookAccessToken);
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Cannot Process Facebook Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, email, gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void loginAndRegisterFacebookUser(final String username, final String gender, final int birthDate, final AccessToken facebookAccessToken) {
        final String userPath = FirebaseUtils.clearUserName(username);
        Query query = this.mDatabaseReference.orderByChild(Config.CHILD_USERS_EMAIL).equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(userPath).getValue(User.class);
                if (user == null) {
                    user = new User(birthDate, gender, username);
                    registerFirebaseUser(user);
                }

                loginFacebookUser(facebookAccessToken, user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
            }
        });
    }

    private void registerFirebaseUser(final User user) {
        String username = user.getEmail();
        String userPath = FirebaseUtils.clearUserName(username);
        this.mDatabaseReference.child(userPath).setValue(user);
    }

    private void loginFacebookUser(final AccessToken facebookAccessToken, final User user) {
        AuthCredential facebookCredential = FacebookAuthProvider.getCredential(facebookAccessToken.getToken());
        mAuth.signInWithCredential(facebookCredential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(getActivity(), SessionsActivity.class);
                    intent.putExtra(Config.USER_AUTHOR_EXTRA, user);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }

                if (!task.isSuccessful()) {
                    Toast.makeText(getContext(), "Unsuccessful Facebook Login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fbLogin() {
        this.showProgressDialog();
        this.mFacebookHiddenLoginButton.performClick();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.mAuth.addAuthStateListener(this.mListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (this.mAuth != null) {
            this.mAuth.removeAuthStateListener(this.mListener);
        }
    }

    public static Fragment newInstance() {
        SignInFragment signInFragment = new SignInFragment();
        return signInFragment;
    }

    private void loginWithEmailAndPassword(final String email, final String password) {
        final String userPath = FirebaseUtils.clearUserName(email);
        Query query = this.mDatabaseReference.orderByChild(Config.CHILD_USERS_EMAIL).equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showProgressDialog();
                final User user = dataSnapshot.child(userPath).getValue(User.class);
                sessionsActivityIntent.putExtra(Config.USER_AUTHOR_EXTRA, user);
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    getActivity().startActivity(sessionsActivityIntent);
                                    getActivity().finish();
                                }

                                if (!task.isSuccessful()) {
                                    hideProgressDialog();
                                    Toast.makeText(getActivity(), Config.ERROR_NO_SUCH_USER, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void emailLogin() {
        if (!validateForm()) {
            return;
        }

        this.mUsername = this.mEmailEditText.getText().toString();
        this.mPassword = this.mPasswordEditText.getText().toString();
        this.loginWithEmailAndPassword(this.mUsername, this.mPassword);
    }

    private boolean validateEmail() {
        String email = this.mEmailEditText.getText().toString();
        boolean isEmailValid = true;
        if (TextUtils.isEmpty(email)) {
            this.mEmailEditText.setError(Config.ERROR_EMAIL_IS_REQUIRED);
            isEmailValid = false;
        } else {
            this.mEmailEditText.setError(null);
        }

        return isEmailValid;
    }

    private boolean validatePassword() {
        String password = this.mPasswordEditText.getText().toString();
        boolean isPasswordValid = true;
        if (TextUtils.isEmpty(password)) {
            this.mPasswordEditText.setError(Config.ERROR_PASSWORD_IS_REQUIRED);
            isPasswordValid = false;
        } else {
            this.mPasswordEditText.setError(null);
        }

        return isPasswordValid;
    }

    private boolean validateForm() {
        return validateEmail() && validatePassword();
    }

    private void hideProgressDialog() {
        this.mProgressDialog.dismiss();
    }

    private void showProgressDialog() {
        this.mProgressDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_next_sign_in:
                this.emailLogin();
                break;
            case R.id.btn_fb_sign_in:
                this.fbLogin();
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.fragment_sign_up_fragment_two:
                this.hideSoftKeyboard(getActivity());
                break;
        }

        return false;
    }

    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus()
                        .getWindowToken(), 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.hideProgressDialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
