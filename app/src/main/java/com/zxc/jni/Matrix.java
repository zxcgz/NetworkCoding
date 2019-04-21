package com.zxc.jni;

import android.util.Log;

import java.util.Iterator;
import java.util.List;

public class Matrix {

    public static final String TAG = "Matrix";
    MatrixJNI matrixJNI = null;

    public Matrix(int m) {
        Log.d(TAG, "Matrix");
        matrixJNI = new MatrixJNI();
        Log.d(TAG, "Matrix");
        matrixJNI.init(m);
    }

    private Matrix() {
    }


    public int getRank(byte[] matrix, int nRow, int nCol) {
        return matrixJNI.getRank(matrix, nRow, nCol);
    }

    public int getRank(List<Byte> matrix, int nRow, int nCol) {
        int i = 0;
        byte[] m = new byte[matrix.size() - 1];
        Iterator<Byte> iterator = matrix.iterator();
        while (iterator.hasNext()) {
            m[i++] = iterator.next();
        }
        return matrixJNI.getRank(m, nRow, nCol);
    }

    public byte[] inverse(byte[] arrayData, int nK) {
        return matrixJNI.inverse(arrayData, nK);
    }

    public byte[] multiply(byte[] matrix1, int row1, int col1, byte[] matrix2, int row2, int col2) {
        byte[] multiply = matrixJNI.multiply(matrix1, row1, col1, matrix2, row2, col2);
        for (int i = 0; i < multiply.length; i++) {
            multiply[i] &= 0xff;
            //Log.d(TAG, Integer.toHexString(multiply[i]));
            //Log.d(TAG,Integer.toHexString(multiply[i])) ;
        }
        return multiply;
    }


    public void free() {
        if (matrixJNI != null) {
            matrixJNI.free();
            matrixJNI = null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        free();
    }
}
