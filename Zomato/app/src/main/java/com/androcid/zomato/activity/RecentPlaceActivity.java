package com.androcid.zomato.activity;

import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.androcid.zomato.BuildConfig;
import com.androcid.zomato.R;
import com.androcid.zomato.model.RestaurantItem;
import com.androcid.zomato.model.room.UserLocation;
import com.androcid.zomato.preference.LocationPreference;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.RestaurantResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.view.adapter.RecentViewedPlacesAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.androcid.zomato.util.Constant.APP_INITIATED_REQUEST;


public class RecentPlaceActivity extends AppCompatActivity {

    private static final String TAG = RecentPlaceActivity.class.getSimpleName();
    private Context context = RecentPlaceActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, RecentPlaceActivity.class);
        return intent;
    }

    RecyclerView recyclerView;
    List<RestaurantItem> allList;
    RecentViewedPlacesAdapter allAdapter;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_place);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Recently Viewed Restaurants");
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        allList = new ArrayList<>();
        allAdapter = new RecentViewedPlacesAdapter(context, allList);
        allAdapter.setClickListener(new RecentViewedPlacesAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                gotoDetailsActivity(pos);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(allAdapter);

        getRecentlyViewed();
    }

    private void getRecentlyViewed() {

        if (BuildConfig.NETWORK_TEST && Build.VERSION.SDK_INT >= 14) {
            try {
                TrafficStats.setThreadStatsTag(APP_INITIATED_REQUEST);
                // Once tag has been applied, network request has to be made request
            } finally {
                TrafficStats.clearThreadStatsTag();
            }
        }

        UserLocation location = LocationPreference.getUserLocation(context);

        RetroInterface.getZomatoRestApi().getRecentRestaurants(
                SessionPreference.getUserId(context) + "",
                location.getLatitude() + "",
                location.getLongitude() + "",
                new Callback<RestaurantResponse>() {
                    @Override
                    public void success(RestaurantResponse restaurantResponse, Response response) {
                        if (restaurantResponse != null && restaurantResponse.isSuccess()) {
                            allList = restaurantResponse.getItems();
                            allAdapter.refresh(allList);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoDetailsActivity(int pos) {

        RestaurantItem item = allList.get(pos);
        startActivity(PlaceDetailActivity.getCallIntent(context, item));

    }

}
