package com.androcid.zomato.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androcid.zomato.BuildConfig;
import com.androcid.zomato.R;
import com.androcid.zomato.activity.base.HomeBaseActivity;
import com.androcid.zomato.model.MealDetailItem;
import com.androcid.zomato.model.MealTypeItem;
import com.androcid.zomato.model.PlaceDisplayItem;
import com.androcid.zomato.model.RestaurantItem;
import com.androcid.zomato.model.room.UserLocation;
import com.androcid.zomato.preference.LocationPreference;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.MealDetailResponse;
import com.androcid.zomato.retro.RestaurantResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.util.Constant;
import com.androcid.zomato.view.adapter.PlaceHorizontalAdapter;
import com.androcid.zomato.view.adapter.PlaceVerticalAdapter;

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
public class HomeActivity extends HomeBaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private Context context = HomeActivity.this;

    //Might Like
    List<RestaurantItem> mightList;
    PlaceHorizontalAdapter mightAdapter;
    RecyclerView mightLikeList;

    //All
    List<PlaceDisplayItem> allList;
    PlaceVerticalAdapter allAdapter;
    RecyclerView allItems;

    LinearLayout browseNearby;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        return intent;
    }

    //For Search
    ImageView searchIcon;
    LinearLayout search_toolbar;

    //Toolbar
    TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        title = (TextView) findViewById(R.id.title);
        if (LocationPreference.hasLocation(context)) {
            UserLocation location = LocationPreference.getUserLocation(context);
            title.setText(location.getName());
        }

        browseNearby = (LinearLayout) findViewById(R.id.browseNearby);
        browseNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BrowseNearbyActivity.getCallIntent(context));
            }
        });

        //Search
        searchIcon = (ImageView) findViewById(R.id.searchIcon);
        search_toolbar = (LinearLayout) findViewById(R.id.search_toolbar);
        search_toolbar.setVisibility(View.INVISIBLE);

        //Might
        mightList = new ArrayList<>();
        mightAdapter = new PlaceHorizontalAdapter(context, mightList);
        mightAdapter.setClickListener(new PlaceHorizontalAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                gotoDetailsActivity(mightList.get(pos));
            }
        });

        mightLikeList = (RecyclerView) findViewById(R.id.mightLikeList);
        mightLikeList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        mightLikeList.setAdapter(mightAdapter);


        //All
        allList = new ArrayList<>();
        allAdapter = new PlaceVerticalAdapter(context, allList);
        allAdapter.setClickListener(new PlaceVerticalAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {

                if (allList.get(pos).getType() == PlaceVerticalAdapter.TYPE_ITEM) {
                    gotoDetailsActivity(allList.get(pos).getRestaurantItem());
                }
            }
        });

        allItems = (RecyclerView) findViewById(R.id.allItems);

        GridLayoutManager llm = new GridLayoutManager(this, 3);
        llm.setOrientation(GridLayoutManager.VERTICAL);
        llm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (allAdapter.getItemViewType(position)) {
                    case PlaceVerticalAdapter.TYPE_HEADER:
                        return 3;
                    case PlaceVerticalAdapter.TYPE_ITEM:
                        return 1;
                    case PlaceVerticalAdapter.TYPE_MORE:
                        return 3;
                    default:
                        return -1;
                }
            }
        });

        allItems.setLayoutManager(llm);
        allItems.setNestedScrollingEnabled(false);
        allItems.setAdapter(allAdapter);

        getDetails();

    }

    private void getDetails() {

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

        RetroInterface.getZomatoRestApi().getMealRestaurants(
                SessionPreference.getUserId(context)+"",
                location.getLatitude() + "",
                location.getLongitude() + "",
                new Callback<MealDetailResponse>() {
                    @Override
                    public void success(MealDetailResponse mealDetailResponse, Response response) {

                        if (mealDetailResponse != null && mealDetailResponse.isSuccess()) {

                            List<MealDetailItem> detailItems = mealDetailResponse.getItems();
                            allList = new ArrayList<>();

                            for (int i = 0; i < detailItems.size(); i++) {

                                MealTypeItem headItem = detailItems.get(i).getMealTypeItem();
                                allList.add(new PlaceDisplayItem(headItem, PlaceVerticalAdapter.TYPE_HEADER));

                                List<RestaurantItem> restaurantItems = detailItems.get(i).getRestaurants();
                                for (int j = 0; j < restaurantItems.size(); j++) {
                                    RestaurantItem restaurantItem = restaurantItems.get(j);
                                    allList.add(new PlaceDisplayItem(restaurantItem, PlaceVerticalAdapter.TYPE_ITEM));
                                }

                                MealTypeItem moreItem = detailItems.get(i).getMoreTypeItem();
                                allList.add(new PlaceDisplayItem(moreItem, PlaceVerticalAdapter.TYPE_MORE));

                            }
                            allAdapter.refresh(allList);

                        }


                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

    }

    private void gotoDetailsActivity(RestaurantItem restaurantItem) {
        startActivity(PlaceDetailActivity.getCallIntent(context, restaurantItem));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseBottomView(SCREEN_HOME);

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
        startActivity(SelectLocationActivity.getCallIntent(context));
    }

    public void searchClick(View view) {
        enterReveal(view, search_toolbar);
    }

    public void bookmarkClick(View view) {
        startActivity(BookmarkActivity.getCallIntent(context));
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

}