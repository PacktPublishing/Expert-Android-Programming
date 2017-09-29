package com.androcid.zomato.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androcid.zomato.BuildConfig;
import com.androcid.zomato.R;
import com.androcid.zomato.model.CuisineItem;
import com.androcid.zomato.model.RestaurantItem;
import com.androcid.zomato.model.room.UserLocation;
import com.androcid.zomato.preference.LocationPreference;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.CuisineResponse;
import com.androcid.zomato.retro.RestaurantResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.util.Constant;
import com.androcid.zomato.util.Toas;
import com.androcid.zomato.view.adapter.CuisineHorizontalAdapter;
import com.androcid.zomato.view.adapter.HomePlacesAdapter;
import com.androcid.zomato.view.adapter.RecentPlacesAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.androcid.zomato.util.Constant.USER_INITIATED_REQUEST;

/**
 *
 */
public class SearchPlaceActivity extends AppCompatActivity {

    private static final String TAG = SearchPlaceActivity.class.getSimpleName();
    private static final long VIEW_ANIMATION = 300;


    //FOR CUISINE LIST
    List<CuisineItem> cuisines;
    CuisineHorizontalAdapter cuisineAdapter;
    RecyclerView cuisineList;

    //All
    List<String> allList;
    RecentPlacesAdapter allAdapter;
    RecyclerView allItems;
    private Context context = SearchPlaceActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, SearchPlaceActivity.class);
        return intent;
    }

    LinearLayout searchViewLay;
    LinearLayout mightLike;

    List<RestaurantItem> searchRestaurants;
    RelativeLayout searchView;
    TextView userHint;
    EditText searchText;
    RecyclerView searchList;
    HomePlacesAdapter searchRestaurantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_place);

        searchViewLay = (LinearLayout) findViewById(R.id.searchViewLay);
        mightLike = (LinearLayout) findViewById(R.id.mightLike);

        //Might
        cuisines = new ArrayList<>();
        cuisineAdapter = new CuisineHorizontalAdapter(context, cuisines);
        cuisineAdapter.setClickListener(new CuisineHorizontalAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                //gotoDetailsActivity();
            }
        });

        cuisineList = (RecyclerView) findViewById(R.id.cuisineList);
        cuisineList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        cuisineList.setAdapter(cuisineAdapter);


        //All
        allList = new ArrayList<>();
        allAdapter = new RecentPlacesAdapter(context, allList);
        allAdapter.setClickListener(new RecentPlacesAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                gotoDetailsActivity();
            }
        });

        allItems = (RecyclerView) findViewById(R.id.allItems);

        allItems.setLayoutManager(new LinearLayoutManager(context));
        allItems.setNestedScrollingEnabled(false);
        allItems.setAdapter(allAdapter);

        searchViewLay.post(new Runnable() {
            @Override
            public void run() {
                enterViews();
            }
        });

        //SEARCH
        searchView = (RelativeLayout) findViewById(R.id.searchView);
        searchView.setVisibility(View.INVISIBLE);
        userHint = (TextView) findViewById(R.id.userHint);
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
        searchRestaurantAdapter = new HomePlacesAdapter(context, searchRestaurants);
        searchRestaurantAdapter.setClickListener(new HomePlacesAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {

                RestaurantItem restaurantItem = searchRestaurants.get(pos);
                startActivity(PlaceDetailActivity.getCallIntent(context, restaurantItem));

                /*UserLocation location = searchRestaurants.get(pos);
                LocationPreference.setLocationParams(context, location);
                finish();*/
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
            userHint.setText("Start Tying");
            searchRestaurants = new ArrayList<>();
            searchRestaurantAdapter.refresh(searchRestaurants);
        } else {
            if (searchRestaurants.size() == 0) {
                userHint.setText("Fetching List");
            }
            getUserLocations(search);
        }

        if (search.length() > 0) {
            searchView.setVisibility(View.VISIBLE);
        } else {
            searchView.setVisibility(View.INVISIBLE);
        }

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

        if (BuildConfig.NETWORK_TEST && Build.VERSION.SDK_INT >= 14) {
            try {
                TrafficStats.setThreadStatsTag(USER_INITIATED_REQUEST);
                // Once tag has been applied, network request has to be made request
            } finally {
                TrafficStats.clearThreadStatsTag();
            }
        }

        UserLocation location =
                LocationPreference.getUserLocation(context);

        if (search != null && !search.equals("")) {

            if (BuildConfig.NETWORK_TEST && Build.VERSION.SDK_INT >= 14) {
                try {
                    TrafficStats.setThreadStatsTag(USER_INITIATED_REQUEST);
                    // Once tag has been applied, network request has to be made request
                } finally {
                    TrafficStats.clearThreadStatsTag();
                }
            }

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

                                if (searchRestaurants.size() != 0) {
                                    userHint.setText("");
                                } else {
                                    userHint.setText("No Users Found");
                                }
                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            userHint.setText("No Users Found");
                        }
                    }
            );

        }

    }


    private void gotoDetailsActivity() {
        onBackPressed();
    }

    private void setList() {
        for (int i = 0; i < 18; i++) {
            allList.add("" + i);
        }
        allAdapter.refresh(allList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCuisines();
    }


    private void getCuisines() {

        RetroInterface.getZomatoRestApi().getAllCuisine(
                "",
                new Callback<CuisineResponse>() {
                    @Override
                    public void success(CuisineResponse cuisineResponse, Response response) {

                        if (cuisineResponse != null && cuisineResponse.isSuccess()) {

                            cuisines = cuisineResponse.getItems();
                            cuisineAdapter.refresh(cuisines);

                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );

    }

    //ALL CLICK HANDLERS

    /**
     * Open the select Location Activity
     *
     * @param view
     */
    public void selectLocationClick(View view) {
        startActivity(SelectLocationActivity.getCallIntent(context));
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        //Animate Exit
        exitViews();

    }

    public void closeClick(View view) {
        onBackPressed();
    }

    //ANIMATIONS
    private void enterViews() {
        showTop(searchViewLay);
        showBottom(mightLike, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

            }
        });
    }

    private void showTop(View view) {
        view.setVisibility(View.VISIBLE);
        Animator iconAnim = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -view.getHeight(), 0f));
        iconAnim.setDuration(VIEW_ANIMATION);
        iconAnim.start();


    }

    private void showBottom(View view, Animator.AnimatorListener listener) {

        view.setVisibility(View.VISIBLE);
        Animator iconAnim = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, view.getHeight(), 0f));
        iconAnim.setDuration(VIEW_ANIMATION);
        iconAnim.addListener(listener);
        iconAnim.start();
    }

    //EXIT
    private void exitViews() {
        hideTop(searchViewLay);
        hideBottom(mightLike, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(0, 0);
            }
        }, 50);

    }

    private void hideTop(final View view) {
        view.setVisibility(View.VISIBLE);
        Animator iconAnim = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, -view.getHeight()),
                PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f));
        iconAnim.setDuration(VIEW_ANIMATION);
        iconAnim.start();
    }

    private void hideBottom(final View view, Animator.AnimatorListener listener) {
        view.setVisibility(View.VISIBLE);
        Animator iconAnim = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, view.getHeight()),
                PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f));
        iconAnim.setDuration(VIEW_ANIMATION);
        iconAnim.addListener(listener);
        iconAnim.start();
    }


}