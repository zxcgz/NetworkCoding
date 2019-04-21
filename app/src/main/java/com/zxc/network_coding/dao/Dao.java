package com.zxc.network_coding.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.zxc.network_coding.R;
import com.zxc.network_coding.utils.DataUtils;

import androidx.annotation.NonNull;

public class Dao {

    private static Dao mDao = null ;
    private static String mDatabaseName = DataUtils.DATABASE_NAME;
    private static DaoMaster mDaoMaster = null ;
    private static Context mContext ;

    public static Dao getInstance(@NonNull Context context){
        if (mDao == null){
            mDao = new Dao() ;
        }
        mContext = context ;
        return mDao ;
    }


    public void initDaoMaster(String databaseName){
        if (mDaoMaster == null){
            if (databaseName != null&&databaseName.equals("")){
                mDatabaseName = databaseName ;
            }
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext,mDatabaseName) ;
            SQLiteDatabase writableDatabase = helper.getWritableDatabase();
            mDaoMaster = new DaoMaster(writableDatabase) ;
        }
    }
    public DaoMaster getDaoMaster(){
        if (mDaoMaster == null){
            initDaoMaster(mDatabaseName);
        }
        return mDaoMaster ;
    }

    private Dao(){}


}
