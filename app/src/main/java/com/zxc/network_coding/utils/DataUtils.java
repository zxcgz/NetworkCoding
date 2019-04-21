package com.zxc.network_coding.utils;

import com.zxc.network_coding.R;
import com.zxc.network_coding.bean.CodeCacheControl;

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
    public static final String TAG_IP_SET = "getIpSet" ;
    //心跳包
    public static final String TAG_HEART = "heart" ;
    //发送文件标识码
    public static final String TAG_SEND_FILE_ID_CODE = "sendFileIdCode" ;
    //删除文件唯一标识码
    public static final String TAG_DELETE_FILE_ID_CODE = "deleteFileIdCode" ;
    //请求文件
    public static final String TAG_REQUEST_FILE = "receiveFile" ;
    //默认的消息内容
    public static final String MSG_DEFAULT = "default" ;

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

    /**
     * 文件分割使用的K、N值，暂定
     */
    public static final int K = 10 ;
    public static final int N = 15 ;

    /**
     * 随机编码矩阵生成时的m值，即有限域的大小
     */
    public static final int M = 12 ;

    /**
     * 数据库相关数据
     */
    public static final String DATABASE_NAME = "NC.db" ;

    /**
     * 数据库相关Bean中的数据
     */
    //File类中的tag
    public static final int FILE_CREATE = 1 ;
    public static final int FILE_NOT_FIND = -1 ;
    //Send类中的status
    public static final int STATUS_CREATE = 1 ;

    /**
     * 区分大文件和小文件的界限，暂定50MB
     */
    public static final long TINY_FILE_SIZE = 52428800 ;
    /**
     * 默认分片数为1
     */
    public static final int DEFAULT_SPLIT_NUM = 1 ;
    /**
     * CodeCacheControl类中的tag
     */
    public static final int TAG_SEND_FILE = 1 ;
    public static final int TAG_INTER_NOTE = 2 ;
    public static final int TAG_RECEIVE_FILE = 3;
    /**
     * SocketMessage类中的tag
     */
    //请求接收数据
    public static final int TAG_REQUEST_ADD = 1 ;
    //回应
    public static final int TAG_RESPONSE = 2 ;
}
