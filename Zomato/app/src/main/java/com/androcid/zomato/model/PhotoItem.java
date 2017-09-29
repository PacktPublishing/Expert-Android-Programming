package com.androcid.zomato.model;

import com.androcid.zomato.dao.DaoSession;
import com.androcid.zomato.dao.classes.PhotoItemDao;

import java.io.Serializable;

import de.greenrobot.dao.DaoException;

/**
 *
 */
public class PhotoItem implements Serializable {
    private static final long serialVersionUID = 1L;

    Long id;

    String _id;
    String name;
    String path;
    String tag;
    boolean status;

    boolean selected;

    public PhotoItem(Long id, String _id, String name, String path, String tag, boolean status) {
        this.id = id;
        this._id = _id;
        this.name = name;
        this.path = path;
        this.tag = tag;
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    //FOR DAO
    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient PhotoItemDao myDao;

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPhotoItemDao() : null;
    }

    /** Convenient call for {@link de.greenrobot.dao.AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /** Convenient call for {@link de.greenrobot.dao.AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** Convenient call for {@link de.greenrobot.dao.AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

}

