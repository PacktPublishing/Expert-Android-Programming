package com.androcid.zomato.activity.collection;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androcid.zomato.BuildConfig;
import com.androcid.zomato.R;
import com.androcid.zomato.activity.PlaceDetailActivity;
import com.androcid.zomato.model.CollectionItem;
import com.androcid.zomato.model.RestaurantItem;
import com.androcid.zomato.model.SaveItem;
import com.androcid.zomato.model.room.UserLocation;
import com.androcid.zomato.preference.LocationPreference;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.RestaurantResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.retro.SaveResponse;
import com.androcid.zomato.util.CommonFunctions;
import com.androcid.zomato.util.Constant;
import com.androcid.zomato.util.MyLg;
import com.androcid.zomato.view.adapter.CollectionPlaceAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.androcid.zomato.util.Constant.APP_INITIATED_REQUEST;

/**
 *
 */
public class CollectionDetailActivity extends AppCompatActivity {

    private static final String TAG = CollectionDetailActivity.class.getSimpleName();

    RelativeLayout toolbar_lay;
    NestedScrollView scrollView;
    View fadeShadow;
    ImageView checkImage;
    private Context context = CollectionDetailActivity.this;

    CollectionItem item;

    public static Intent getCallIntent(Context context, CollectionItem item) {
        Intent intent = new Intent(context, CollectionDetailActivity.class);
        intent.putExtra(Constant.DATA, item);
        return intent;
    }

    TextView title;

    //PAGE DETAILS
    TextView name;
    TextView saves;
    TextView details;
    TextView saveButton;

    //Might Like
    List<RestaurantItem> mightList;
    CollectionPlaceAdapter mightAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_details);


        title = (TextView) findViewById(R.id.title);
        toolbar_lay = (RelativeLayout) findViewById(R.id.toolbar_lay);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            toolbar_lay.setPadding(0, CommonFunctions.getStatusBarHeight(context), 0, 0);
        }

        item = (CollectionItem) getIntent().getSerializableExtra(Constant.DATA);

        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        fadeShadow = (View) findViewById(R.id.fadeShadow);
        checkImage = (ImageView) findViewById(R.id.checkImage);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                checkVisibility();
            }
        });

        checkImage.post(new Runnable() {
            @Override
            public void run() {
                checkVisibility();
            }
        });
        checkVisibility();

        name = (TextView) findViewById(R.id.name);
        saves = (TextView) findViewById(R.id.saves);
        details = (TextView) findViewById(R.id.details);
        saveButton = (TextView) findViewById(R.id.saveButton);

        //Might
        mightList = new ArrayList<>();
        mightAdapter = new CollectionPlaceAdapter(context, mightList);
        mightAdapter.setClickListener(new CollectionPlaceAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                gotoDetailsActivity(pos);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mightAdapter);

        setCollectionDetails();
    }

    private void setCollectionDetails() {

        if (!CommonFunctions.checkNull(item.getImage()).equals("")) {
            Picasso.with(context)
                    .load(RetroInterface.IMAGE_URL + item.getImage())
                    .resize(500, 500)
                    .placeholder(R.drawable.placeholder_200)
                    .error(R.drawable.placeholder_200)
                    .into(checkImage);
        }
        title.setText(item.getName());
        name.setText(item.getName());
        details.setText(item.getDescription());

        saves.setText(getString(R.string.txt_place_n_save, mightList.size(), item.getCount()));

        if (item.getUser_id() == SessionPreference.getUserId(context)) {
            saveButton.setVisibility(View.GONE);
        } else {
            saveButton.setVisibility(View.VISIBLE);
            if (item.isSaved()) {
                saveButton.setSelected(true);
                saveButton.setText("Saved");
            } else {
                saveButton.setSelected(false);
                saveButton.setText("Save");
            }
        }

        getCollectionRestaurants();
    }

    private void getCollectionRestaurants() {

        if (BuildConfig.NETWORK_TEST && Build.VERSION.SDK_INT >= 14) {
            try {
                TrafficStats.setThreadStatsTag(APP_INITIATED_REQUEST);
                // Once tag has been applied, network request has to be made request
            } finally {
                TrafficStats.clearThreadStatsTag();
            }
        }

        UserLocation location = LocationPreference.getUserLocation(context);
        RetroInterface.getZomatoRestApi().getCollectionRestaurants(
                SessionPreference.getUserId(context)+"",
                item.getId() + "",
                location.getLatitude() + "",
                location.getLongitude() + "",
                new Callback<RestaurantResponse>() {
                    @Override
                    public void success(RestaurantResponse restaurantResponse, Response response) {

                        if (restaurantResponse != null && restaurantResponse.isSuccess()) {
                            mightList = restaurantResponse.getItems();
                            mightAdapter.refresh(mightList);
                            saves.setText(getString(R.string.txt_place_n_save, mightList.size(), item.getCount()));
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );

    }


    private void gotoDetailsActivity(int pos) {
        RestaurantItem item = mightList.get(pos);
        startActivity(PlaceDetailActivity.getCallIntent(context, item));
    }

    private void checkVisibility() {

        int vis = getVisiblePercent(checkImage);
        if (vis > -1) {
            float alpha = 1f - (float) vis / (float) 80;
            MyLg.e(TAG, "Visible " + vis + " " + alpha);

            fadeShadow.setAlpha(alpha);
            toolbar_lay.setAlpha(alpha);
        } else {
            fadeShadow.setAlpha(1f);
            toolbar_lay.setAlpha(1f);
        }
    }

    public int getVisiblePercent(View v) {

        Rect scrollBounds = new Rect();
        scrollView.getHitRect(scrollBounds);
        if (v.getLocalVisibleRect(scrollBounds)) {
            // Any portion of the imageView, even a single pixel, is within the visible window
        } else {
            // NONE of the imageView is within the visible window
            return -1;
        }

        if (v.isShown()) {
            Rect r = new Rect();
            v.getGlobalVisibleRect(r);
            double sVisible = r.width() * r.height();
            double sTotal = v.getWidth() * v.getHeight();

            MyLg.e(TAG, "sVisible " + sVisible + " sTotal" + sTotal);

            return (int) (100 * sVisible / sTotal) - 20;
        } else {
            return -1;
        }
    }

    public void closeClick(View view) {
        finish();
    }

    public void selectChange(View view) {
        if (view.isSelected()) {
            view.setSelected(false);
        } else {
            view.setSelected(true);
        }

        if (view.getId() == R.id.saveButton) {
            int save;
            if (view.isSelected()) {
                ((TextView) view).setText("Saved");
                save = 1;
            } else {
                ((TextView) view).setText("Save");
                save = 0;
            }

            saveCollection(save);

            iconAnim(view);
        }

    }

    private void saveCollection(int save) {
        RetroInterface.getZomatoRestApi().saveCollection(
                SessionPreference.getUserId(context) + "",
                item.getId() + "",
                save + "",
                new Callback<SaveResponse>() {
                    @Override
                    public void success(SaveResponse normalResponse, Response response) {

                        if(normalResponse!=null && normalResponse.getItems()!=null){

                            SaveItem item = normalResponse.getItems();

                            saves.setText(getString(R.string.txt_place_n_save, mightList.size(), item.getCount()));
                            if (item.isStatus()) {
                                saveButton.setSelected(true);
                                saveButton.setText("Saved");
                            } else {
                                saveButton.setSelected(false);
                                saveButton.setText("Save");
                            }

                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }

        );
    }

    private void iconAnim(View icon) {
        Animator iconAnim = ObjectAnimator.ofPropertyValuesHolder(icon,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.5f, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.5f, 1f));
        iconAnim.start();
    }

}