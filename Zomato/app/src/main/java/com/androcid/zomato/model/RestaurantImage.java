package com.androcid.zomato.model;

import com.androcid.zomato.dao.DaoSession;
import com.androcid.zomato.dao.classes.RestaurantImageDao;
import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import de.greenrobot.dao.DaoException;

/**
 * Created by Androcid on 18-01-2017.
 */
public class RestaurantImage implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName(Constant.ID)
    Long id;
    @SerializedName(Constant.RESTAURANT_ID)
    String restaurant_id;
    @SerializedName(Constant.RESTAURANT_BRANCH_ID)
    String restaurant_branch_id;
    @SerializedName(Constant.USER_ID)
    String user_id;
    @SerializedName(Constant.IMAGE)
    String image;
    @SerializedName(Constant.TAGS)
    String tags;

    @SerializedName(Constant.RESTAURANT)
    RestaurantItem restaurant;

    public RestaurantImage(Long id, String restaurant_id, String restaurant_branch_id, String image) {
        this.id = id;
        this.restaurant_id = restaurant_id;
        this.restaurant_branch_id = restaurant_branch_id;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public String getRestaurant_branch_id() {
        return restaurant_branch_id;
    }

    public String getImage() {
        return image;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public void setRestaurant_branch_id(String restaurant_branch_id) {
        this.restaurant_branch_id = restaurant_branch_id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public RestaurantItem getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantItem restaurant) {
        this.restaurant = restaurant;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    //FOR DAO
    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient RestaurantImageDao myDao;

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRestaurantImageDao() : null;
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
