package com.zxc.network_coding.service.network;

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
     * @param tag   消息类型
     * @param msg   消息内容
     */
    void sendMessage(String tag,String msg) ;

    /**
     * 注册监听
     * @param listener
     */
    void registerListener(NetworkService.NetworkListener listener) ;
    /**
     * 移除注册的监听
     *
     * @param listener
     */
    void unregisterListener(NetworkService.NetworkListener listener) ;
}
