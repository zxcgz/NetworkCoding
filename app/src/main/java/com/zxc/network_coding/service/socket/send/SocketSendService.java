package com.zxc.network_coding.service.socket.send;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class SocketSendService extends Service {
    private MyBinder myBinder ;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        myBinder = new MyBinder() ;
        return myBinder;
    }

    private class MyBinder extends Binder implements ISocketSendServiceBinder{

    }
}
