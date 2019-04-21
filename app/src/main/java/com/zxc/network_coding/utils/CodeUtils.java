package com.zxc.network_coding.utils;

import android.net.Uri;
import android.util.Log;

import com.zxc.jni.Matrix;

import java.io.IOException;

/**
 * 涉及编码的工具类
 */
public class CodeUtils {
    public static String TAG = "CodeUtils" ;
    /**
     * 第一次编码
     * @return
     */
    public static byte[][] code(Uri uri,int K,int N) throws IOException {
        //对文件进行处理
        //获取文件大小和文件流
        byte[] bytes = FileUtils.readLines(uri.getPath());
        //将文件分片
        int length = bytes.length / K + (bytes.length % K == 0 ? 0 : 1);
        byte[] splitArray = new byte[K * length];
        System.arraycopy(bytes, 0, splitArray, 0, bytes.length);
        //生成随机矩阵
        byte[] encodeMatrix = new byte[N * K];
        for (int i = 0; i < N * K; i++) {
            encodeMatrix[i] = (byte) ((int) (Math.random() * 255) - 128);
        }
        Matrix m = new Matrix(8);
        byte[] codeResult = m.multiply(encodeMatrix, N, K, splitArray, K, length);
        //将编码矩阵和编码结果组成一个矩阵，是一个N*(K+length)的矩阵
        byte[][] mat = new byte[N][K + length];
        Log.d(TAG, mat.length + "_" + encodeMatrix.length + "_" + codeResult.length);
        byte[][] bytes1 = ArrayUtils.array1to2(encodeMatrix, N, K);
        byte[][] bytes2 = ArrayUtils.array1to2(codeResult, N, length);
        for (int i = 0; i < N; i++) {  //0到K-1列为编码矩阵encodeMatrix
            for (int j = 0; j < K; j++) {
                mat[i][j] = bytes1[i][j] ;
                //mat[i][j] = encodeMatrix[i * K + j];
            }
        }
        for (int i = 0; i < N; i++) {  //从K到K+length为编码结果矩阵codeResult
            for (int j = K; j < K + length; j++) {
                mat[i][j] = bytes2[i][j-K] ;
                //mat[i][j] = codeResult[i * K + j - K];
            }
        }
        return mat ;
    }
    /**
     * 中间节点编码
     */
    public static byte[][] reCode(byte[] splitArray,int N,int K,int length){
        //获取随机编码矩阵

        return null ;
    }
    /**
     * 解码
     */
    public  static byte[] decode(byte[][]mat,int K){
        Matrix m = new Matrix(8);
        int col = mat[0].length;
        byte[][] encodeMAT = new byte[K][K];
        byte[][] MAT = new byte[K][col - K];
        for (int i = 0; i < K; i++) {
            for (int j = 0; j < K; j++) {
                encodeMAT[i][j] = mat[i][j];
            }
        }
        for (int i = 0; i < K; i++) {
            for (int j = 0; j < col - K; j++) {
                MAT[i][j] = mat[i][j + K];
            }
        }
        //对编码矩阵求逆
        byte[] inverse = m.inverse(ArrayUtils.array2to1(encodeMAT), K);
        byte[] multiply = m.multiply(inverse, K, K, ArrayUtils.array2to1(MAT), K, col - K);
        //FileUtils.byte2file(mContext.getCacheDir().getPath() + "/txt.txt", multiply);*/
        return multiply ;
    }
}
