package com.zxc.network_coding.entity;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.zxc.network_coding.dao.DaoSession;
import com.zxc.network_coding.dao.FileDao;
import com.zxc.network_coding.dao.ReceiveDao;

/**
 * 接受文件的JavaBean
 */
@Entity
public class Receive {
    @Id(autoincrement = true)
    private Long id ;
    private Long fileId ;
    private Long backupFileId ;
    @ToOne(joinProperty = "fileId")
    private File file ;
    @ToOne(joinProperty = "backupFileId")
    private File backupFile ;
    private boolean isBackup ;
    private int split ;
    @Convert(converter = Converter.TransmitConverter.class,columnType = byte[].class)
    private int[] transmit ;
    private String fileIdCode ;
    private Date startDate ;
    private Date endDate ;
    private Date restartDate ;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 685332456)
    private transient ReceiveDao myDao;
    @Generated(hash = 1393961460)
    public Receive(Long id, Long fileId, Long backupFileId, boolean isBackup, int split,
            int[] transmit, String fileIdCode, Date startDate, Date endDate,
            Date restartDate) {
        this.id = id;
        this.fileId = fileId;
        this.backupFileId = backupFileId;
        this.isBackup = isBackup;
        this.split = split;
        this.transmit = transmit;
        this.fileIdCode = fileIdCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.restartDate = restartDate;
    }
    @Generated(hash = 1172216053)
    public Receive() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getFileId() {
        return this.fileId;
    }
    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
    public Long getBackupFileId() {
        return this.backupFileId;
    }
    public void setBackupFileId(Long backupFileId) {
        this.backupFileId = backupFileId;
    }
    public boolean getIsBackup() {
        return this.isBackup;
    }
    public void setIsBackup(boolean isBackup) {
        this.isBackup = isBackup;
    }
    public int getSplit() {
        return this.split;
    }
    public void setSplit(int split) {
        this.split = split;
    }
    public int[] getTransmit() {
        return this.transmit;
    }
    public void setTransmit(int[] transmit) {
        this.transmit = transmit;
    }
    public String getFileIdCode() {
        return this.fileIdCode;
    }
    public void setFileIdCode(String fileIdCode) {
        this.fileIdCode = fileIdCode;
    }
    public Date getStartDate() {
        return this.startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return this.endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Date getRestartDate() {
        return this.restartDate;
    }
    public void setRestartDate(Date restartDate) {
        this.restartDate = restartDate;
    }
    @Generated(hash = 1893111882)
    private transient Long file__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1906988392)
    public File getFile() {
        Long __key = this.fileId;
        if (file__resolvedKey == null || !file__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FileDao targetDao = daoSession.getFileDao();
            File fileNew = targetDao.load(__key);
            synchronized (this) {
                file = fileNew;
                file__resolvedKey = __key;
            }
        }
        return file;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 881001165)
    public void setFile(File file) {
        synchronized (this) {
            this.file = file;
            fileId = file == null ? null : file.getId();
            file__resolvedKey = fileId;
        }
    }
    @Generated(hash = 1182984338)
    private transient Long backupFile__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 572569875)
    public File getBackupFile() {
        Long __key = this.backupFileId;
        if (backupFile__resolvedKey == null || !backupFile__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FileDao targetDao = daoSession.getFileDao();
            File backupFileNew = targetDao.load(__key);
            synchronized (this) {
                backupFile = backupFileNew;
                backupFile__resolvedKey = __key;
            }
        }
        return backupFile;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 826031176)
    public void setBackupFile(File backupFile) {
        synchronized (this) {
            this.backupFile = backupFile;
            backupFileId = backupFile == null ? null : backupFile.getId();
            backupFile__resolvedKey = backupFileId;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1802735035)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getReceiveDao() : null;
    }

}
