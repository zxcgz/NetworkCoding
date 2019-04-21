package com.zxc.network_coding;

import android.content.Intent;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;
import com.zxc.network_coding.dao.Dao;
import com.zxc.network_coding.service.network.NetworkService;
import com.zxc.network_coding.service.network.NetworkServiceConnection;
import com.zxc.network_coding.utils.DataUtils;

/**
 * 发送方：
 * 1、第一次发送
 * 1.1、将文件的id上传到服务器中
 * 1.2、等待接收方上传相关信息
 * 1.3、建立网络拓扑
 * 1.4、发送文件，同时中间节点保存有部分数据
 * 1.5、将id从服务器中删除
 * 2、非第一次发送（服务器中没有相应的id信息）
 * 2.1、服务器询问所有的在线用户
 * 2.2、在线用户将自己保存的相关信息上传服务器，服务器判断数据是否完备
 * 2.3、如完备、则通知这些设备向目标设备发送数据
 * 2.4、如不完备、则等待数据持有者上线
 */
public class Application extends android.app.Application {
    private static String TAG = "Application";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
        /**
         * 开启服务
         */
        Log.d(TAG, "bindService");
        Intent intent = new Intent(this, NetworkService.class);
        bindService(intent, NetworkServiceConnection.getInstance(), BIND_AUTO_CREATE);
        /**
         * 初始化数据库
         */
        Dao.getInstance(this).initDaoMaster(DataUtils.DATABASE_NAME) ;
    }

    @Override
    public void onTerminate() {
        unbindService(NetworkServiceConnection.getInstance());
        super.onTerminate();
    }
}
