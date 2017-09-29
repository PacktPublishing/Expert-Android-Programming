package com.androcid.zomato.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.androcid.zomato.dao.classes.PhotoItemDao;
import com.androcid.zomato.dao.classes.RestaurantImageDao;
import com.androcid.zomato.model.PhotoItem;
import com.androcid.zomato.model.RestaurantImage;
import com.androcid.zomato.preference.SessionPreference;

import java.util.List;

public class DaoFunctions {
	
	private static final String TAG = DaoFunctions.class.getSimpleName();
    private Context context;

	private DaoMaster daoMaster;
	private DaoSession daoSession;
    private SQLiteDatabase db;
    private DaoMaster.DevOpenHelper helper;

    private RestaurantImageDao restaurantImageDao;
    private PhotoItemDao photoItemDao;

    String userId;

    public DaoFunctions(Context context) {
		this.context = context;
		helper = new DaoMaster.DevOpenHelper(context, Dao.DATABASE, null);
        userId = SessionPreference.getUserId(context)+"";
	}	
		
	public void close(){
		helper.close();		 
	}
			
	private void closeDao(){
		 db.close();
		 daoSession.clear();		 
	}

	private void initDao() {

		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();

        restaurantImageDao = daoSession.getRestaurantImageDao();
        photoItemDao = daoSession.getPhotoItemDao();
	}

    //TODO CATEGORY ITEM DAO
    public void updateRestaurantImages(List<RestaurantImage> list) {
        initDao();
        restaurantImageDao.insertOrReplaceInTx(list);
        closeDao();
    }

    public List<RestaurantImage> getRestaurantImages() {
        List<RestaurantImage> dataList;
        initDao();
        dataList = restaurantImageDao.queryBuilder()
                .list();
        closeDao();
        return dataList;
    }

    public void deleteRestaurantImages() {
        initDao();
        restaurantImageDao.deleteAll();
        closeDao();
    }

    //TODO CATEGORY ITEM DAO
    public void updatePhotoItems(List<PhotoItem> list) {
        initDao();
        photoItemDao.insertOrReplaceInTx(list);
        closeDao();
    }

    public void updatePhotoItem(PhotoItem item) {
        initDao();
        photoItemDao.insertOrReplaceInTx(item);
        closeDao();
    }

    public List<PhotoItem> getPhotoItems() {
        List<PhotoItem> dataList;
        initDao();
        dataList = photoItemDao.queryBuilder()
                .list();
        closeDao();
        return dataList;
    }

    public List<PhotoItem> getRemainingPhotoItems() {
        List<PhotoItem> dataList;
        initDao();
        dataList = photoItemDao.queryBuilder()
                .where(PhotoItemDao.Properties.Status.eq(0))
                .list();
        closeDao();
        return dataList;
    }

    public void deletePhotoItem() {
        initDao();
        photoItemDao.deleteAll();
        closeDao();
    }

}