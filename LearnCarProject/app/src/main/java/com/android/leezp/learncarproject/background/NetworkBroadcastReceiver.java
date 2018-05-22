package com.android.leezp.learncarproject.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.leezp.learncarproject.interfaces.NetAvailableInterface;

public class NetworkBroadcastReceiver extends BroadcastReceiver {
    private NetAvailableInterface netAvailable;

    public NetworkBroadcastReceiver(NetAvailableInterface netAvailable) {
        this.netAvailable = netAvailable;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            netAvailable.netAvailable();
        }
    }
}
