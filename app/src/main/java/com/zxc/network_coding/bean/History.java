package com.zxc.network_coding.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;

/**
 * 成功的历史记录的JavaBean
 */
@Entity
public class History {
    @Id
    private int id ;
    @ToOne(joinProperty = "sendId")
    private Send sendFile ;
    @ToOne(joinProperty = "receiveId")
    private Receive receiveFile ;
}
