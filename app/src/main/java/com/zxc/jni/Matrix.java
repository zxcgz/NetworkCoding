package com.zxc.jni;

/**
 * jni接口
 * 矩阵运算类
 */
public class Matrix {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("matrix");
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native void inverse(byte[][] e,int n) ;
}
