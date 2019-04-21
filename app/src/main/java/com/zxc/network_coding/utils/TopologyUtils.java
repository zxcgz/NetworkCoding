package com.zxc.network_coding.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 关于网络拓扑的工具类
 */
public class TopologyUtils {
    /**
     * 用来存储文件标识码与对应的网络层级的map
     */
    private static Map<String,Integer> tp = new HashMap<>() ;

    /**
     * 判断是否接收该发送者
     * @param fileId    文件标识码
     * @param level     层级
     * @return          是否接收
     */
    public static boolean checkTp(String fileId,int level){
        Set<String> strings = tp.keySet();
        for (String s:strings){
            if (s.equals(fileId)){
                //本机处于网络中，判断层级
                Integer l = tp.get(fileId);
                if (l!=null&&l>=level){
                    //接收
                    return true ;
                }else {
                    return false ;
                }
            }
        }
        //本机没有在网络中，添加并允许接收
        tp.put(fileId,level) ;
        return true ;
    }
}
