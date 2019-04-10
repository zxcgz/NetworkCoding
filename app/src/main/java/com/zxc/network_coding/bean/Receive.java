package com.zxc.network_coding.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * 接受文件的JavaBean
 */
@Entity
public class Receive {
    @Id
    private int id ;
    private File file ;
    private File backupFile ;
    private boolean isBackup ;
    private int split ;
    private int[] transmit ;
    private String fileId ;
    private Date startDate ;
    private Date endDate ;
    private Date restartDate ;
}
