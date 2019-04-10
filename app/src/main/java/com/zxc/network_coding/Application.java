package com.zxc.network_coding;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;
import com.zxc.network_coding.broadcast.NetworkStatus;
import com.zxc.network_coding.service.NetworkService;
import com.zxc.network_coding.service.NetworkServiceConnection;

public class Application extends android.app.Application {
    private static String TAG = "Application";
    private NetworkStatus mNetworkStatusReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
        /**
         * 注册广播接收者
         */
        mNetworkStatusReceiver = new NetworkStatus();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkStatusReceiver, intentFilter);
        /**
         * 开启服务
         */
        Log.d(TAG, "bindService");
        Intent intent = new Intent(this, NetworkService.class);
        bindService(intent, NetworkServiceConnection.getInstance(), BIND_AUTO_CREATE);

    }

    @Override
    public void onTerminate() {
        unregisterReceiver(mNetworkStatusReceiver);
        super.onTerminate();
    }
}
