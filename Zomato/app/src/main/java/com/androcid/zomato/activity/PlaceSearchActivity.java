package com.androcid.zomato.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.androcid.zomato.R;
import com.androcid.zomato.model.RestaurantItem;
import com.androcid.zomato.model.room.UserLocation;
import com.androcid.zomato.preference.LocationPreference;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.RestaurantResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.util.Constant;
import com.androcid.zomato.util.Toas;
import com.androcid.zomato.view.adapter.SearchPlacesAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 *
 */
public class PlaceSearchActivity extends AppCompatActivity {

    private static final String TAG = PlaceSearchActivity.class.getSimpleName();

    private Context context = PlaceSearchActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, PlaceSearchActivity.class);
        return intent;
    }


    List<RestaurantItem> searchRestaurants;
    EditText searchText;
    RecyclerView searchList;
    SearchPlacesAdapter searchRestaurantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_search);


        //SEARCH
        searchText = (EditText) findViewById(R.id.searchText);

        //SEARCH
        closeHandler = new Handler();
        closeRun = new Runnable() {

            @Override
            public void run() {
                getUserLocationSearch();
            }
        };

        searchRestaurants = new ArrayList<>();
        searchRestaurantAdapter = new SearchPlacesAdapter(context, searchRestaurants);
        searchRestaurantAdapter.setClickListener(new SearchPlacesAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {

                RestaurantItem item = searchRestaurants.get(pos);

                Intent intent = new Intent();
                intent.putExtra(Constant.ID, item.getId() + "");
                intent.putExtra(Constant.RESTAURANT_ID, item.getRestaurant_id() + "");
                intent.putExtra(Constant.NAME, item.getName());

                setResult(RESULT_OK, intent);
                finish();
            }
        });

        searchList = (RecyclerView) findViewById(R.id.searchList);
        searchList.setLayoutManager(new LinearLayoutManager(context));
        searchList.setAdapter(searchRestaurantAdapter);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void checkText(String search) {
        if (search.equals("")) {
            searchRestaurants = new ArrayList<>();
            searchRestaurantAdapter.refresh(searchRestaurants);
        } else {
            if (searchRestaurants.size() == 0) {
            }
        }
        getUserLocations(search);

    }

    private Handler closeHandler;
    private Runnable closeRun;

    private void checkQuery() {
        StopRunnable();
    }

    public void StopRunnable() {
        try {
            closeHandler.removeCallbacks(closeRun);
        } catch (Exception e) {
            // TODO: handle exception
        }

        StartRunnable();
    }

    private void StartRunnable() {
        closeHandler.postDelayed(closeRun, Constant.SPLASH_TIME_OUT);
    }

    String search;

    private void getUserLocations(String search) {
        this.search = search;
        checkQuery();
    }

    private void getUserLocationSearch() {
        UserLocation location =
                LocationPreference.getUserLocation(context);

        if (search != null /*&& !search.equals("")*/) {
            RetroInterface.getZomatoRestApi().getSearchRestaurants(
                    SessionPreference.getUserId(context) + "",
                    search,
                    location.getLatitude() + "",
                    location.getLongitude() + "",
                    new Callback<RestaurantResponse>() {
                        @Override
                        public void success(RestaurantResponse userLocationResponse, Response response) {

                            if (userLocationResponse != null) {
                                searchRestaurants = userLocationResponse.getItems();
                                searchRestaurantAdapter.refresh(searchRestaurants);

                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {
                        }
                    }
            );

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkText("");
    }

    public void closeClick(View view) {
        onBackPressed();
    }

}