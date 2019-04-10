package com.zxc.network_coding.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * 网络相关的工具类
 */
public class NetworkUtils {

    private static final String TAG = "NetworkUtils";

    private static String mHeartBeat;
    private static WebSocket mWebSocket;
    private Timer mTimer = new Timer();
    private static OkHttpClient mOkHttpClient;
    private String mMessage;


    /**
     * 从服务器中获取到正在运行的程序的IP地址集合
     */


    /**
     * 根据IP集合构造合适的网络拓扑
     */

    /**
     * 连接服务器
     */
    public static WebSocket connectServer() {
        Log.d(TAG,"connectServer") ;
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(3000, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(3000, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(3000, TimeUnit.SECONDS)//设置连接超时时间
                .build();
        Request request = new Request.Builder().url(DataUtils.url).build();
        EchoWebSocketListener socketListener = new EchoWebSocketListener();
        mWebSocket = mOkHttpClient.newWebSocket(request, socketListener);
        mOkHttpClient.dispatcher().executorService();
        return mWebSocket ;
    }



    private static final class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            webSocket.send("open");
            Log.d(TAG,"onOpen") ;
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            Log.d(TAG,"onMessage") ;
            Log.d(TAG,bytes.toString()) ;
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            Log.d(TAG,"onMessage") ;
            Log.d(TAG, null==text?"null":text);

        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            Log.d(TAG,"onClosed") ;
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            webSocket.send(mHeartBeat);
            Log.d(TAG,"onClosing") ;

        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            Log.d(TAG,"onFailure") ;
            Log.d(TAG,t.toString()) ;
        }
    }



    /**
     * 获取网络状态
     *
     * @param context
     * @return one of TYPE_NONE, TYPE_MOBILE, TYPE_WIFI
     * @permission android.permission.ACCESS_NETWORK_STATE
     */
    public static final int getNetWorkStates(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return DataUtils.TYPE_NONE;//没网
        }

        int type = activeNetworkInfo.getType();
        switch (type) {
            case ConnectivityManager.TYPE_MOBILE:
                return DataUtils.TYPE_MOBILE;//移动数据
            case ConnectivityManager.TYPE_WIFI:
                return DataUtils.TYPE_WIFI;//WIFI
            default:
                break;
        }
        return DataUtils.TYPE_NONE;
    }

}
