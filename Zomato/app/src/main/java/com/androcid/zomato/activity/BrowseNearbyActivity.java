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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androcid.zomato.BuildConfig;
import com.androcid.zomato.R;
import com.androcid.zomato.model.RestaurantItem;
import com.androcid.zomato.model.room.UserLocation;
import com.androcid.zomato.preference.LocationPreference;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.RestaurantResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.util.Constant;
import com.androcid.zomato.util.Toas;
import com.androcid.zomato.view.adapter.PlaceDetailsAdapter;
import com.androcid.zomato.view.adapter.PlaceSponsorHorizontalAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import io.codetail.animation.ViewAnimationUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.androcid.zomato.util.Constant.APP_INITIATED_REQUEST;


/**
 *
 */
public class BrowseNearbyActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = BrowseNearbyActivity.class.getSimpleName();
    //Might Like
    List<RestaurantItem> mightList;
    PlaceSponsorHorizontalAdapter mightAdapter;
    RecyclerView mightLikeList;
    //All
    List<RestaurantItem> allList;
    PlaceDetailsAdapter allAdapter;
    RecyclerView allItems;
    private Context context = BrowseNearbyActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, BrowseNearbyActivity.class);
        return intent;
    }

    //For Search
    ImageView searchIcon;
    LinearLayout search_toolbar;

    //For filter
    RelativeLayout filterLayout;

    //
    NestedScrollView nestedScrollView;
    private GoogleMap mMap;
    FloatingActionButton mFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_nearby);

        //
        nestedScrollView = (NestedScrollView) findViewById(R.id.main_layout);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_layout);
        mapFragment.getMapAsync(this);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLayout();
            }
        });

        //Filter
        filterLayout = (RelativeLayout) findViewById(R.id.filterLayout);
        filterLayout.setVisibility(View.INVISIBLE);

        //Search
        searchIcon = (ImageView) findViewById(R.id.searchIcon);
        search_toolbar = (LinearLayout) findViewById(R.id.search_toolbar);
        search_toolbar.setVisibility(View.INVISIBLE);

        //Might
        mightList = new ArrayList<>();
        mightAdapter = new PlaceSponsorHorizontalAdapter(context, mightList);
        mightAdapter.setClickListener(new PlaceSponsorHorizontalAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                gotoDetailsActivity();
            }
        });

        mightLikeList = (RecyclerView) findViewById(R.id.mightLikeList);
        mightLikeList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        mightLikeList.setAdapter(mightAdapter);


        //All
        allList = new ArrayList<>();
        allAdapter = new PlaceDetailsAdapter(context, allList);
        allAdapter.setClickListener(new PlaceDetailsAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                gotoDetailsActivity();
            }
        });

        allItems = (RecyclerView) findViewById(R.id.allItems);

        allItems.setLayoutManager(new LinearLayoutManager(context));
        allItems.setNestedScrollingEnabled(false);
        allItems.setAdapter(allAdapter);

        getRestaurants();
    }

    private void getRestaurants() {

        if (BuildConfig.NETWORK_TEST && Build.VERSION.SDK_INT >= 14) {
            try {
                TrafficStats.setThreadStatsTag(APP_INITIATED_REQUEST);
                // Once tag has been applied, network request has to be made request
            } finally {
                TrafficStats.clearThreadStatsTag();
            }
        }

        UserLocation location =
                LocationPreference.getUserLocation(context);
        RetroInterface.getZomatoRestApi().getRecommendedRestaurants(
                SessionPreference.getUserId(context)+"",
                location.getLatitude() + "",
                location.getLongitude() + "",
                new Callback<RestaurantResponse>() {
                    @Override
                    public void success(RestaurantResponse restaurantResponse, Response response) {

                        if (restaurantResponse != null && restaurantResponse.isSuccess()) {
                            mightList = restaurantResponse.getItems();
                            mightAdapter.refresh(mightList);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );
    }

    boolean isMapShown = false;
    private void showLayout() {

        if(isMapShown){
            isMapShown = false;
            nestedScrollView.setVisibility(View.VISIBLE);
            Animator iconAnim = ObjectAnimator.ofPropertyValuesHolder(nestedScrollView,
                    PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, nestedScrollView.getHeight(), 0f));
            iconAnim.setDuration(Constant.REVEAL_DURATION);
            iconAnim.start();
        }else{
            isMapShown = true;
            nestedScrollView.setVisibility(View.VISIBLE);
            Animator iconAnim = ObjectAnimator.ofPropertyValuesHolder(nestedScrollView,
                    PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, nestedScrollView.getHeight()));
            iconAnim.setDuration(Constant.REVEAL_DURATION);
            iconAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    nestedScrollView.setVisibility(View.INVISIBLE);
                }
            });
            iconAnim.start();
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void gotoDetailsActivity() {
        startActivity(PlaceDetailActivity.getCallIntent(context, null));
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (searchOpened) {
            searchOpened = false;
            exitReveal(searchIcon, search_toolbar);
        }

    }

    //ALL CLICK HANDLERS

    /**
     * Open the select Location Activity
     *
     * @param view
     */
    public void selectLocationClick(View view) {

    }

    public void searchClick(View view) {
        enterReveal(view, search_toolbar);
    }

    public void filterClick(View view) {
        showFilter(filterLayout);
    }

    public void closeClick(View view) {
        finish();
    }

    //For Search
    boolean searchOpened;

    private void exitReveal(final View icon, final View toolbar) {

        // get the center for the clipping circle
        int cx = getRelativeLeft(icon) + icon.getMeasuredWidth() / 2;
        int cy = getRelativeTop(icon);

        // get the initial radius for the clipping circle
        int initialRadius = Math.max(toolbar.getWidth(), toolbar.getHeight());

        // create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(toolbar, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                toolbar.setVisibility(View.INVISIBLE);
            }
        });

        anim.setDuration(Constant.SEARCH_REVEAL_DURATION);
        // start the animation
        anim.start();
    }

    private void enterReveal(View icon, View toolbar) {

        searchOpened = true;

        // get the center for the clipping circle
        int cx = getRelativeLeft(icon) + icon.getMeasuredWidth() / 2;
        int cy = getRelativeTop(icon);

        // get the final radius for the clipping circle
        int finalRadius = Math.max(toolbar.getWidth(), toolbar.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(toolbar, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        toolbar.setVisibility(View.VISIBLE);
        anim.setDuration(Constant.SEARCH_REVEAL_DURATION);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                startActivity(SearchPlaceActivity.getCallIntent(context));
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        anim.start();
    }

    private int getRelativeLeft(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getLeft();
        else
            return myView.getLeft() + getRelativeLeft((View) myView.getParent());
    }

    private int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }

    //For Filter

    private void showFilter(View view) {

        view.setVisibility(View.VISIBLE);
        Animator iconAnim = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, view.getHeight(), 0f));
        iconAnim.setDuration(Constant.REVEAL_DURATION);
        iconAnim.start();
    }

    private void hideFilter(final View view) {

        view.setVisibility(View.VISIBLE);
        Animator iconAnim = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, view.getHeight()));
        iconAnim.setDuration(Constant.REVEAL_DURATION);
        iconAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });
        iconAnim.start();
    }

    public void quickFilterClick(View view) {

    }

    public void closeFilter(View view) {
        hideFilter(filterLayout);
    }

    public void applyFilterClick(View view) {
        hideFilter(filterLayout);
    }

    public void sortByClick(View view) {

    }

    public void cuisineClick(View view) {
    }

    public void filterTypeClick(View view) {
    }

    public void offerClick(View view) {
    }

    public void costClick(View view) {
    }

    public void moreFilterClick(View view) {
    }
}