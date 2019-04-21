package com.zxc.network_coding.service.socket.receive;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.zxc.network_coding.bean.Ip;
import com.zxc.network_coding.bean.SocketControlMessage;
import com.zxc.network_coding.utils.DataUtils;
import com.zxc.network_coding.utils.TopologyUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 在应用启动时就开启
 * 启动一个socket服务端，监听某一个端口来接收相应的控制信息
 * 在收到不同的控制信息时，启动新的服务端来接收编码结果矩阵
 * 此socket服务器用于网络拓扑建立时
 * <p>
 * 要设置对于某一个文件的接收模式，是中间节点还是汇聚节点，默认为中间节点
 */
public class SocketReceiveService extends Service {
    private MyBinder mBinder = null;
    private List<MyServerSocket> mServerSockets = new CopyOnWriteArrayList<>();
    private List<String> mSinks = new CopyOnWriteArrayList<>();
    private ServerSocket mServerSocket;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mBinder = new MyBinder();
        //启动一个监听socket（服务端）
        startListenerSocket();
        return mBinder;
    }

    private void startListenerSocket() {
        try {
            mServerSocket = new ServerSocket(1989);
            //循环接收
            while (true) {
                Socket accept = mServerSocket.accept();
                String str = getString(accept.getInputStream());
                //解析数据
                /**
                 * 1、发送方：文件的唯一标识码和其所在的拓扑层级
                 * 2、本机判断自己是否在网络拓扑中
                 * 3、如果没有在网络拓扑中，则同意接收
                 * 4、如果在网络拓扑中，则判断发送方的拓扑层级是否小于本地的拓扑层级
                 * 5、如果小于，则同意接收，如果不小于，为了防止环路的出现，拒绝接收
                 *
                 */
                //解析数据
                Gson gson = new Gson();
                SocketControlMessage socketMessage = gson.fromJson(str, SocketControlMessage.class);
                int tag = socketMessage.getTag();
                if (tag == DataUtils.TAG_REQUEST_ADD) {
                    //请求添加
                    boolean b = TopologyUtils.checkTp(socketMessage.getFileIdCode(), socketMessage.getLevel());
                    socketMessage.setCheck(b);
                    if (b) {
                        //允许接收
                        //判断是否有针对此fileId的接收socket启动
                        boolean have = false;
                        for (MyServerSocket socket : mServerSockets) {
                            if (socket.getFileIdCode().equals(socketMessage.getFileIdCode())) {
                                //相同
                                have = true;
                            }
                        }
                        if (!have) {
                            //不存在，启动socket
                            String hostAddress = accept.getInetAddress().getHostAddress();
                            startSocket(socketMessage.getFileIdCode(), hostAddress, socketMessage.getPort());
                        }
                        //TODO 需要修改port值
                        socketMessage.setPort(0);
                    }
                    //将信息返回
                    OutputStream outputStream = accept.getOutputStream();
                    outputStream.write(gson.toJson(socketMessage).getBytes());
                    outputStream.flush();
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        try {
            mServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onUnbind(intent);
    }

    /**
     * 根据文件唯一标识码启动socket
     *
     * @param fileIdCode 文件唯一标识码
     */
    private void startSocket(String fileIdCode, String ip, int port) {
        try {
            //判断是否是sink节点
            boolean isSink = isSink(fileIdCode);
            MyServerSocket socket = new MyServerSocket(fileIdCode);
            socket.addSocket(ip, port);
            mServerSockets.add(socket);
            while (true) {
                /**
                 * 解析编码数据
                 */
                Socket accept = socket.accept();
                //到达此处说明已经接收到数据。则调用connect方法
                socket.connect();
                String string = getString(accept.getInputStream());
                if (isSink){
                    //如果是汇聚节点，则需要报告进度
                    //报告进度
                    //TODO 需要修改进度
                    socket.mListener.onRate(0);
                }

                //TODO 将接收到的数据进行存储

                //判断是否还有需要接收的数据
                if (socket.surplusConnect(accept.getInetAddress().getHostAddress(),accept.getPort())==0){
                    //没有需要接收的数据
                    //将存储的矩阵进行编码
                    //TODO 编码
                    //编码
                    //TODO 判断是存储转发还是恢复数据
                    if (isSink){
                        //恢复数据
                    }else {
                        //存储转发
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从输入流中获取字符串
     *
     * @param stream
     * @return
     */
    private String getString(InputStream stream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte buffer[] = new byte[1024 * 4];
        int length;
        // 从InputStream当中读取客户端所发送的数据
        while ((length = stream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8.name());
    }

    private boolean isSink(String fileIdCode) {
        for (String s :
                mSinks) {
            if (s.equals(fileIdCode))
                return true;
        }
        return false;
    }

    /**
     * Socket服务器
     */
    private class MyServerSocket extends ServerSocket {
        /**
         * 作为sink节点时的监听
         */
        private SinkListener mListener ;
        /**
         * 此socket是否已经有传入链接
         */
        private boolean isConnect = false;
        /**
         * 需要接收的socket集合
         */
        private List<Ip> mInSocket = new CopyOnWriteArrayList<>() ;
        //private Map<String, Integer> mInSocket = new HashMap<>();
        /**
         * 需要接收的文件
         */
        private String mFileIdCode;

        /**
         * 获取需要接收的文件
         * @return
         */
        public String getFileIdCode() {
            return mFileIdCode;
        }
        /**
         * 获取监听对象
         */
        public SinkListener getListener(){
            return mListener ;
        }

        /**
         * 添加一个传入链接
         * @param ip        传入的ip
         * @param port      传入的port
         */
        public void addSocket(String ip, int port) {
            //如果当前socket已经有链接则不再添加
            if (isConnect)
                return;
            //从列表中查找是否存在
            for (Ip i:
                    mInSocket) {
                if (i.getIp().equals(ip)&&i.getPort() == port) {
                    //存在相同的socket，则不添加
                    return;
                }
            }
            mInSocket.add(new Ip(ip,port)) ;
        }

        /**
         * 当前socket有数据传入，则调用此方法，则，socket不再接受其他传入链接
         */
        public void connect() {
            if (!isConnect) {
                isConnect = true;
            }
        }
        /**
         * 判断是否还需要接收数据
         */
        private boolean first = true ;
        private  List<Ip> inSocket ;
        public int surplusConnect(String ip,int port){
            if (first){
                inSocket = mInSocket ;
            }
            inSocket.remove(new Ip(ip,port)) ;
            return inSocket.size();
        }

        /**
         * 构造方法
         * @param fileIdCode        文件Id
         * @throws IOException
         */
        public MyServerSocket(String fileIdCode) throws IOException {
            mFileIdCode = fileIdCode;
        }

        public void setListener(SinkListener listener) {
            mListener = listener ;
        }


    }

    /**
     * Binder
     */
    private class MyBinder extends Binder implements ISocketReceiveServiceBinder {
        /**
         * 注册文件
         *
         * @param fileIdCode    文件标识码
         */
        @Override
        public void registerSink(String fileIdCode) {
            mSinks.add(fileIdCode);
        }

        /**
         * 取消注册文件
         *
         * @param fileIdCode    文件标识码
         */
        @Override
        public void unRegisterSink(String fileIdCode) {
            mSinks.remove(fileIdCode);
        }

        /**
         * 添加一个是汇聚节点时的监听
         */
        @Override
        public void addSinkListener(String fileIdCode,SinkListener listener){
            //找到接收指定文件的socket
            for (MyServerSocket s :
                    mServerSockets) {
                String f = s.getFileIdCode();
                if (f!=null&&f.equals(fileIdCode)){
                    //找到了
                    //为其添加监听
                    s.setListener(listener);
                }
            }
        }
    }

    public interface SinkListener{
        void onRate(int rate) ;
    }

}
