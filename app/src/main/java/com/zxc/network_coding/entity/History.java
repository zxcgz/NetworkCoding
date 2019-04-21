package com.zxc.network_coding.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.zxc.network_coding.dao.DaoSession;
import com.zxc.network_coding.dao.ReceiveDao;
import com.zxc.network_coding.dao.SendDao;
import com.zxc.network_coding.dao.HistoryDao;

/**
 * 成功的历史记录的JavaBean
 */
@Entity
public class History {
    @Id(autoincrement = true)
    private Long id ;
    private Long sendId ;
    @ToOne(joinProperty = "sendId")
    private Send sendFile ;
    private Long receiveId ;
    @ToOne(joinProperty = "receiveId")
    private Receive receiveFile ;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1462128466)
    private transient HistoryDao myDao;
    @Generated(hash = 185077410)
    public History(Long id, Long sendId, Long receiveId) {
        this.id = id;
        this.sendId = sendId;
        this.receiveId = receiveId;
    }
    @Generated(hash = 869423138)
    public History() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getSendId() {
        return this.sendId;
    }
    public void setSendId(Long sendId) {
        this.sendId = sendId;
    }
    public Long getReceiveId() {
        return this.receiveId;
    }
    public void setReceiveId(Long receiveId) {
        this.receiveId = receiveId;
    }
    @Generated(hash = 1017783501)
    private transient Long sendFile__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2040295955)
    public Send getSendFile() {
        Long __key = this.sendId;
        if (sendFile__resolvedKey == null || !sendFile__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SendDao targetDao = daoSession.getSendDao();
            Send sendFileNew = targetDao.load(__key);
            synchronized (this) {
                sendFile = sendFileNew;
                sendFile__resolvedKey = __key;
            }
        }
        return sendFile;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 611798849)
    public void setSendFile(Send sendFile) {
        synchronized (this) {
            this.sendFile = sendFile;
            sendId = sendFile == null ? null : sendFile.getId();
            sendFile__resolvedKey = sendId;
        }
    }
    @Generated(hash = 1860538846)
    private transient Long receiveFile__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2119892080)
    public Receive getReceiveFile() {
        Long __key = this.receiveId;
        if (receiveFile__resolvedKey == null
                || !receiveFile__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ReceiveDao targetDao = daoSession.getReceiveDao();
            Receive receiveFileNew = targetDao.load(__key);
            synchronized (this) {
                receiveFile = receiveFileNew;
                receiveFile__resolvedKey = __key;
            }
        }
        return receiveFile;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 520138422)
    public void setReceiveFile(Receive receiveFile) {
        synchronized (this) {
            this.receiveFile = receiveFile;
            receiveId = receiveFile == null ? null : receiveFile.getId();
            receiveFile__resolvedKey = receiveId;
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
    @Generated(hash = 851899508)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getHistoryDao() : null;
    }
}
