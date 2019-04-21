package com.zxc.jni;

/**
 * jni接口
 * 矩阵运算类
 */
public class MatrixJNI {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("matrix");
    }
    /**
     * 矩阵求逆
     */
    protected native byte[] inverse(byte[] arrayData, int nK);
    protected native int getRank(byte[] matrix,int nRow,int nCol) ;
    protected native void init(int m) ;
    protected native byte[] multiply(byte[] matrix1, int row1, int col1, byte[] matrix2, int row2, int col2);
    protected native void free() ;
}
