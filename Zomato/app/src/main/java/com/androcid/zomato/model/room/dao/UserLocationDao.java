package com.androcid.zomato.model.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.androcid.zomato.model.room.UserLocation;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserLocationDao {

    @Query("select * from UserLocation")
    LiveData<List<UserLocation>> getAllUserLocations();

    @Query("select * from UserLocation where name like :name order by name")
    LiveData<List<UserLocation>> getUserLocationsByName(String name);

    @Insert(onConflict = REPLACE)
    void addUserLocations(List<UserLocation> userLocations);

    @Delete
    void deleteUserLocation(UserLocation userLocation);

}
