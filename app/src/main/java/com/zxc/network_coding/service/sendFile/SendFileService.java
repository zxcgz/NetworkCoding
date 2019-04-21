package com.zxc.network_coding.service.sendFile;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zxc.jni.Matrix;
import com.zxc.network_coding.bean.CodeCacheControl;
import com.zxc.network_coding.bean.Ip;
import com.zxc.network_coding.bean.SimpleMessage;
import com.zxc.network_coding.entity.Send;
import com.zxc.network_coding.dao.Dao;
import com.zxc.network_coding.dao.DaoSession;
import com.zxc.network_coding.dao.SendDao;
import com.zxc.network_coding.service.network.INetworkServiceBinder;
import com.zxc.network_coding.service.network.NetworkServiceConnection;
import com.zxc.network_coding.utils.ArrayUtils;
import com.zxc.network_coding.utils.CacheUtils;
import com.zxc.network_coding.utils.CodeUtils;
import com.zxc.network_coding.utils.DaoUtils;
import com.zxc.network_coding.utils.DataUtils;
import com.zxc.network_coding.utils.DeviceUtils;
import com.zxc.network_coding.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import androidx.annotation.Nullable;

import static com.zxc.network_coding.utils.DataUtils.K;
import static com.zxc.network_coding.utils.DataUtils.N;

public class SendFileService extends Service {

    private static final String TAG = SendFileService.class.getName();

    private Uri mUri = null;
    //private DaoSession mDaoSession = null;
    private Context mContext = null;
    private INetworkServiceBinder mNetworkServiceBinder;
    private List<Ip> mIps = new CopyOnWriteArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //TODO 需要添加对文件大小的判断，小文件直接进行编码，对于大文件只进行分割，当有请求信息时再编码
        mUri = intent.getData();
        File file = new File(mUri.getPath());
        long length = file.length();
        if (length > DataUtils.TINY_FILE_SIZE) {
            //判断为大文件
        }

        try {


            //将相关数据存储到数据库中
            //mDaoSession = Dao.getInstance(getApplicationContext()).getDaoMaster().newSession();
            Send send = DaoUtils.insertSend(mContext, mUri, true, null);
            byte[][] code = CodeUtils.code(mUri, K, N);
            //将数据写入数据库
            //SendDao sendDao = Dao.getInstance(mContext).getDaoMaster().newSession().getSendDao();
            send.setSplit(K);
            //生成文件唯一的标识码
            String fileIdCode = DeviceUtils.getIMEI(mContext) + "_" + send.getFile().getHash() + "_" + send.getFile().getMd5();
            send.setFileIdCode(fileIdCode);
            DaoUtils.updateSend(mContext, send);
            //向服务器发消息，并获取ip集合
            mNetworkServiceBinder.sendMessage(DataUtils.TAG_SEND_FILE_ID_CODE, send.getFileIdCode());
            mNetworkServiceBinder.sendMessage(DataUtils.TAG_IP_SET, DataUtils.MSG_DEFAULT);
            CodeCacheControl control = new CodeCacheControl() ;
            control.setSplitNum(K);
            control.setFileIdCode(fileIdCode);
            control.setK(K);
            control.setLength(length);
            control.setN(N);
            control.setReceiveSplits(null);
            control.setTag(DataUtils.TAG_SEND_FILE);
            CacheUtils cacheUtils = new CacheUtils(mContext, control);
            cacheUtils.saveSplit(1,code,N);
            /**
             * 测试
             */
            /*//获取随机编码矩阵
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
            //直接解码
            //从mat中取出数据
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
            FileUtils.byte2file(mContext.getCacheDir().getPath() + "/txt.txt", multiply);*/
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mUri = null;
        //mDaoSession = null;
        mContext = null;
        mNetworkServiceBinder = null;
        mIps = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        mContext = getApplicationContext();
        mNetworkServiceBinder = NetworkServiceConnection.getInstance().getBinder();
        /*此处不获取ip集合
        mNetworkServiceBinder.registerListener((webSocket, text) -> {
            Gson gson = new Gson() ;
            SimpleMessage simpleMessage = gson.fromJson(text, SimpleMessage.class);
            if (simpleMessage!=null&&simpleMessage.getTag().equals(DataUtils.TAG_IP_SET)){
                //得到了ip集合
                //解析消息内容
                Type type = new TypeToken<CopyOnWriteArrayList<Ip>>() {}.getType();
                mIps = gson.fromJson(simpleMessage.getMsg(), type);
                //处理ip集合
            }
        });*/
    }

    private class MyBinder extends Binder implements ISendFileServiceBinder {

        @Override
        public String getRate() {
            return null;
        }

    }
}
