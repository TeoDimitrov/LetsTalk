package com.wecode.letstalk.core.billing;

import android.app.PendingIntent;
import android.os.RemoteException;

import com.wecode.letstalk.domain.product.Payable;

import org.json.JSONException;

import java.util.List;

public interface Billing {

    String CHAT = "k41ch47_1thb.buy";

    String TALK = "k41t4lk_1thb.buy";

    List<Payable> getAvailableForPurchaseItems() throws RemoteException, JSONException;

    PendingIntent purchasePrepaid(String sku) throws RemoteException;

    void consumePrepaid(String token);
}
