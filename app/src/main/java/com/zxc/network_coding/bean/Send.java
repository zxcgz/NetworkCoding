package com.zxc.network_coding.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;

/**
 * 发送文件的JavaBean
 */
@Entity
public class Send {

    //主键
    @Id
    private int id ;
    //状态:传送中、处理中、传输完成等
    private int status ;
    @ToOne(joinProperty = "fileId")
    private File file ;
    //文件是否备份
    private boolean isBackup ;
    @ToOne(joinProperty = "fileId")
    private File backupFile ;
    //文件分割数
    private int split ;
    //传输成功的片
    private int[] transmit ;
    //文件的唯一标识码，用于接收方接受文件
    private String fileId ;
    //开始时间
    private Date startDate ;
    //结束时间
    private Date endDate ;
    //重启时间
    private Date restartDate ;
}
