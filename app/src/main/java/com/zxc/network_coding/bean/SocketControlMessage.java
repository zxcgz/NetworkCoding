package com.zxc.network_coding.bean;

/**
 * 用户与用户之间交换控制信息的bean
 */
public class SocketControlMessage {
    /**
     * 标签
     */
    private int tag ;
    /**
     * 文件id
     */
    private String fileIdCode ;
    /**
     * 发送方的层级
     */
    private int level ;
    /**
     * 返回接收方接收的端口
     */
    private int port ;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public SocketControlMessage(int tag, String fileIdCode, int level, int port, boolean check) {
        this.tag = tag;
        this.fileIdCode = fileIdCode;
        this.level = level;
        this.port = port;
        this.check = check;
    }

    /**
     * 返回的判断信息
     */
    private boolean check ;

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getFileIdCode() {
        return fileIdCode;
    }

    public void setFileIdCode(String fileIdCode) {
        this.fileIdCode = fileIdCode;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

}
