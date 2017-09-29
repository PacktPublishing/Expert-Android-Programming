package com.androcid.zomato.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androcid.zomato.BuildConfig;
import com.androcid.zomato.R;
import com.androcid.zomato.activity.photos.AllMenuActivity;
import com.androcid.zomato.activity.photos.AllPhotosActivity;
import com.androcid.zomato.activity.photos.PhotosPagerActivity;
import com.androcid.zomato.activity.photos.PickPhotosActivity;
import com.androcid.zomato.activity.review.AddReviewActivity;
import com.androcid.zomato.activity.review.AllReviewActivity;
import com.androcid.zomato.dao.DaoController;
import com.androcid.zomato.model.RestaurantDetails;
import com.androcid.zomato.model.RestaurantImage;
import com.androcid.zomato.model.RestaurantItem;
import com.androcid.zomato.model.RestaurantMenu;
import com.androcid.zomato.model.RestaurantTiming;
import com.androcid.zomato.model.ReviewDetail;
import com.androcid.zomato.model.ReviewItem;
import com.androcid.zomato.model.SaveItem;
import com.androcid.zomato.model.UserBeenThere;
import com.androcid.zomato.model.UserBookmark;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.RestaurantDetailResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.retro.SaveResponse;
import com.androcid.zomato.util.AppUtils;
import com.androcid.zomato.util.CommonFunctions;
import com.androcid.zomato.util.Constant;
import com.androcid.zomato.util.MyLg;
import com.androcid.zomato.view.adapter.ReviewAdapter;
import com.androcid.zomato.view.bottomfab.FooterLayout;
import com.androcid.zomato.view.pageradapter.ImagePagerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.androcid.zomato.util.Constant.APP_INITIATED_REQUEST;


/**
 *
 */
public class PlaceDetailActivity extends AppCompatActivity {

    Context context = PlaceDetailActivity.this;
    private static final String TAG = PlaceDetailActivity.class.getSimpleName();
    FooterLayout mFabToolbar;
    FloatingActionButton mFab;

    LinearLayout ratePlace;
    LinearLayout addPhoto;
    LinearLayout addReview;
    LinearLayout addCollection;

    ImageView tra_overlay;
    LinearLayout bottomLay;

    NestedScrollView scrollView;
    View fadeShadow;
    ViewPager checkImage;

    public static Intent getCallIntent(Context context, RestaurantItem item) {
        Intent intent = new Intent(context, PlaceDetailActivity.class);
        intent.putExtra(Constant.RESTAURANT, item);
        return intent;
    }

    RestaurantItem restaurantItem;
    RelativeLayout toolbar_lay;
    TextView title;

    //
    TextView restaurant_rating;
    TextView rating_count;
    TextView restaurant_name;
    TextView restaurant_location;
    TextView restaurant_visits;
    TextView restaurant_address;

    LinearLayout beenThereLay;
    LinearLayout bookmarkLay;
    TextView bookmarkValue;

    //Timings
    LinearLayout dayMonday;
    TextView timingMonday;
    LinearLayout dayTuesday;
    TextView timingTuesday;
    LinearLayout dayWednesday;
    TextView timingWednesday;
    LinearLayout dayThursday;
    TextView timingThursday;
    LinearLayout dayFriday;
    TextView timingFriday;
    LinearLayout daySaturday;
    TextView timingSaturday;
    LinearLayout daySunday;
    TextView timingSunday;

    //PHOTOS
    LinearLayout photo_lay;
    LinearLayout photo_1_lay, photo_2_lay, photo_3_lay, photo_4_lay;
    ImageView photo_1, photo_2, photo_3, photo_4;

    //MENU
    LinearLayout menu_lay;
    LinearLayout menu_1_lay, menu_2_lay, menu_3_lay, menu_4_lay, menu_5_lay;
    ImageView menu_1, menu_2, menu_3, menu_4, menu_5;

    //Reviews
    List<ReviewItem> reviewItems;
    ReviewAdapter reviewAdapter;
    RecyclerView reviewRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        restaurantItem = (RestaurantItem) getIntent().getSerializableExtra(Constant.RESTAURANT);
        if (restaurantItem == null) {
            finish();
            return;
        }

