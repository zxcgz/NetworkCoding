package com.zxc.network_coding.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

/**
 * 存储文件信息
 */
@Entity
public class File {
    @Id
    private int id ;
    private String fileName ;
    private String filePath ;
    private boolean isBackup ;
    private String md5 ;
    private String hash ;
    private int tag ;
}
