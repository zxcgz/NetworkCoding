package com.zxc.network_coding.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zxc.network_coding.pool.MessagePool;
import com.zxc.network_coding.utils.DataUtils;
import com.zxc.network_coding.utils.NetworkUtils;

import java.lang.reflect.Type;
import java.util.Map;

import androidx.annotation.Nullable;
import okhttp3.WebSocket;


/**
 * 维持与服务器交互的Service
 */
public class NetworkService extends Service {

    private WebSocket mWebSocket = null;
    public static String TAG = "NetworkService";
    private MyBinder myBinder = null ;
    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate") ;
        mWebSocket = NetworkUtils.connectServer() ;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mWebSocket.close(DataUtils.SUCCESS_CODE, DataUtils.SUCCESS_REASON);
        mWebSocket = null;
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind") ;
        return new MyBinder();
    }

    private class MyBinder extends Binder implements INetworkServiceBinder {

        @Override
        public boolean reconnect() {
            //TODO
            return false;
        }

        @Override
        public boolean isConnect() {
            //TODO
            return false;
        }

        @Override
        public void close() {
            mWebSocket.close(DataUtils.SUCCESS_CODE, DataUtils.SUCCESS_REASON);
        }

        @Override
        public void sendMessage(String msg) {
            mWebSocket.send(msg);
        }

        @Override
        public Map<String, String> getIpSet() {
            //发送消息，如果放松失败则重试，至多重试20次
            boolean getIpSet = false;
            int num = 20;
            while (num-- != 0 && !getIpSet) {
                getIpSet = mWebSocket.send(DataUtils.MSG_IPSET);
            }
            try {
                //从消息池中获取消息
                while (getIpSet) {
                    MessagePool pool = MessagePool.getInstance();
                    if (pool.isExist(DataUtils.TAG_IPSET)) {
                        String s = pool.get(DataUtils.TAG_IPSET);
                        //使用Gson解析数据
                        Gson gson = new Gson();
                        Type type = new TypeToken<Map<String, String>>() {
                        }.getType();
                        return gson.fromJson(s, type);
                    }
                    //睡眠0.1秒再获取消息
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d(TAG, e.getMessage());
            } finally {
                return null;
            }
        }
    }

}
