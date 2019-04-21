package com.zxc.network_coding.utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    /*private static String mHeartBeat = DataUtils.TAG_HEART;
    private static WebSocket mWebSocket = null;
    private static OkHttpClient mOkHttpClient;

    private static List<NetworkListener> mListener = new ArrayList<>();


    *//**
     * 从服务器中获取到正在运行的程序的IP地址集合
     *//*


    *//**
     * 连接服务器
     *//*
    public static WebSocket connectServer() {
        if (mWebSocket == null) {
            Log.d(TAG, "connectServer");
            mOkHttpClient = new OkHttpClient.Builder()
                    .readTimeout(3000, TimeUnit.SECONDS)//设置读取超时时间
                    .writeTimeout(3000, TimeUnit.SECONDS)//设置写的超时时间
                    .connectTimeout(3000, TimeUnit.SECONDS)//设置连接超时时间
                    .build();
            Request request = new Request.Builder().url(DataUtils.url).build();
            EchoWebSocketListener socketListener = new EchoWebSocketListener();
            mWebSocket = mOkHttpClient.newWebSocket(request, socketListener);
            mOkHttpClient.dispatcher().executorService();
        }
        return mWebSocket;
    }

    *//**
     * 监听接口
     *//*
    public interface NetworkListener {
        void onOpen(WebSocket webSocket, Response response);

        void onMessage(WebSocket webSocket, ByteString bytes);

        void onMessage(WebSocket webSocket, String text);

        void onClosed(WebSocket webSocket, int code, String reason);

        void onClosing(WebSocket webSocket, int code, String reason);

        void onFailure(WebSocket webSocket, Throwable t, Response response);
    }

    *//**
     * 注册监听
     *
     * @param listener
     *//*
    public static void registerListener(NetworkListener listener) {
        mListener.add(listener);
    }

    *//**
     * 移除注册的监听
     *
     * @param listener
     *//*
    public static void unregisterListener(NetworkListener listener) {
        mListener.remove(listener);
    }


    private static final class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            webSocket.send("open");
            Log.d(TAG, "onOpen");
            setListener(onOpen, webSocket, response);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            Log.d(TAG, "onMessage");
            Log.d(TAG, bytes.toString());
            setListener(onMessage, webSocket, bytes);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            Log.d(TAG, "onMessage");
            Log.d(TAG, null == text ? "null" : text);
            setListener(onMessage, webSocket, text);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            Log.d(TAG, "onClosed");
            setListener(onClosed, webSocket, code, reason);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            webSocket.send(mHeartBeat);
            Log.d(TAG, "onClosing");
            setListener(onClosing, webSocket, code, reason);

        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            Log.d(TAG, "onFailure");
            Log.d(TAG, t.toString());
            setListener(onFailure, webSocket, t, response);
        }
    }

    *//**
     * 需要调用的方法的标识码
     *//*
    private static final int onOpen = 0;
    private static final int onMessage = 1;
    private static final int onClosed = 2;
    private static final int onClosing = 3;
    private static final int onFailure = 4;

    *//**
     * 调用监听中的方法
     *
     * @param tag
     * @param o
     *//*
    private static void setListener(int tag, Object... o) {
        for (NetworkListener listener :
                mListener) {
            switch (tag) {
                case onOpen:
                    listener.onOpen((WebSocket) o[0], (Response) o[1]);
                    break;
                case onMessage:
                    listener.onMessage((WebSocket) o[0], (String) o[1]);
                    break;
                case onClosed:
                    listener.onClosed((WebSocket) o[0], (Integer) o[1], (String) o[2]);
                    break;
                case onClosing:
                    listener.onClosing((WebSocket) o[0], (Integer) o[1], (String) o[2]);
                    break;
                case onFailure:
                    listener.onFailure((WebSocket) o[0], (Throwable) o[1], (Response) o[2]);
                    break;
            }
        }
    }*/


    /**
     * 获取网络状态
     *
     * @param context
     * @return one of TYPE_NONE, TYPE_MOBILE, TYPE_WIFI
     * @permission android.permission.ACCESS_NETWORK_STATE
     */
    public static final int getNetWorkStatus(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
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


    /**
     * 网络发生变化的相关类
     */
    public static class NetworkStatusChange {
        private static Context mContext ;
        private static NetworkStatusChange mNetworkStatusChange = null;
        private List<NetworkStatusChangeListener> mListeners = new ArrayList<>();
        private NetworkStatusBroadcastReceiver mNetworkStatusReceiver;

        private NetworkStatusChange() {
            //注册广播接收者
            mNetworkStatusReceiver = new NetworkStatusBroadcastReceiver(mContext);
            IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            mContext.registerReceiver(mNetworkStatusReceiver, intentFilter);
        }

        public static NetworkStatusChange getInstance(Context context) {
            if (mNetworkStatusChange == null) {
                mNetworkStatusChange = new NetworkStatusChange();
            }
            mContext = context ;
            return mNetworkStatusChange;
        }

        /**
         * 注册监听
         *
         * @param listener
         */
        public void registerListener(NetworkStatusChangeListener listener) {
            mListeners.add(listener);
        }

        /**
         * 取消注册
         *
         * @param listener
         */
        public void unregisterListener(NetworkStatusChangeListener listener) {
            mListeners.remove(listener);
        }


        /**
         * 调用集合中对象的对应方法
         *
         * @param tag
         */
        private void setListener(int tag) {
            for (NetworkStatusChangeListener l :
                    mListeners) {
                switch (tag) {
                    case DataUtils.TYPE_MOBILE:
                        l.onMobile();
                        break;
                    case DataUtils.TYPE_WIFI:
                        l.onWIFI();
                        break;
                    case DataUtils.TYPE_NONE:
                        l.onNone();
                        break;
                    default:
                        l.onDisconnect();
                        break;
                }
            }
        }

        public interface NetworkStatusChangeListener {
            /**
             * WIFI连接
             */
            void onWIFI();

            /**
             * 没有连接
             */
            void onNone();

            /**
             * 手机流量
             */
            void onMobile();

            /**
             * 有连接但是无法访问互联网
             */
            void onDisconnect();
        }

        /**
         * 广播接收者
         */
        private class NetworkStatusBroadcastReceiver extends BroadcastReceiver {

            private String TAG = this.getClass().getName();

            private int mLastNetworkStatus;

            public NetworkStatusBroadcastReceiver(@NonNull Context context) {
                mLastNetworkStatus = NetworkUtils.getNetWorkStatus(context);
            }

            private NetworkStatusBroadcastReceiver() {
            }

            @Override
            public void onReceive(Context context, Intent intent) {
                int networkStatus = NetworkUtils.getNetWorkStatus(context);
                setListener(networkStatus);
            }
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            mContext.unregisterReceiver(mNetworkStatusReceiver);
            mContext = null ;
            mNetworkStatusChange = null ;
            mListeners = null ;
            mNetworkStatusReceiver = null ;
        }
    }
}
