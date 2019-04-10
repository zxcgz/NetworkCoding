package com.zxc.network_coding.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zxc.network_coding.service.INetworkServiceBinder;
import com.zxc.network_coding.service.NetworkService;
import com.zxc.network_coding.service.NetworkServiceConnection;
import com.zxc.network_coding.utils.DataUtils;
import com.zxc.network_coding.utils.NetworkUtils;
import com.zxc.network_coding.utils.ServiceUtils;


/**
 * 网络状态改变时的广播接收者
 */
public class NetworkStatus extends BroadcastReceiver {

    private static String TAG = "NetworkStatus";

    @Override
    public void onReceive(Context context, Intent intent) {
        int netWorkStates = NetworkUtils.getNetWorkStates(context);
        Log.d(TAG, "onReceive");
        boolean serviceRunning = ServiceUtils.isServiceRunning(context, NetworkService.class.getName());
        Log.d(TAG, serviceRunning ? "serviceRunning" : "serviceClose");
        INetworkServiceBinder binder = NetworkServiceConnection.getInstance().getBinder();
        if (serviceRunning && null != binder) {
            Log.d(TAG,"sendMessage") ;
            binder.sendMessage("test");
        }
        switch (netWorkStates) {
            case DataUtils.TYPE_MOBILE:
                //手机流量
                Log.d(TAG, "TYPE_MOBILE");
                break;
            case DataUtils.TYPE_NONE:
                //无网络
                Log.d(TAG, "TYPE_NONE");
                break;
            case DataUtils.TYPE_WIFI:
                //WIFI
                Log.d(TAG, "TYPE_WIFI");
                break;
        }
    }
}
