package com.zxc.network_coding.utils;

import com.zxc.network_coding.R;

public class DataUtils {
    //WebsSocket地址
    public static final String url = "ws://172.17.100.2:8080/NetworkCoding/socket" ;
    /**
     * 访问WebSocket状态信息
     */
    //成功或者正常的代码
    public static final int SUCCESS_CODE = 200 ;
    //成功或者正常的原因
    public static final String SUCCESS_REASON = "success" ;
    /**
     * 消息池中的TAG
     */
    //获取ip集合
    public static final String TAG_IPSET = "ipSet" ;

    /**
     * 和webSocket交互的消息
     */
    //获取ip集合
    public static final String MSG_IPSET = "getIpSet" ;
    //心跳包
    public static final String MSG_HEART = "heart" ;

    /**
     * startActivityForResult中的请求码，用于不同在不同的Activity中响应
     */
    //选择文件
    public static final int CHOOSE_FILE_CODE = 0 ;
    /**
     * 网络状态码
     */
    public static final int TYPE_NONE = -1;
    public static final int TYPE_MOBILE = 0;
    public static final int TYPE_WIFI = 1;

}
