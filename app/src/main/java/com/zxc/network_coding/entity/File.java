package com.zxc.network_coding.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 存储文件信息
 */
@Entity
public class File {
    @Id(autoincrement = true)
    private Long id ;
    private String fileName ;
    private String filePath ;
    private boolean isBackup ;
    private String md5 ;
    private String hash ;
    private int tag ;
    private long size ;
    @Generated(hash = 1636470269)
    public File(Long id, String fileName, String filePath, boolean isBackup,
            String md5, String hash, int tag, long size) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.isBackup = isBackup;
        this.md5 = md5;
        this.hash = hash;
        this.tag = tag;
        this.size = size;
    }
    @Generated(hash = 375897388)
    public File() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFileName() {
        return this.fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFilePath() {
        return this.filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public boolean getIsBackup() {
        return this.isBackup;
    }
    public void setIsBackup(boolean isBackup) {
        this.isBackup = isBackup;
    }
    public String getMd5() {
        return this.md5;
    }
    public void setMd5(String md5) {
        this.md5 = md5;
    }
    public String getHash() {
        return this.hash;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }
    public int getTag() {
        return this.tag;
    }
    public void setTag(int tag) {
        this.tag = tag;
    }
    public long getSize() {
        return this.size;
    }
    public void setSize(long size) {
        this.size = size;
    }
}
