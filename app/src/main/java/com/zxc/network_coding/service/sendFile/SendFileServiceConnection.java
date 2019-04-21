package com.zxc.network_coding.service.sendFile;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class SendFileServiceConnection implements ServiceConnection {

    private ISendFileServiceBinder mBinder = null ;

    private static SendFileServiceConnection mSendFileServiceConnection = null ;

    public static SendFileServiceConnection getInstance(){
        if (mSendFileServiceConnection == null){
            mSendFileServiceConnection = new SendFileServiceConnection() ;
        }
        return mSendFileServiceConnection ;
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mBinder = (ISendFileServiceBinder) service;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
