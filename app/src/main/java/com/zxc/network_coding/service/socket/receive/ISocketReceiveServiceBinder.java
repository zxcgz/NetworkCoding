package com.zxc.network_coding.service.socket.receive;

/**
 * 数据传输过程中接收方服务的binder接口
 */
public interface ISocketReceiveServiceBinder {
    /**
     * 注册
     *
     * @param fileIdCode
     */
    void registerSink(String fileIdCode) ;

    /**
     * 取消注册
     *
     * @param fileIdCode
     */
    void unRegisterSink(String fileIdCode);
    /**
     * 添加一个是汇聚节点时的监听
     */
    void addSinkListener(String fileIdCode, SocketReceiveService.SinkListener listener) ;
}
