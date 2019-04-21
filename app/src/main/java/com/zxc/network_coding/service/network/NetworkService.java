package com.zxc.network_coding.service.network;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.zxc.network_coding.bean.SimpleMessage;
import com.zxc.network_coding.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


/**
 * 维持与服务器交互的Service
 */
public class NetworkService extends Service {

    private WebSocket mWebSocket = null;
    public String TAG = "NetworkService";
    private MyBinder myBinder = null;
    private Context mContext;
    private String mHeartBeat = DataUtils.TAG_HEART;
    private OkHttpClient mOkHttpClient;

    private boolean isConnect = false ;

    private List<NetworkListener> mListener = new ArrayList<>();
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        mWebSocket = connectServer();
        mContext = getApplicationContext();
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
        Log.d(TAG, "onBind");
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
            return isConnect;
        }

        @Override
        public void close() {
            mWebSocket.close(DataUtils.SUCCESS_CODE, DataUtils.SUCCESS_REASON);
        }

        @Override
        public void sendMessage(String tag, String msg) {
            SimpleMessage simpleMessage = new SimpleMessage(tag,msg) ;
            Gson gson = new Gson() ;
            String s = gson.toJson(simpleMessage);
            mWebSocket.send(s);
        }
        /**
         * 注册监听
         *
         * @param listener
         */
        @Override
        public void registerListener(NetworkListener listener) {
            mListener.add(listener);
        }

        /**
         * 移除注册的监听
         *
         * @param listener
         */
        @Override
        public void unregisterListener(NetworkListener listener) {
            mListener.remove(listener);
        }

    }

    /**
     * 连接服务器
     */
    private WebSocket connectServer() {
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

    /**
     * 监听接口
     */
    public interface NetworkListener {
        void onMessage(WebSocket webSocket, String text);
    }




    private final class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            //webSocket.send("open");
            Log.d(TAG, "onOpen");
            isConnect = true ;
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
            isConnect = false ;
            setListener(onClosed, webSocket, code, reason);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            Log.d(TAG, "onClosing");
            isConnect = false ;
            setListener(onClosing, webSocket, code, reason);

        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            Log.d(TAG, "onFailure");
            Log.d(TAG, t.toString());
            isConnect = false ;
            setListener(onFailure, webSocket, t, response);
        }
    }

    /**
     * 需要调用的方法的标识码
     */
    private static final int onOpen = 0;
    private static final int onMessage = 1;
    private static final int onClosed = 2;
    private static final int onClosing = 3;
    private static final int onFailure = 4;

    /**
     * 调用监听中的方法
     *
     * @param tag
     * @param o
     */
    private void setListener(int tag, Object... o) {
        Log.d(TAG,"setListener") ;
        Log.d(TAG,tag+"") ;
        for (NetworkListener listener :
                mListener) {
            switch (tag) {
                case onMessage:
                    listener.onMessage((WebSocket) o[0], (String) o[1]);
                    break;
            }
        }
    }


}
