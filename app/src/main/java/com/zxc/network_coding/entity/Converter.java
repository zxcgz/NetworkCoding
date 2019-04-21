package com.zxc.network_coding.entity;

import com.zxc.network_coding.utils.ArrayUtils;

import org.greenrobot.greendao.converter.PropertyConverter;

public class Converter {

    public static class TransmitConverter implements PropertyConverter<int[], byte[]> {

        @Override
        public int[] convertToEntityProperty(byte[] databaseValue) {
            if (databaseValue == null){
                return null ;
            }
            return ArrayUtils.byteArrayToIntArray(databaseValue) ;
        }

        @Override
        public byte[] convertToDatabaseValue(int[] entityProperty) {
            if (entityProperty == null){
                return null ;
            }
            return ArrayUtils.intArrayToByteArray(entityProperty) ;
        }
    }

}
