package com.zxc.network_coding.service.network;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class NetworkServiceConnection implements ServiceConnection{

    private static final String TAG = "NetworkServiceConnect";
    private static NetworkServiceConnection networkServiceConnection = null;

    private INetworkServiceBinder mBinder = null;

    public static NetworkServiceConnection getInstance() {
        if (networkServiceConnection == null) {
            networkServiceConnection = new NetworkServiceConnection();
        }
        return networkServiceConnection;
    }

    private NetworkServiceConnection() {
    }


    public INetworkServiceBinder getBinder() {
        return mBinder ;

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mBinder = (INetworkServiceBinder) service;
        Log.d(TAG, "onServiceConnected");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
