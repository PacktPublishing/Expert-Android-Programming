package com.androcid.zomato.activity.photos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.androcid.zomato.R;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.RestaurantImageResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.util.Constant;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 *
 */
public class AllMenuActivity extends AppCompatActivity {

    private static final String TAG = AllMenuActivity.class.getSimpleName();
    ViewPager viewPager;
    private Context context = AllMenuActivity.this;

    public static Intent getCallIntent(Context context, String restaurant_branch_id) {
        Intent intent = new Intent(context, AllMenuActivity.class);
        intent.putExtra(Constant.RESTAURANT_BRANCH_ID, restaurant_branch_id);
        return intent;
    }

    Toolbar toolbar;
    String restaurant_branch_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_menu);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Menus");
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));

        restaurant_branch_id = getIntent().getStringExtra(Constant.RESTAURANT_BRANCH_ID);

        getRestaurantMenu();

    }

    private void getRestaurantMenu() {

        RetroInterface.getZomatoRestApi().getRestaurantMenu(
                SessionPreference.getUserId(context) + "",
                restaurant_branch_id,
                new Callback<RestaurantImageResponse>() {
                    @Override
                    public void success(RestaurantImageResponse restaurantImageResponse, Response response) {

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