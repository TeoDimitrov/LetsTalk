package com.wecode.letstalk.activity.authentication.signUp;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
import com.wecode.letstalk.firebase.FirebaseEmailAuthenticator;
import com.wecode.letstalk.repository.UserRepository;
import com.wecode.letstalk.utils.FirebaseUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.view.View.*;
import static com.wecode.letstalk.configuration.Config.VALID_EMAIL_ADDRESS_PATTERN;

public class SignUpFragmentTwo extends Fragment implements OnClickListener, OnTouchListener {

    private RelativeLayout mRelativeLayout;

    private EditText mEmailEditText;

    private EditText mPasswordEditText;

    private EditText mConfirmPasswordEditText;

    private Button mButtonNext;

    private ProgressDialog mCreateUserProgressDialog;

    private String mEmailValue;

    private String mPasswordValue;

    private String mConfirmPasswordValue;

    private String mGender;

    private Integer mBirthYear;

    private User mCurrentUser;

    private Intent mSessionActivityIntent;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabaseReference;

    private FirebaseAuth.AuthStateListener mListener;

    private FirebaseEmailAuthenticator firebaseEmailAuthenticator;

    private UserRepository userRepository;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.prepareLayout(inflater, container);
        this.prepareViews();
        this.initializeProgressDialogCreateUser();
        this.prepareListeners();
        this.prepareIntents();
        this.prepareFirebase();
        this.receiveBundles();

        return this.mRelativeLayout;
    }

    private void prepareLayout(LayoutInflater inflater, ViewGroup container) {
        this.mRelativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_sign_up_fragment_two, container, false);
    }

    private void prepareViews() {
        this.mEmailEditText = (EditText) this.mRelativeLayout.findViewById(R.id.email);
        this.mPasswordEditText = (EditText) this.mRelativeLayout.findViewById(R.id.password);
        this.mConfirmPasswordEditText = (EditText) this.mRelativeLayout.findViewById(R.id.confirm_password);
        this.mButtonNext = (Button) this.mRelativeLayout.findViewById(R.id.button_next2);
    }

    private void prepareListeners() {
        this.mButtonNext.setOnClickListener(this);
        this.mRelativeLayout.setOnTouchListener(this);
    }

    private void prepareIntents() {
        this.mSessionActivityIntent = new Intent(getActivity(), SessionsActivity.class);
    }

    private void prepareFirebase() {
        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Config.CHILD_USERS);
        this.mAuth = FirebaseAuth.getInstance();
        this.mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //The implementation currently is nor required
            }
        };
    }

    private void receiveBundles() {
        Bundle args = getArguments();
        if (args != null) {
            this.mGender = args.getString(Config.USER_GENDER);
            this.mBirthYear = args.getInt(Config.USER_BIRTHYEAR);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_next2:
                clickNextButton();
                break;
        }
    }

    private void clickNextButton() {
        this.mEmailValue = this.mEmailEditText.getText().toString();
        this.mPasswordValue = this.mPasswordEditText.getText().toString();
        this.mConfirmPasswordValue = this.mConfirmPasswordEditText.getText().toString();
        this.mCurrentUser = new User(this.mBirthYear, this.mGender, this.mEmailValue, this.mPasswordValue);
        this.mSessionActivityIntent.putExtra(Config.USER_AUTHOR_EXTRA, this.mCurrentUser);
        this.createUser(this.mCurrentUser);
    }

    private boolean validateEmail() {
        String email = this.mEmailEditText.getText().toString();
        boolean isEmailValid = true;
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile(VALID_EMAIL_ADDRESS_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);

        if (TextUtils.isEmpty(email)) {
            this.mEmailEditText.setError(Config.ERROR_EMAIL_IS_REQUIRED);
            isEmailValid = false;
        } else if (email.matches("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$")) {
            this.mEmailEditText.setError(Config.ERROR_EMAIL_SHOULD_NOT_CONTAIN);
            isEmailValid = false;
        } else if (!matcher.find()) {
            this.mEmailEditText.setError(Config.ERROR_EMAIL_NOT_VALID);
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
        } else if (password.length() < Config.MIN_PASSWORD_LENGTH) {
            this.mPasswordEditText.setError(Config.ERROR_PASSWORD_SHORT);
            isPasswordValid = false;
        } else {
            this.mPasswordEditText.setError(null);
        }

        String confirmPassword = this.mConfirmPasswordEditText.getText().toString();
        if (TextUtils.isEmpty(confirmPassword)) {
            this.mConfirmPasswordEditText.setError(Config.ERROR_CONFIRM_PASSWORD_IS_REQUIRED);
            isPasswordValid = false;
        } else if (!confirmPassword.equals(password)) {
            this.mConfirmPasswordEditText.setError(Config.ERROR_PASSWORDS_DOESNT_MATCH);
            isPasswordValid = false;
        } else {
            this.mConfirmPasswordEditText.setError(null);
        }

        return isPasswordValid;
    }

    private boolean validateForm() {
        return validateEmail() && validatePassword();
    }

    private void createUser(final User user) {
        if (!validateForm()) {
            return;
        }

        this.showProgressDialog();
        String email = user.getEmail();
        String password = user.getPassword();
        final String userPath = FirebaseUtils.clearUserName(email);
        Query query = this.mDatabaseReference.orderByChild(Config.CHILD_USERS_EMAIL).equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(userPath).getValue(User.class);
                if (user == null) {
                    user = new User();
                    user.setEmail(mEmailValue);
                    user.setGender(mGender);
                    user.setBirthDate(mBirthYear);
                    mDatabaseReference.child(userPath).setValue(user);
                }

                mSessionActivityIntent.putExtra(Config.USER_AUTHOR_EXTRA, user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        this.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getActivity().startActivity(mSessionActivityIntent);
                            getActivity().finish();
                        }

                        if (!task.isSuccessful()) {
                            hideProgressDialog();
                            Toast.makeText(getActivity(), Config.ERROR_EXISTING_USER, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static SignUpFragmentTwo newInstance() {
        SignUpFragmentTwo signUpFragmentTwo = new SignUpFragmentTwo();
        return signUpFragmentTwo;
    }

    private void initializeProgressDialogCreateUser() {
        this.mCreateUserProgressDialog = new ProgressDialog(getActivity());
        this.mCreateUserProgressDialog.setTitle(Config.MESSAGE_AUTHENTICATION);
        this.mCreateUserProgressDialog.setMessage(Config.MESSAGE_CREATING_USER);
        this.mCreateUserProgressDialog.setProgress(0);
        this.mCreateUserProgressDialog.setMax(20);
    }

    private void hideProgressDialog() {
        this.mCreateUserProgressDialog.dismiss();
    }

    private void showProgressDialog() {
        this.mCreateUserProgressDialog.show();
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
}