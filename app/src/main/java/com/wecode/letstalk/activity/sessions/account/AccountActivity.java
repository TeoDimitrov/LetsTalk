package com.wecode.letstalk.activity.sessions.account;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.wecode.letstalk.R;
import com.wecode.letstalk.configuration.Config;
import com.wecode.letstalk.core.billing.Billing;
import com.wecode.letstalk.core.billing.BillingManager;
import com.wecode.letstalk.domain.product.Payable;
import com.wecode.letstalk.domain.user.User;
import com.wecode.letstalk.repository.UserRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.view.View.OnClickListener;
import static com.wecode.letstalk.core.billing.Billing.CHAT;
import static com.wecode.letstalk.core.billing.Billing.TALK;

public class AccountActivity extends AppCompatActivity implements OnClickListener {

    TextView mChatsTextView;

    TextView mPaidChatsTextView;

    TextView mTalksTextView;

    TextView mPaidTalksTextView;

    private Button mBtnBuyChat;

    private Button mBtnBuyTalk;

    private User mCurrentUser;

    private UserRepository mUserRepository;

    private IInAppBillingService mService;

    private Billing mBilling;

    private ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
            //Create Billing Manager
            mBilling = new BillingManager(mService, getPackageName());
            getProducts();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        this.receiveAuthenticatedUser(savedInstanceState);
        this.prepareViews();
        this.updateUserStats();
        this.prepareBilling();
        this.prepareRepositories();
    }

    private void getProducts() {
        try {
            List<Payable> products = mBilling.getAvailableForPurchaseItems();
            for (Payable product : products) {
                String purchaseText = String.format("Buy %s - %s", product.getTitle(), product.getPrice());
                switch (product.getProductId()) {
                    case CHAT:
                        mBtnBuyChat.setText(purchaseText);
                        break;
                    case TALK:
                        mBtnBuyTalk.setText(purchaseText);
                        break;
                }
            }
        } catch (RemoteException | JSONException ignored) {

        }
    }

    private void prepareRepositories(){
        mUserRepository = new UserRepository(Config.CHILD_USERS);
    }

    private void prepareBilling() {
        //Bind Intents
        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
    }

    private void prepareViews() {
        mChatsTextView = (TextView) findViewById(R.id.chats_number);
        mPaidChatsTextView = (TextView) findViewById(R.id.paid_chats_number);

        mTalksTextView = (TextView) findViewById(R.id.talks_number);
        mPaidTalksTextView = (TextView) findViewById(R.id.paid_talks_number);

        mBtnBuyChat = (Button) findViewById(R.id.buy_chat);
        mBtnBuyChat.setOnClickListener(this);
        mBtnBuyTalk = (Button) findViewById(R.id.buy_talk);
        mBtnBuyTalk.setOnClickListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void updateUserStats(){
        mChatsTextView.setText(Integer.toString(mCurrentUser.getChats()));
        mPaidChatsTextView.setText(Integer.toString(mCurrentUser.getPaidChats()));
        mTalksTextView.setText(Integer.toString(mCurrentUser.getTalks()));
        mPaidTalksTextView.setText(Integer.toString(mCurrentUser.getPaidTalks()));
    }

    private void receiveAuthenticatedUser(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            this.mCurrentUser = extras.getParcelable(Config.USER_AUTHOR_EXTRA);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                returnToSessionActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void returnToSessionActivity() {
        setResult(Activity.RESULT_OK,
                new Intent().putExtra(Config.USER_AUTHOR_EXTRA, this.mCurrentUser));
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buy_chat:
                purchaseItem(CHAT);
                break;
            case R.id.buy_talk:
                purchaseItem(TALK);
                break;
        }
    }

    private void purchaseItem(String sku){
        try {
            PendingIntent pendingIntent = mBilling.purchasePrepaid(sku);
            startIntentSenderForResult(pendingIntent.getIntentSender(),
                    1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                    Integer.valueOf(0));
        } catch (RemoteException | IntentSender.SendIntentException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            if (resultCode == RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    String sku = jo.getString("productId");
                    String purchaseToken = jo.getString("purchaseToken");
                    mBilling.consumePrepaid(purchaseToken);
                    switch (sku){
                        case Billing.CHAT:
                            mCurrentUser.addPaidChat(Billing.CHAT_NUMBER);
                            mUserRepository.increaseChats(mCurrentUser.getEmail(), mCurrentUser.getPaidChats());
                            break;
                        case Billing.TALK:
                            mCurrentUser.addPaidTalk(Billing.TALK_NUMBER);
                            mUserRepository.increaseTalks(mCurrentUser.getEmail(), mCurrentUser.getPaidTalks());
                            break;
                    }

                    this.updateUserStats();
                }
                catch (JSONException e) {
                    Toast.makeText(this, R.string.purchase_unsuccessful, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.purchase_unsuccessful, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
