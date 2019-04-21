package com.zxc.network_coding.bean;

/**
 * 分片控制的bean
 * 每一个分片目录下存储的是经过编码的编码结果矩阵
 * 即从目录中取出的矩阵直接传输
 * 这样可以减轻源节点的负担
 */
public class SplitCacheControl {
    /**
     * 编码结果矩阵行数
     * 对于大文件而言，每一个分片可能通过不同的中间节点进行传输
     * 会造成每一个中间节点接收到的矩阵行数不同
     * 所以使用此变量来记录
     */
    private int matrixRowNum ;

    public SplitCacheControl(int matrixRowNum) {
        this.matrixRowNum = matrixRowNum;
    }

    public int getMatrixRowNum() {
        return matrixRowNum;
    }

    public void setMatrixRowNum(int matrixRowNum) {
        this.matrixRowNum = matrixRowNum;
    }
}
