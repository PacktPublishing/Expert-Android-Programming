package com.androcid.zomato.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.androcid.zomato.R;
import com.androcid.zomato.model.RestaurantItem;
import com.androcid.zomato.model.room.UserLocation;
import com.androcid.zomato.preference.LocationPreference;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.RestaurantResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.view.adapter.BookmarkAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class BookmarkActivity extends AppCompatActivity {

    private static final String TAG = BookmarkActivity.class.getSimpleName();
    private Context context = BookmarkActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, BookmarkActivity.class);
        return intent;
    }

    RecyclerView recyclerView;
    List<RestaurantItem> allList;
    BookmarkAdapter allAdapter;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Bookmarks");
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        allList = new ArrayList<>();
        allAdapter = new BookmarkAdapter(context, allList);
        allAdapter.setClickListener(new BookmarkAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                gotoDetailsActivity(pos);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(allAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getBookmarkedRestaurants();
    }

    private void getBookmarkedRestaurants() {

        UserLocation location = LocationPreference.getUserLocation(context);
        RetroInterface.getZomatoRestApi().getBookmarkedRestaurants(
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

    private void gotoDetailsActivity(int pos) {

        RestaurantItem item = allList.get(pos);
        startActivity(PlaceDetailActivity.getCallIntent(context, item));

    }

}
