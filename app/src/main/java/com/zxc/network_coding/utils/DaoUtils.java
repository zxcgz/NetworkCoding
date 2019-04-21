package com.zxc.network_coding.utils;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.zxc.network_coding.entity.File;
import com.zxc.network_coding.entity.Send;
import com.zxc.network_coding.dao.Dao;
import com.zxc.network_coding.dao.DaoSession;
import com.zxc.network_coding.dao.FileDao;
import com.zxc.network_coding.dao.SendDao;

import java.util.Date;

/**
 * 数据库操作的工具类
 */
public class DaoUtils {

    public static final String TAG = "DaoUtils" ;


    private static Context mContext = null;

    public static Send insertSend(Context context, Uri uri, boolean backup, String backupName) {
        mContext = context;
        File backupFile = null ;
        File file = null ;
        DaoSession daoSession = Dao.getInstance(context).getDaoMaster().newSession();
        SendDao sendDao = daoSession.getSendDao();
        Send send = new Send() ;
        file = insertFile(daoSession.getFileDao(), uri, false, backupName);
        if (backup) {
            backupFile = insertFile(daoSession.getFileDao(), uri, true, backupName);
            send.setIsBackup(true);
            send.setBackupFile(backupFile);
            send.setBackupFileId(backupFile.getId());
        }else {
            send.setIsBackup(false);
        }
        send.setFile(file);
        send.setStatus(DataUtils.STATUS_CREATE);
        send.setStartDate(new Date());
        sendDao.insert(send);
        mContext = null;
        return send;
    }

    private static File insertFile(FileDao fileDao, Uri uri, boolean backup, String backupName) {
        File file = new File();
        java.io.File f = new java.io.File(uri.getPath());
        if (backup) {
            //复制文件到工作目录
            String absolutePath = mContext.getApplicationContext().getFilesDir().getAbsolutePath();
            //防止文件重名，加入日期信息
            Date date = new Date();
            long time = date.getTime();
            String name = backupName == null?FileUtils.getName(uri):backupName;
            name = time + "_" + name;
            //复制文件
            FileUtils.copyFile(f, absolutePath + "/" + name);
            //设置file信息
            file.setFilePath(absolutePath + "/" + name);
            file.setFileName(name);
            file.setIsBackup(true);
        } else {
            file.setFilePath(uri.getPath());
            file.setFileName(FileUtils.getName(uri));
            file.setIsBackup(false);
        }
        file.setHash(HashUtils.getFileSHA1(f));
        file.setMd5(HashUtils.getFileMD5(f));
        file.setTag(DataUtils.FILE_CREATE);
        fileDao.insert(file);
        file.setId(fileDao.getKey(file));
        Log.d(TAG,file.getId()+"") ;
        return file;
    }

    public static Send updateSend(Context context,Send send){
        mContext = context ;
        //TODO
        SendDao sendDao = Dao.getInstance(context).getDaoMaster().newSession().getSendDao();
        sendDao.update(send);


        mContext = null ;
        return null ;
    }

}
