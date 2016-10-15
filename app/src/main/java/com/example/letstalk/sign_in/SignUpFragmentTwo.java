package com.example.letstalk.sign_in;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.letstalk.Config;
import com.example.letstalk.R;
import com.example.letstalk.SessionsActivity;
import com.example.letstalk.users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragmentTwo extends Fragment implements OnClickListener{

    private RelativeLayout relativeLayout;

    private EditText etUsername;

    private EditText etPassword;

    private EditText etConfirmPassword;

    private Button btnNext;

    private ProgressDialog pdCreateUser;

    private String usernameValue;

    private String passwordValue;

    private String confirmPasswordValue;

    private String gender;

    private Integer birthYear;

    private User currentUser;

    private Intent sessionActivityIntent;

    private FirebaseAuth mAuth;

    private DatabaseReference databaseReference;

    private RelativeLayout getRelativeLayout() {
        return this.relativeLayout;
    }

    private void setRelativeLayout(RelativeLayout relativeLayout) {
        this.relativeLayout = relativeLayout;
    }

    private EditText getEtUsername() {
        return this.etUsername;
    }

    private void setEtUsername(EditText etUsername) {
        this.etUsername = etUsername;
    }

    private EditText getEtPassword() {
        return this.etPassword;
    }

    private void setEtPassword(EditText etPassword) {
        this.etPassword = etPassword;
    }

    private EditText getEtConfirmPassword() {
        return this.etConfirmPassword;
    }

    private void setEtConfirmPassword(EditText etConfirmPassword) {
        this.etConfirmPassword = etConfirmPassword;
    }

    private Button getBtnNext() {
        return this.btnNext;
    }

    private void setBtnNext(Button btnNext) {
        this.btnNext = btnNext;
    }

    private ProgressDialog getPdCreateUser() {
        return this.pdCreateUser;
    }

    private void setPdLogin() {
        this.pdCreateUser = new ProgressDialog(getActivity());
        this.getPdCreateUser().setTitle("Authentication");
        this.getPdCreateUser().setMessage("Creating User...");
        this.getPdCreateUser().setProgress(0);
        this.getPdCreateUser().setMax(20);
    }

    private String getUsernameValue() {
        return this.usernameValue;
    }

    private void setUsernameValue(String usernameValue) {
        this.usernameValue = usernameValue;
    }

    private String getPasswordValue() {
        return this.passwordValue;
    }

    private void setPasswordValue(String passwordValue) {
        this.passwordValue = passwordValue;
    }

    private String getConfirmPasswordValue() {
        return this.confirmPasswordValue;
    }

    private void setConfirmPasswordValue(String confirmPasswordValue) {
        this.confirmPasswordValue = confirmPasswordValue;
    }

    private String getGender() {
        return this.gender;
    }

    private void setGender(String gender) {
        this.gender = gender;
    }

    private Integer getBirthYear() {
        return this.birthYear;
    }

    private void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    private User getCurrentUser() {
        return this.currentUser;
    }

    private void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    private Intent getSessionActivityIntent() {
        return this.sessionActivityIntent;
    }

    private void setSessionActivityIntent(Intent sessionActivityIntent) {
        this.sessionActivityIntent = sessionActivityIntent;
    }

    private FirebaseAuth getmAuth() {
        return this.mAuth;
    }

    private void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    private DatabaseReference getDatabaseReference() {
        return this.databaseReference;
    }

    private void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.setRelativeLayout((RelativeLayout) inflater.inflate(R.layout.fragment_sign_up_fragment_two,container, false));
        this.setEtUsername((EditText) this.relativeLayout.findViewById(R.id.username));
        this.setEtPassword((EditText) this.relativeLayout.findViewById(R.id.password));
        this.setEtConfirmPassword((EditText) this.relativeLayout.findViewById(R.id.confirm_password));
        this.setBtnNext((Button) this.relativeLayout.findViewById(R.id.button_next2));
        this.getBtnNext().setOnClickListener(this);
        this.setPdLogin();
        this.setSessionActivityIntent(new Intent(getActivity(), SessionsActivity.class));

        Bundle args = getArguments();
        if (args != null) {
            this.setGender(args.getString("gender"));
            this.setBirthYear(args.getInt("birthYear"));
        }

        this.setDatabaseReference(FirebaseDatabase.getInstance().getReference().child(Config.CHILD_USERS));
        this.setmAuth(FirebaseAuth.getInstance());

        return this.getRelativeLayout();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_next2:
                clickNextButton();
                break;
        }
    }

    private void clickNextButton() {
        this.setUsernameValue(this.etUsername.getText().toString());
        this.setPasswordValue(this.etPassword.getText().toString());
        this.setConfirmPasswordValue(this.etConfirmPassword.getText().toString());

        User newUser = new User(this.getBirthYear(), this.getGender(), this.getUsernameValue(), this.getPasswordValue());
        this.setCurrentUser(newUser);

        createAccount(newUser);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = this.getUsernameValue();
        if (TextUtils.isEmpty(email)) {
            this.getEtUsername().setError("Required.");
            valid = false;
        } else {
            this.getEtUsername().setError(null);
        }

        String password = this.getPasswordValue();
        if (TextUtils.isEmpty(password)) {
            this.getEtPassword().setError("Required.");
            valid = false;
        } else {
            this.getEtPassword().setError(null);
        }

        return valid;
    }

    private void createAccount(final User user) {
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        String email = user.getUsername();
        String password = user.getPassword();
        this.getmAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            insertInDatabase(user);
                            startActivity(getSessionActivityIntent());
                            destroyActivity();
                        }


                        if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Login has failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                        hideProgressDialog();
                    }
                });
    }

    private void hideProgressDialog() {
        this.getPdCreateUser().dismiss();
    }

    private void showProgressDialog() {
        this.getPdCreateUser().show();
    }

    private void insertInDatabase(User user){
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("UserName", user.getUsername());
        userDetails.put("Password", "Not Stored");
        userDetails.put("Year", user.getBirthDate());
        userDetails.put("Gender", user.getGender());
        userDetails.put("Chats", user.getChats());
        userDetails.put("Talks", user.getTalks());
        userDetails.put("Role", user.getRole());

        String userKey = getDatabaseReference().push().getKey();
        this.getDatabaseReference().child(userKey).updateChildren(userDetails);
    }

    private void destroyActivity(){
        getActivity().finish();
    }

    public static SignUpFragmentTwo newInstance(){
        SignUpFragmentTwo signUpFragmentTwo = new SignUpFragmentTwo();
        return signUpFragmentTwo;
    }
}