package com.zxc.network_coding.service;

import java.util.Map;
import java.util.Set;

/**
 * 访问服务内部方法的接口
 */
public interface INetworkServiceBinder {
    /**
     * 重新连接
     * @return  连接是否成功
     */
    boolean reconnect() ;

    /**
     * 是否正在连接
     * @return  正在连接与否
     */
    boolean isConnect() ;

    /**
     * 关闭连接
     */
    void close() ;

    /**
     * 发送消息
     * @param msg   消息内容
     */
    void sendMessage(String msg) ;

    /**
     * 获取ip集合
     * @return  返回一个Map，Map中包含ip地址和port值
     */
    Map<String,String> getIpSet() ;
}
