package com.zxc.network_coding.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.zxc.network_coding.bean.CodeCacheControl;
import com.zxc.network_coding.bean.SplitCacheControl;

import java.io.File;
import java.io.IOException;

/**
 * 缓存管理的工具类
 * 缓存中包含：
 * 以FileIdCode为目录名
 * |--------传输情况的描述文件（N、K、length值信息）
 * |--------分片号（目录）
 * |-------编码文件
 * |-------  ……
 * 类功能：
 * 初始化：输入FileIdCode和分片数，创建相应目录和描述文件
 * 添加分片：输入分片号和N、K、length信息，创建分片目录和描述文件
 * 写入分片：输入编码文件数组，写入编码文件，修改描述文件
 * 读取分片：输入分片号，读取分片文件，返回编码文件数组
 * 传输情况设置：输入分片号，修改传输情况描述文件
 * <p>
 * 分片号目录下的文件是将编码结果
 */
public class CacheUtils {


    public static final String TAG = "CacheUtils";
    private Context mContext;
    private CodeCacheControl mCodeCacheControl;
    private Gson mGson;
    private String mDescriptionJson = "/description.json";
    private String mSplitCacheControlJson = "/SplitCacheControl.json";
    private File mRootDir;
    private String mMatrixFileName = "/matrix.nc" ;

    /**
     * 通过FileIdCode获取一个CacheUtils对象
     */
    public CacheUtils(Context context, CodeCacheControl codeCacheControl) {
        mGson = new Gson();
        mContext = context;
        mCodeCacheControl = codeCacheControl;
        //得到cache目录
        String path = mContext.getCacheDir().getPath() + "/" + mCodeCacheControl.getFileIdCode();
        mRootDir = new File(path);
        if (!mRootDir.exists()) {
            //文件夹不存在
            //创建文件夹
            if (mRootDir.mkdir()) {
                //创建描述文件
                FileUtils.byte2file(path + mDescriptionJson, mGson.toJson(mCodeCacheControl).getBytes());
            }
        } else {
            try {//文件夹存在，判断是否存在描述文件
                String p = path + "/" + mDescriptionJson;
                File f = new File(p);
                if (f.exists()) {
                    //描述文件存在，判断是否相同
                    String s = new String(FileUtils.readLines(p));
                    CodeCacheControl control = mGson.fromJson(s, CodeCacheControl.class);
                    if (!control.equals(mCodeCacheControl)) {
                        //文件不同，重新创建
                        FileUtils.byte2file(p, mGson.toJson(mCodeCacheControl).getBytes());
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取指定的分片
     *
     * @return
     */
    public byte[][] getSplit(int splitNum) {
        try {
            String path = mRootDir.getPath() + "/" + splitNum;
            File file = new File(path);
            if (splitNum > mCodeCacheControl.getSplitNum()) {
                //给定的分片数大于定义的分片数返回空值
                return null;
            } else if (!file.exists()) {
                //目录不存在，返回空
                return null;
            } else
                //从目录中的文件中随机取出K个文件
                //先获取SplitCacheControl.json文件
                if (new File(path + mSplitCacheControlJson).exists()) {
                    //文件存在
                    //读取内容
                    String s = new String(FileUtils.readLines(path + mSplitCacheControlJson));
                    //解析文件
                    SplitCacheControl splitCacheControl = mGson.fromJson(s, SplitCacheControl.class);
                    int matrixRowNum = splitCacheControl.getMatrixRowNum();
                    //TODO 此处需要调整
                    //返回文件
                    /**
                     * 因为不管是源节点、中间节点还是汇聚节点，里面的数据总是要全部取出来
                     * 所以，在当前目录下用一个文件存储整个编码结果矩阵
                     * 此方法将整个文件返回
                     */
                    return ArrayUtils.array1to2(FileUtils.readLines(path+mMatrixFileName),matrixRowNum, (int) mCodeCacheControl.getLength()) ;
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*{
            //TODO 假定文件是分行存储的
            //从
        }*/
        return null;
    }

    /**
     * 保存分片
     * @param splitNum  分片号
     * @param split     分片矩阵
     * @param row       矩阵行数
     */
    public void saveSplit(int splitNum,byte[][] split,int row){
        //判断是否存在相应目录
        String s = mRootDir.getPath() +"/"+ splitNum;
        File root = new File(s) ;
        if (!root.exists()){
            root.mkdir() ;

        }
        //目录存在，写入配置文件并修改，同时将矩阵写入目录中
        SplitCacheControl cacheControl = new SplitCacheControl(row) ;
        FileUtils.byte2file(s+mSplitCacheControlJson,mGson.toJson(cacheControl).getBytes());
        //将矩阵存储
        FileUtils.byte2file(s+mMatrixFileName,ArrayUtils.array2to1(split));
    }

    /*private boolean checkDir(){
        return false ;
    }
    private boolean cha*/

    private CacheUtils() {}


}
