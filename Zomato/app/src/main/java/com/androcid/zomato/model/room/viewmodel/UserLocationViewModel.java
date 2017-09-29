package com.androcid.zomato.model.room.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.androcid.zomato.model.room.AppDatabase;
import com.androcid.zomato.model.room.UserLocation;

import java.util.List;


public class UserLocationViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;
    public UserLocationViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabase(this.getApplication());
    }

    public LiveData<List<UserLocation>> getUserLocationsByName(String name) {
        return appDatabase.getUserLocationDao().getUserLocationsByName(name);
    }

    public void addUserLocations(final List<UserLocation> userLocations) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.getUserLocationDao().addUserLocations(userLocations);
            }
        }).start();

    }

}
