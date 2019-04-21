package com.zxc.network_coding.utils;

public class ArrayUtils {
    public static byte[] intArrayToByteArray(int[] intarr) {
        int bytelength = intarr.length * 4;//长度
        byte[] bt = new byte[bytelength];//开辟数组
        int curint = 0;
        for (int j = 0, k = 0; j < intarr.length; j++, k += 4) {
            curint = intarr[j];
            bt[k] = (byte) ((curint >> 24) & 0b1111_1111);//右移4位，与1作与运算
            bt[k + 1] = (byte) ((curint >> 16) & 0b1111_1111);
            bt[k + 2] = (byte) ((curint >> 8) & 0b1111_1111);
            bt[k + 3] = (byte) ((curint >> 0) & 0b1111_1111);
        }


        return bt;
    }

    public static int[] byteArrayToIntArray(byte[] btarr) {
        if (btarr.length % 4 != 0) {
            return null;
        }
        int[] intarr = new int[btarr.length / 4];

        int i1, i2, i3, i4;
        for (int j = 0, k = 0; j < intarr.length; j++, k += 4)//j循环int		k循环byte数组
        {
            i1 = btarr[k];
            i2 = btarr[k + 1];
            i3 = btarr[k + 2];
            i4 = btarr[k + 3];

            if (i1 < 0) {
                i1 += 256;
            }
            if (i2 < 0) {
                i2 += 256;
            }
            if (i3 < 0) {
                i3 += 256;
            }
            if (i4 < 0) {
                i4 += 256;
            }
            intarr[j] = (i1 << 24) + (i2 << 16) + (i3 << 8) + (i4 << 0);//保存Int数据类型转换

        }
        return intarr;
    }

    /**
     * 将二维数组转换成一维数组
     *
     * @param a
     * @return
     */
    public static byte[] array2to1(byte[][] a) {
        byte[] result = new byte[a.length * a[0].length];
        int index = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                result[index++] = a[i][j];
            }
        }
        return result;
    }

    /**
     * 将一维数组转换成二维数组
     *
     * @param a   一维数组
     * @param row 行
     * @param col 列
     * @return
     */
    public static byte[][] array1to2(byte[] a, int row, int col) {
        byte[][] result = new byte[row][col] ;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                result[i][j] = a[i*col+j] ;
            }
        }
        return result;
    }

}
