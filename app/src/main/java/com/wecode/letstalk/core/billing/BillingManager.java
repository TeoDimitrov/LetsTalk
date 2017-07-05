package com.wecode.letstalk.core.billing;

import android.app.PendingIntent;
import android.os.Bundle;
import android.os.RemoteException;

import com.android.vending.billing.IInAppBillingService;
import com.wecode.letstalk.domain.product.Payable;
import com.wecode.letstalk.domain.product.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class BillingManager implements Billing {

    private IInAppBillingService mService;

    private String packageName;

    public BillingManager(IInAppBillingService mService, String packageName) {
        this.mService = mService;
        this.packageName = packageName;
    }

    public List<Payable> getAvailableForPurchaseItems() throws RemoteException, JSONException {
        List<Payable> products = new ArrayList<>();
        ArrayList<String> skuList = new ArrayList<> ();
        skuList.add(CHAT);
        skuList.add(TALK);
        Bundle querySkus = new Bundle();
        querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
        Bundle skuDetails = mService.getSkuDetails(3,
                packageName, "inapp", querySkus);
        int response = skuDetails.getInt("RESPONSE_CODE");
        if (response == 0) {
            ArrayList<String> responseList
                    = skuDetails.getStringArrayList("DETAILS_LIST");

            for (String thisResponse : responseList) {
                JSONObject object = new JSONObject(thisResponse);
                String sku = object.getString("productId");
                String title = object.getString("title");
                //Remove (LetsTalk) suffix
                title = title.substring(0, title.length() - 11);
                String price = object.getString("price");
                Payable payable = new Product(sku, title, price);
                products.add(payable);
            }
        }

        return products;
    }

    @Override
    public PendingIntent purchasePrepaid(String sku) throws RemoteException {
        Bundle buyIntentBundle = mService.getBuyIntent(3, packageName,
                sku, "inapp", null);

        PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
        return pendingIntent;
    }

    @Override
    public void consumePrepaid(final String token) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    mService.consumePurchase(3, packageName, token);
                } catch (RemoteException ignored) {

                }
            }
        };

        thread.start();
    }
}
