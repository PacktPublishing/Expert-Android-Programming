package com.androcid.zomato.activity.collection;

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
import com.androcid.zomato.model.CollectionItem;
import com.androcid.zomato.model.RestaurantItem;
import com.androcid.zomato.model.room.UserLocation;
import com.androcid.zomato.preference.LocationPreference;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.NormalResponse;
import com.androcid.zomato.retro.RestaurantResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.util.CommonFunctions;
import com.androcid.zomato.util.Constant;
import com.androcid.zomato.util.Toas;
import com.androcid.zomato.view.adapter.AddPlacesAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.androcid.zomato.util.Constant.APP_INITIATED_REQUEST;


public class AddPlaceToCollectionActivity extends AppCompatActivity {

    private static final String TAG = AddPlaceToCollectionActivity.class.getSimpleName();
    private Context context = AddPlaceToCollectionActivity.this;

    public static Intent getCallIntent(Context context, CollectionItem item) {
        Intent intent = new Intent(context, AddPlaceToCollectionActivity.class);
        intent.putExtra(Constant.DATA, item);
        return intent;
    }

    RecyclerView recyclerView;
    List<RestaurantItem> allList;
    List<String> selectedRestaurants;
    AddPlacesAdapter allAdapter;

    Toolbar toolbar;

    //FINAL ITEM
    CollectionItem item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place_collection);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Place to Collection");
        getSupportActionBar().setSubtitle("Place");
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));

        item = (CollectionItem) getIntent().getSerializableExtra(Constant.DATA);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        allList = new ArrayList<>();
        selectedRestaurants = new ArrayList<>();
        allAdapter = new AddPlacesAdapter(context, allList);
        allAdapter.setClickListener(new AddPlacesAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {

                String id = allList.get(pos).getId() + "";

                if (selectedRestaurants.contains(id)) {
                    selectedRestaurants.remove(id);
                } else {
                    selectedRestaurants.add(id);
                }

                refreshList();

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(allAdapter);

        getRestaurantList();
    }

    private void refreshList() {

        for (int i = 0; i < allList.size(); i++) {

            String id = allList.get(i).getId() + "";
            if (selectedRestaurants.contains(id)) {
                allList.get(i).setSelected(true);
            } else {
                allList.get(i).setSelected(false);
            }
        }

        allAdapter.refresh(allList);

    }

    private void getRestaurantList() {
        if (BuildConfig.NETWORK_TEST && Build.VERSION.SDK_INT >= 14) {
            try {
                TrafficStats.setThreadStatsTag(APP_INITIATED_REQUEST);
                // Once tag has been applied, network request has to be made request
            } finally {
                TrafficStats.clearThreadStatsTag();
            }
        }

        UserLocation location = LocationPreference.getUserLocation(context);

        RetroInterface.getZomatoRestApi().getAllRestaurants(
                SessionPreference.getUserId(context) + "",
                location.getLatitude() + "",
                location.getLongitude() + "",
                new Callback<RestaurantResponse>() {
                    @Override
                    public void success(RestaurantResponse restaurantResponse, Response response) {

                        if (restaurantResponse != null && restaurantResponse.isSuccess()) {
                            allList = restaurantResponse.getItems();
                            //allAdapter.refresh(allList);

                            refreshList();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }

        );

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

    public void addToCollection(View view) {

        if (selectedRestaurants.size() != 0) {
            item.setRestaurants(selectedRestaurants);

            RetroInterface.getZomatoRestApi().createCollection(
                    CommonFunctions.getHashMap(item),
                    new Callback<NormalResponse>() {
                        @Override
                        public void success(NormalResponse normalResponse, Response response) {

                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    }
            );

        } else {

            Toas.show(context, "Select atleast one Restaurant");
        }

    }
}