        mFabToolbar = (FooterLayout) findViewById(R.id.fabtoolbar);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        toolbar_lay = (RelativeLayout) findViewById(R.id.toolbar_lay);
        title = (TextView) findViewById(R.id.title);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            toolbar_lay.setPadding(0, CommonFunctions.getStatusBarHeight(context), 0, 0);
        }

        //RESTAURANT DETAILS
        restaurant_name = (TextView) findViewById(R.id.restaurant_name);
        restaurant_address = (TextView) findViewById(R.id.restaurant_address);

        restaurant_visits = (TextView) findViewById(R.id.restaurant_visits);
        beenThereLay = (LinearLayout) findViewById(R.id.beenThereLay);
        bookmarkLay = (LinearLayout) findViewById(R.id.bookmarkLay);
        bookmarkValue = (TextView) findViewById(R.id.bookmarkValue);

        //PHOTOS
        photo_lay = (LinearLayout) findViewById(R.id.photo_lay);
        photo_1_lay = (LinearLayout) findViewById(R.id.photo_1_lay);
        photo_2_lay = (LinearLayout) findViewById(R.id.photo_2_lay);
        photo_3_lay = (LinearLayout) findViewById(R.id.photo_3_lay);
        photo_4_lay = (LinearLayout) findViewById(R.id.photo_4_lay);

        photo_1 = (ImageView) findViewById(R.id.photo_1);
        photo_2 = (ImageView) findViewById(R.id.photo_2);
        photo_3 = (ImageView) findViewById(R.id.photo_3);
        photo_4 = (ImageView) findViewById(R.id.photo_4);

        //PHOTOS
        menu_lay = (LinearLayout) findViewById(R.id.menu_lay);
        menu_1_lay = (LinearLayout) findViewById(R.id.menu_1_lay);
        menu_2_lay = (LinearLayout) findViewById(R.id.menu_2_lay);
        menu_3_lay = (LinearLayout) findViewById(R.id.menu_3_lay);
        menu_4_lay = (LinearLayout) findViewById(R.id.menu_4_lay);
        menu_5_lay = (LinearLayout) findViewById(R.id.menu_5_lay);

        menu_1 = (ImageView) findViewById(R.id.menu_1);
        menu_2 = (ImageView) findViewById(R.id.menu_2);
        menu_3 = (ImageView) findViewById(R.id.menu_3);
        menu_4 = (ImageView) findViewById(R.id.menu_4);
        menu_5 = (ImageView) findViewById(R.id.menu_5);

        //Review
        reviewItems = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(context, reviewItems);
        reviewAdapter.setClickListener(new ReviewAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                //gotoDetailsActivity(mightList.get(pos));
            }
        });

        reviewRecyclerView = (RecyclerView) findViewById(R.id.reviewRecyclerView);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        reviewRecyclerView.setAdapter(reviewAdapter);

        //Timing
        initTiming();


        ratePlace = (LinearLayout) findViewById(R.id.ratePlace);
        addPhoto = (LinearLayout) findViewById(R.id.addPhoto);
        addReview = (LinearLayout) findViewById(R.id.addReview);
        addCollection = (LinearLayout) findViewById(R.id.addCollection);

        tra_overlay = (ImageView) findViewById(R.id.tra_overlay);
        bottomLay = (LinearLayout) findViewById(R.id.bottomLay);

        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        fadeShadow = (View) findViewById(R.id.fadeShadow);
        checkImage = (ViewPager) findViewById(R.id.checkImage);

        tra_overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tra_overlay.setVisibility(View.INVISIBLE);
                bottomLay.setVisibility(View.INVISIBLE);

                mFabToolbar.contractFab();
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFabToolbar.expandFab();

                Animator bottomExpansion =
                        ObjectAnimator.ofPropertyValuesHolder(bottomLay,
                                PropertyValuesHolder.ofFloat(View.SCALE_X, 0f, 1f));
                bottomExpansion.setStartDelay(300);
                bottomExpansion.setDuration(300);

                Animator bottomExpansionFade =
                        ObjectAnimator.ofPropertyValuesHolder(bottomLay,
                                PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f));
                bottomExpansion.setStartDelay(300);
                bottomExpansion.setDuration(300);

                Animator overlayFade =
                        ObjectAnimator.ofPropertyValuesHolder(tra_overlay,
                                PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f));
                overlayFade.setStartDelay(0);
                overlayFade.setDuration(600);

                bottomExpansion.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        bottomLay.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });

                overlayFade.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        tra_overlay.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });


                AnimatorSet animSet = new AnimatorSet();
                animSet.playTogether(bottomExpansion, bottomExpansionFade, overlayFade);
                animSet.start();

            }
        });

        mFabToolbar.setFab(mFab);

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

        setRestaurantDetails();
        getRestaurantDetails();
    }

    private void initTiming() {
        dayMonday = (LinearLayout) findViewById(R.id.dayMonday);
        dayTuesday = (LinearLayout) findViewById(R.id.dayTuesday);
        dayWednesday = (LinearLayout) findViewById(R.id.dayWednesday);
        dayThursday = (LinearLayout) findViewById(R.id.dayThursday);
        dayFriday = (LinearLayout) findViewById(R.id.dayFriday);
        daySaturday = (LinearLayout) findViewById(R.id.daySaturday);
        daySunday = (LinearLayout) findViewById(R.id.daySunday);

        timingMonday = (TextView) findViewById(R.id.timingMonday);
        timingTuesday = (TextView) findViewById(R.id.timingTuesday);
        timingWednesday = (TextView) findViewById(R.id.timingWednesday);
        timingThursday = (TextView) findViewById(R.id.timingThursday);
        timingFriday = (TextView) findViewById(R.id.timingFriday);
        timingSaturday = (TextView) findViewById(R.id.timingSaturday);
        timingSunday = (TextView) findViewById(R.id.timingSunday);

        dayMonday.setSelected(false);
        dayTuesday.setSelected(false);
        dayWednesday.setSelected(false);
        dayThursday.setSelected(false);
        dayFriday.setSelected(false);
        daySaturday.setSelected(false);
        daySunday.setSelected(false);
    }

    private void setToday() {
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DAY_OF_WEEK);

        switch (date) {
            case Calendar.MONDAY:
                dayMonday.setSelected(true);
                break;
            case Calendar.TUESDAY:
                dayTuesday.setSelected(true);
                break;
            case Calendar.WEDNESDAY:
                dayWednesday.setSelected(true);
                break;
            case Calendar.THURSDAY:
                dayThursday.setSelected(true);
                break;
            case Calendar.FRIDAY:
                dayFriday.setSelected(true);
                break;
            case Calendar.SATURDAY:
                daySaturday.setSelected(true);
                break;
            case Calendar.SUNDAY:
                daySunday.setSelected(true);
                break;
        }

    }

    private void setTimings(List<RestaurantTiming> timings) {

        for (int i = 0; i < timings.size(); i++) {
            switch (AppUtils.getDay(timings.get(i).getDay())) {

                case Calendar.MONDAY:
                    timingMonday.setText(timings.get(i).getTiming());
                    break;
                case Calendar.TUESDAY:
                    timingTuesday.setText(timings.get(i).getTiming());
                    break;
                case Calendar.WEDNESDAY:
                    timingWednesday.setText(timings.get(i).getTiming());
                    break;
                case Calendar.THURSDAY:
                    timingThursday.setText(timings.get(i).getTiming());
                    break;
                case Calendar.FRIDAY:
                    timingFriday.setText(timings.get(i).getTiming());
                    break;
                case Calendar.SATURDAY:
                    timingSaturday.setText(timings.get(i).getTiming());
                    break;
                case Calendar.SUNDAY:
                    timingSunday.setText(timings.get(i).getTiming());
                    break;
            }
        }
        setToday();
    }

    private void getRestaurantDetails() {

        if (BuildConfig.NETWORK_TEST && Build.VERSION.SDK_INT >= 14) {
            try {
                TrafficStats.setThreadStatsTag(APP_INITIATED_REQUEST);
                // Once tag has been applied, network request has to be made request
            } finally {
                TrafficStats.clearThreadStatsTag();
            }
        }

        RetroInterface.getZomatoRestApi().getRestaurantDetails(
                SessionPreference.getUserId(context) + "",
                restaurantItem.getId() + "",
                new Callback<RestaurantDetailResponse>() {
                    @Override
                    public void success(RestaurantDetailResponse restaurantResponse, Response response) {

                        RestaurantDetails details = restaurantResponse.getDetails();
                        List<RestaurantImage> images = details.getImages();
                        List<RestaurantTiming> timings = details.getTimings();
                        List<RestaurantImage> photos = details.getPhoto();
                        List<RestaurantMenu> menu = details.getMenu();
                        reviewItems = details.getReviews();

                        setPagerImages(images);
                        setTimings(timings);
                        setReviews();
                        updatePhotos(photos);
                        updateMenus(menu);

                        setMoreDetails(details.getReviewDetail(), details.getBookmark(), details.getBeenThere());

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );
    }

    private void updatePhotos(List<RestaurantImage> images) {

        if (images != null && images.size() != 0) {
            photo_lay.setVisibility(View.VISIBLE);

            photo_1_lay.setVisibility(View.INVISIBLE);
            photo_2_lay.setVisibility(View.INVISIBLE);
            photo_3_lay.setVisibility(View.INVISIBLE);
            photo_4_lay.setVisibility(View.INVISIBLE);

            for (int i = 0; i < images.size(); i++) {
                showImage(images.get(i), i);
                if (i == 3)
                    break;
            }

        } else {
            photo_lay.setVisibility(View.GONE);
        }

    }
    private void showImage(RestaurantImage restaurantImage, int i) {

        switch (i) {
            case 0:
                photo_1_lay.setVisibility(View.VISIBLE);
                if (!CommonFunctions.checkNull(restaurantImage.getImage()).equals("")) {
                    Picasso.with(context)
                            .load(RetroInterface.IMAGE_URL + restaurantImage.getImage())
                            .resize(200, 200)
                            .placeholder(R.drawable.placeholder_200)
                            .error(R.drawable.placeholder_200)
                            .into(photo_1);
                }
                break;
            case 1:
                photo_2_lay.setVisibility(View.VISIBLE);
                if (!CommonFunctions.checkNull(restaurantImage.getImage()).equals("")) {
                    Picasso.with(context)
                            .load(RetroInterface.IMAGE_URL + restaurantImage.getImage())
                            .resize(200, 200)
                            .placeholder(R.drawable.placeholder_200)
                            .error(R.drawable.placeholder_200)
                            .into(photo_2);
                }
                break;
            case 2:
                photo_3_lay.setVisibility(View.VISIBLE);
                if (!CommonFunctions.checkNull(restaurantImage.getImage()).equals("")) {
                    Picasso.with(context)
                            .load(RetroInterface.IMAGE_URL + restaurantImage.getImage())
                            .resize(200, 200)
                            .placeholder(R.drawable.placeholder_200)
                            .error(R.drawable.placeholder_200)
                            .into(photo_3);
                }
                break;
            case 3:
                photo_4_lay.setVisibility(View.VISIBLE);
                if (!CommonFunctions.checkNull(restaurantImage.getImage()).equals("")) {
                    Picasso.with(context)
                            .load(RetroInterface.IMAGE_URL + restaurantImage.getImage())
                            .resize(200, 200)
                            .placeholder(R.drawable.placeholder_200)
                            .error(R.drawable.placeholder_200)
                            .into(photo_4);
                }
                break;
        }

    }


    private void updateMenus(List<RestaurantMenu> images) {

        if (images != null && images.size() != 0) {
            menu_lay.setVisibility(View.VISIBLE);

            menu_1_lay.setVisibility(View.INVISIBLE);
            menu_2_lay.setVisibility(View.INVISIBLE);
            menu_3_lay.setVisibility(View.INVISIBLE);
            menu_4_lay.setVisibility(View.INVISIBLE);
            menu_5_lay.setVisibility(View.INVISIBLE);

            for (int i = 0; i < images.size(); i++) {
                showMenu(images.get(i), i);
                if (i == 4)
                    break;
            }

        } else {
            menu_lay.setVisibility(View.GONE);
        }

    }
    private void showMenu(RestaurantMenu restaurantImage, int i) {

        switch (i) {
            case 0:
                menu_1_lay.setVisibility(View.VISIBLE);
                if (!CommonFunctions.checkNull(restaurantImage.getImage()).equals("")) {
                    Picasso.with(context)
                            .load(RetroInterface.IMAGE_URL + restaurantImage.getImage())
                            .resize(200, 200)
                            .placeholder(R.drawable.placeholder_200)
                            .error(R.drawable.placeholder_200)
                            .into(menu_1);
                }
                break;
            case 1:
                menu_2_lay.setVisibility(View.VISIBLE);
                if (!CommonFunctions.checkNull(restaurantImage.getImage()).equals("")) {
                    Picasso.with(context)
                            .load(RetroInterface.IMAGE_URL + restaurantImage.getImage())
                            .resize(200, 200)
                            .placeholder(R.drawable.placeholder_200)
                            .error(R.drawable.placeholder_200)
                            .into(menu_2);
                }
                break;
            case 2:
                menu_3_lay.setVisibility(View.VISIBLE);
                if (!CommonFunctions.checkNull(restaurantImage.getImage()).equals("")) {
                    Picasso.with(context)
                            .load(RetroInterface.IMAGE_URL + restaurantImage.getImage())
                            .resize(200, 200)
                            .placeholder(R.drawable.placeholder_200)
                            .error(R.drawable.placeholder_200)
                            .into(menu_3);
                }
                break;
            case 3:
                menu_4_lay.setVisibility(View.VISIBLE);
                if (!CommonFunctions.checkNull(restaurantImage.getImage()).equals("")) {
                    Picasso.with(context)
                            .load(RetroInterface.IMAGE_URL + restaurantImage.getImage())
                            .resize(200, 200)
                            .placeholder(R.drawable.placeholder_200)
                            .error(R.drawable.placeholder_200)
                            .into(menu_4);
                }
                break;
            case 4:
                menu_5_lay.setVisibility(View.VISIBLE);
                if (!CommonFunctions.checkNull(restaurantImage.getImage()).equals("")) {
                    Picasso.with(context)
                            .load(RetroInterface.IMAGE_URL + restaurantImage.getImage())
                            .resize(200, 200)
                            .placeholder(R.drawable.placeholder_200)
                            .error(R.drawable.placeholder_200)
                            .into(menu_5);
                }
                break;
        }

    }

    private void setMoreDetails(ReviewDetail reviewDetail, UserBookmark bookmark, UserBeenThere beenThere) {

        beenThereLay.setSelected(beenThere.isUser_beenthere());
        if (bookmark.isUser_bookmark()) {
            bookmarkLay.setSelected(true);
            bookmarkValue.setText("Bookmarked");
        } else {
            bookmarkLay.setSelected(false);
            bookmarkValue.setText("Bookmark");
        }

        updateVisit(reviewDetail.getReview_count(), bookmark.getBookmark_count(), beenThere.getBeenthere_count());

    }

    int review_count;
    int bookmark_count;
    int beenthere_count;

    private void updateVisit(int review_count, int bookmark_count, int beenthere_count) {

        this.review_count = review_count;
        this.bookmark_count = bookmark_count;
        this.beenthere_count = beenthere_count;

        restaurant_visits.setText(getString(R.string.txt_restaurant_visit,
                review_count, bookmark_count, beenthere_count));
    }


    private void setReviews() {
        reviewAdapter.refresh(reviewItems);
    }

    List<RestaurantImage> restaurantImages;

    private void setPagerImages(List<RestaurantImage> images) {

        if (images != null) {

            restaurantImages = images;

            ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(getSupportFragmentManager(), images, ImagePagerAdapter.NORMAL);
            checkImage.setAdapter(imagePagerAdapter);
            checkImage.setOnTouchListener(new View.OnTouchListener() {
                private float pointX;
                private float pointY;
                private int tolerance = 50;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            return false; //This is important, if you return TRUE the action of swipe will not take place.
                        case MotionEvent.ACTION_DOWN:
                            pointX = event.getX();
                            pointY = event.getY();
                            break;
                        case MotionEvent.ACTION_UP:
                            boolean sameX = pointX + tolerance > event.getX() && pointX - tolerance < event.getX();
                            boolean sameY = pointY + tolerance > event.getY() && pointY - tolerance < event.getY();
                            if (sameX && sameY) {

                                //save List
                                if (restaurantImages != null) {

                                    DaoController.addRestaurantImages(context, restaurantImages);
                                    startActivity(PhotosPagerActivity.getCallIntent(context, checkImage.getCurrentItem()));
                                }

                            }
                    }
                    return false;
                }
            });

        }
    }

    private void setRestaurantDetails() {

        title.setText(restaurantItem.getName());
        restaurant_name.setText(restaurantItem.getName());
        restaurant_address.setText(restaurantItem.getAddress());
    }

    private void checkVisibility() {

        int vis = getVisiblePercent(checkImage);
        if (vis > -1) {
            float alpha = 1f - (float) vis / (float) 80;
            MyLg.e(TAG, "Visible " + vis + " " + alpha);

            fadeShadow.setAlpha(alpha);
            toolbar_lay.setAlpha(alpha);
            title.setAlpha(alpha);
        } else {
            fadeShadow.setAlpha(1f);
            toolbar_lay.setAlpha(1f);
            title.setAlpha(1f);
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

    private void iconAnim(View icon) {
        Animator iconAnim = ObjectAnimator.ofPropertyValuesHolder(icon,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 0.5f, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0.5f, 1f));
        iconAnim.start();
    }

    public void closeClick(View view) {
        finish();
    }

    public void addReview(View view) {
        startActivity(AddReviewActivity.getCallIntent(context, restaurantItem.getRestaurant_id() + ""
                , restaurantItem.getId() + "", restaurantItem.getName()));
    }

    public void showReviews(View view) {
        startActivity(AllReviewActivity.getCallIntent(context));
    }

    public void showMenuClick(View view) {
        startActivity(AllMenuActivity.getCallIntent(context, restaurantItem.getId()+""));
    }

    public void showAllPhotosCLick(View view) {
        startActivity(AllPhotosActivity.getCallIntent(context, restaurantItem.getId()+""));
    }

    public void capturePhoto(View view) {
        startActivity(PickPhotosActivity.getCallIntent(context, PickPhotosActivity.TYPE_NORMAL));

    }

    public void selectChange(View view) {
        if (view.isSelected()) {
            view.setSelected(false);
        } else {
            view.setSelected(true);
        }

        if (view.getId() == R.id.bookmarkLay) {
            int save;
            if (view.isSelected()) {
                bookmarkValue.setText("Bookmarked");
                save = 1;
            } else {
                bookmarkValue.setText("Bookmark");
                save = 0;
            }

            saveBookmark(save);

        }

        if (view.getId() == R.id.beenThereLay) {
            int save;
            if (view.isSelected()) {
                save = 1;
            } else {
                save = 0;
            }

            saveBeenthere(save);

        }

        iconAnim(view);
    }

    private void saveBookmark(int save) {

        RetroInterface.getZomatoRestApi().bookmarkRestaurant(
                SessionPreference.getUserId(context) + "",
                restaurantItem.getId() + "",
                save + "",
                new Callback<SaveResponse>() {
                    @Override
                    public void success(SaveResponse normalResponse, Response response) {

                        if (normalResponse != null && normalResponse.getItems() != null) {

                            SaveItem item = normalResponse.getItems();

                            updateVisit(review_count, item.getCount(), beenthere_count);
                            if (item.isStatus()) {
                                bookmarkLay.setSelected(true);
                                bookmarkValue.setText("Bookmarked");
                            } else {
                                bookmarkLay.setSelected(false);
                                bookmarkValue.setText("Bookmark");
                            }

                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }

        );
    }

    private void saveBeenthere(int save) {
        RetroInterface.getZomatoRestApi().beenthereRestaurant(
                SessionPreference.getUserId(context) + "",
                restaurantItem.getId() + "",
                save + "",
                new Callback<SaveResponse>() {
                    @Override
                    public void success(SaveResponse normalResponse, Response response) {

                        if (normalResponse != null && normalResponse.getItems() != null) {

                            SaveItem item = normalResponse.getItems();

                            updateVisit(review_count, bookmark_count, item.getCount());
                            if (item.isStatus()) {
                                beenThereLay.setSelected(true);
                            } else {
                                beenThereLay.setSelected(false);
                            }

                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }

        );
    }

}