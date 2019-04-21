package com.zxc.network_coding.bean;

import androidx.annotation.Nullable;

/**
 * 编码缓存控制Bean
 */
public class CodeCacheControl {
    /**
     * 标志当前用户是发送方、中间节点还是接收方
     * 其中发送方和接收方，需要完整缓存所有的编码分片
     * 而中间节点可以部分接受甚至丢失部分分片
     */
    private int tag;
    /**
     * 一共的分片数
     */
    private int splitNum;
    /**
     * 编码相关的N、K、length信息
     */
    private int N;
    private int K;
    private long length;

    public boolean equals(@Nullable CodeCacheControl control) {
        boolean b = control.getFileIdCode().equals(this.fileIdCode)
                && control.getK() == this.K
                && control.getLength() == this.length
                && control.getN() == this.N
                && control.getReceiveSplits().equals(this.receiveSplits)
                && control.getSplitNum() == this.splitNum
                && control.getTag() == this.tag;
        return b;
    }

    /**
     * 存储的分片数
     */
    private int[] receiveSplits;
    /**
     * 文件唯一标识码
     */
    private String fileIdCode;

    public String getFileIdCode() {
        return fileIdCode;
    }

    public void setFileIdCode(String fileIdCode) {
        this.fileIdCode = fileIdCode;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getSplitNum() {
        return splitNum;
    }

    public void setSplitNum(int splitNum) {
        this.splitNum = splitNum;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public int getK() {
        return K;
    }

    public void setK(int k) {
        K = k;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public int[] getReceiveSplits() {
        return receiveSplits;
    }

    public void setReceiveSplits(int[] receiveSplits) {
        this.receiveSplits = receiveSplits;
    }
}
