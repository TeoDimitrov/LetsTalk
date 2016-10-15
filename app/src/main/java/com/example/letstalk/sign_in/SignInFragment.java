package com.example.letstalk.sign_in;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.letstalk.Config;
import com.example.letstalk.R;
import com.example.letstalk.SessionsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by teodo on 14/10/2016.
 */
public class SignInFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout relativeLayout;

    private EditText etUsername;

    private EditText etPassword;

    private Button btnNext;

    private ProgressDialog pdSignUser;

    private String usernameValue;

    private String passwordValue;

    private Intent sessionActivityIntent;

    private DatabaseReference databaseReference;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mListener;

    public RelativeLayout getRelativeLayout() {
        return this.relativeLayout;
    }

    public void setRelativeLayout(RelativeLayout relativeLayout) {
        this.relativeLayout = relativeLayout;
    }


    public EditText getEtUsername() {
        return this.etUsername;
    }

    public void setEtUsername(EditText etUsername) {
        this.etUsername = etUsername;
    }

    public EditText getEtPassword() {
        return this.etPassword;
    }

    public void setEtPassword(EditText etPassword) {
        this.etPassword = etPassword;
    }

    public Button getBtnNext() {
        return this.btnNext;
    }

    public void setBtnNext(Button btnNext) {
        this.btnNext = btnNext;
    }

    public ProgressDialog getPdSignUser() {
        return this.pdSignUser;
    }

    public void setPdSignUser() {
            this.pdSignUser = new ProgressDialog(getActivity());
            this.getPdSignUser().setTitle("Sign In");
            this.getPdSignUser().setMessage("Signing User...");
            this.getPdSignUser().setProgress(0);
            this.getPdSignUser().setMax(20);
    }

    public String getUsernameValue() {
        return this.usernameValue;
    }

    public void setUsernameValue(String usernameValue) {
        this.usernameValue = usernameValue;
    }

    public String getPasswordValue() {
        return this.passwordValue;
    }

    public void setPasswordValue(String passwordValue) {
        this.passwordValue = passwordValue;
    }

    public Intent getSessionActivityIntent() {
        return this.sessionActivityIntent;
    }

    public void setSessionActivityIntent(Intent sessionActivityIntent) {
        this.sessionActivityIntent = sessionActivityIntent;
    }

    public DatabaseReference getDatabaseReference() {
        return this.databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public FirebaseAuth getmAuth() {
        return this.mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public FirebaseAuth.AuthStateListener getmListener() {
        return this.mListener;
    }

    public void setmListener(FirebaseAuth.AuthStateListener mListener) {
        this.mListener = mListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.setRelativeLayout((RelativeLayout) inflater.inflate(R.layout.fragment_sign_in,container, false));
        this.setEtUsername((EditText) this.getRelativeLayout().findViewById(R.id.username_sing_in));
        this.setEtPassword((EditText) this.getRelativeLayout().findViewById(R.id.password_sign_in));
        this.setBtnNext((Button) this.getRelativeLayout().findViewById(R.id.button_next_sign_in));
        this.getBtnNext().setOnClickListener(this);
        this.setPdSignUser();
        this.setSessionActivityIntent(new Intent(getActivity(), SessionsActivity.class));

        this.setDatabaseReference(FirebaseDatabase.getInstance().getReference().child(Config.CHILD_USERS));
        this.setmAuth(FirebaseAuth.getInstance());
        this.setmListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//                String userName = firebaseUser.getEmail();
//                Query usersQuery = getDatabaseReference().orderByChild("UserName").equalTo(userName).limitToFirst(1);
                insertLogs();
                if (firebaseUser != null) {
                    // User is signed in
                    FirebaseAuth.getInstance().signOut();
                    startActivity(getSessionActivityIntent());
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + firebaseUser.getUid());
                }
            }
        });

        return this.getRelativeLayout();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.getmAuth().addAuthStateListener(this.getmListener());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (this.getmListener() != null) {
            this.getmAuth().removeAuthStateListener(this.getmListener());
        }
    }

    public static Fragment newInstance() {
        SignInFragment signInFragment = new SignInFragment();
        return signInFragment;
    }

    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            Log.d("T", "signInWithEmail:onComplete:" + task.isSuccessful());
                            startActivity(getSessionActivityIntent());
                            destroyActivity();
                        }


                        if (!task.isSuccessful()) {
                            Log.w("T", "signInWithEmail:failed", task.getException());
                            Toast.makeText(getActivity(), "Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        this.hideProgressDialog();
    }

    private void signInClick(){
        this.showProgressDialog();
        this.setUsernameValue(this.getEtUsername().getText().toString());
        this.setPasswordValue(this.getEtPassword().getText().toString());
        this.signIn(this.getUsernameValue(), this.getPasswordValue());
    }

    private void hideProgressDialog() {
        this.getPdSignUser().dismiss();
    }

    private void showProgressDialog() {
        this.getPdSignUser().show();
    }

    private void destroyActivity(){
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_next_sign_in:
                this.signInClick();
                break;
        }
    }

    private void insertLogs() {
        LoginSQLiteHelper loginSQLiteHelper = new LoginSQLiteHelper(this.getContext());
        SQLiteDatabase db = loginSQLiteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LoginSQLiteHelper.COLUMN_NAME, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

        long newRowId = db.insert(LoginSQLiteHelper.TABLE_NAME, null, values);

        Log.d("SQL Lite", String.valueOf(newRowId));
    }
}
