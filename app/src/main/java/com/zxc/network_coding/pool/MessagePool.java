package com.zxc.network_coding.pool;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 消息队列，用于在不同类之间传递消息
 */
public class MessagePool {

    private static MessagePool mPool = null;
    public static final String TAG = "MessagePool" ;

    /**
     * Map中的key是一个tag，value是一个序列化后的字符串
     */
    private final HashMap<String, String> mPoolMap = new HashMap<>();
    private int mMapSize = 20;

    public static MessagePool getInstance() {
        if (mPool == null) {
            mPool = new MessagePool();
        }
        return mPool;
    }

    public boolean add(String key,String value) {
        synchronized (mPoolMap) {
            if (mMapSize <= 20 && mPoolMap.size() > 0) {
                mPoolMap.put(key, value) ;
                mMapSize++;
            }
        }
        return true;
    }

    public String get(String tag) {
        String msg = null ;
        synchronized (mPoolMap) {
            if (mMapSize > 0 && mPoolMap.size() > 0) {
                msg = mPoolMap.get(tag);
                mPoolMap.remove(tag) ;
                mMapSize-- ;
            }
        }
        return msg;
    }

    public boolean isExist(String tag){
        synchronized (mPoolMap){
            Set<String> strings = mPoolMap.keySet();
            for (int i = 0;i<strings.size();i++){
                if (strings.equals(tag)){
                    return true ;
                }
            }
        }
        return false ;
    }
}
