package com.androcid.zomato.activity.photos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.androcid.zomato.R;
import com.androcid.zomato.model.RestaurantImage;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.RestaurantImageResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.util.Constant;
import com.androcid.zomato.view.adapter.PhotosAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 *
 */
public class AllPhotosActivity extends AppCompatActivity {

    private static final String TAG = AllPhotosActivity.class.getSimpleName();
    ViewPager viewPager;
    private Context context = AllPhotosActivity.this;

    public static Intent getCallIntent(Context context, String restaurant_branch_id) {
        Intent intent = new Intent(context, AllPhotosActivity.class);
        intent.putExtra(Constant.RESTAURANT_BRANCH_ID, restaurant_branch_id);
        return intent;
    }

    Toolbar toolbar;
    List<RestaurantImage> allList;
    PhotosAdapter adapter;
    RecyclerView recyclerView;

    String restaurant_branch_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_photos);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Photos");
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));

        restaurant_branch_id = getIntent().getStringExtra(Constant.RESTAURANT_BRANCH_ID);

        allList = new ArrayList<>();
        adapter = new PhotosAdapter(context, allList);
        adapter.setClickListener(new PhotosAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                //gotoDetailsActivity();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.setAdapter(adapter);

        getAllPhotos();

    }

    private void getAllPhotos() {

        RetroInterface.getZomatoRestApi().getRestaurantPhotos(
                SessionPreference.getUserId(context) + "",
                restaurant_branch_id,
                new Callback<RestaurantImageResponse>() {
                    @Override
                    public void success(RestaurantImageResponse restaurantImageResponse, Response response) {

                        if(restaurantImageResponse!=null){
                            allList = restaurantImageResponse.getItems();
                            adapter.refresh(allList);
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

    @Override
    protected void onResume() {
        super.onResume();
    }
}