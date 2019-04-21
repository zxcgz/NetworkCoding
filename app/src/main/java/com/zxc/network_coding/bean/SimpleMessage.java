package com.zxc.network_coding.bean;

/**
 * 一个简单的消息bean
 */
public class SimpleMessage {
    private String tag ;
    private String msg ;

    public SimpleMessage() {}
    public SimpleMessage(String tag, String msg) {
        this.tag = tag;
        this.msg = msg;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
