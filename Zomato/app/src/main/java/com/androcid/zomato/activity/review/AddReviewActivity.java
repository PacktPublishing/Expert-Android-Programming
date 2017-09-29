package com.androcid.zomato.activity.review;

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
import android.widget.ScrollView;
import android.widget.TextView;

import com.androcid.zomato.BuildConfig;
import com.androcid.zomato.R;
import com.androcid.zomato.activity.PlaceSearchActivity;
import com.androcid.zomato.activity.photos.PickPhotosActivity;
import com.androcid.zomato.dao.DaoController;
import com.androcid.zomato.model.PhotoItem;
import com.androcid.zomato.model.User;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.MealDetailResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.retro.UserListResponse;
import com.androcid.zomato.service.ImagePreference;
import com.androcid.zomato.service.ImageUploadItem;
import com.androcid.zomato.service.UploadImageService;
import com.androcid.zomato.util.Constant;
import com.androcid.zomato.util.KeyboardDetect;
import com.androcid.zomato.util.MyLg;
import com.androcid.zomato.util.ServiceCheck;
import com.androcid.zomato.util.Toas;
import com.androcid.zomato.view.adapter.PhotoItemHorizontalAdapter;
import com.androcid.zomato.view.adapter.UserSelectListAdapter;
import com.androcid.zomato.view.custom.ImageRatingBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.androcid.zomato.util.Constant.USER_INITIATED_REQUEST;


public class AddReviewActivity extends AppCompatActivity {

    private static final String TAG = AddReviewActivity.class.getSimpleName();
    private static final int SEARCH_PLACE = 111;
    private Context context = AddReviewActivity.this;

    String restaurant_id, restaurant_branch_id, restaurant_name;

    public static Intent getCallIntent(Context context, String restaurant_id, String restaurant_branch_id, String restaurant_name) {
        Intent intent = new Intent(context, AddReviewActivity.class);
        intent.putExtra(Constant.RESTAURANT_ID, restaurant_id);
        intent.putExtra(Constant.RESTAURANT_BRANCH_ID, restaurant_branch_id);
        intent.putExtra(Constant.NAME, restaurant_name);
        return intent;
    }

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, AddReviewActivity.class);
        return intent;
    }

    ImageRatingBar ratingBar;
    TextView ratingValue;

    ScrollView scroll;
    KeyboardDetect keyboardDetect;
    LinearLayout imageHolder;
    RelativeLayout userHolder;
    TextView userHint;
    LinearLayout buttonHolder;
    EditText yourReview;

    List<User> users;
    UserSelectListAdapter userSelectListAdapter;
    RecyclerView userList;

    //TOOLBAR
    TextView title;

    //IMAGES
    List<PhotoItem> imageList;
    PhotoItemHorizontalAdapter adapter;
    RecyclerView imageRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        //GET IDS
        restaurant_id = getIntent().getStringExtra(Constant.RESTAURANT_ID);
        restaurant_branch_id = getIntent().getStringExtra(Constant.RESTAURANT_BRANCH_ID);

        closeHandler = new Handler();
        closeRun = new Runnable() {

            @Override
            public void run() {
                getUserSearch();
            }
        };

        title = (TextView) findViewById(R.id.title);

        ratingBar = (ImageRatingBar) findViewById(R.id.ratingBar);
        ratingValue = (TextView) findViewById(R.id.ratingValue);

        ratingBar.setOnRatingSliderChangeListener(new ImageRatingBar.OnRatingSliderChangeListener() {
            @Override
            public void onPendingRating(int rating) {
                MyLg.e(TAG, "onPendingRating " + rating);
            }

            @Override
            public void onFinalRating(int rating) {
                MyLg.e(TAG, "onFinalRating " + rating);

                updateRating();

            }

            @Override
            public void onCancelRating() {
                MyLg.e(TAG, "onCancelRating ");

            }
        });

        scroll = (ScrollView) findViewById(R.id.scroll);
        scroll.setEnabled(false);

        imageList = new ArrayList<>();
        adapter = new PhotoItemHorizontalAdapter(context, imageList);

        imageRecycler = (RecyclerView) findViewById(R.id.imageRecycler);
        imageRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        imageRecycler.setAdapter(adapter);


        buttonHolder = (LinearLayout) findViewById(R.id.buttonHolder);
        imageHolder = (LinearLayout) findViewById(R.id.imageHolder);
        userHolder = (RelativeLayout) findViewById(R.id.userHolder);
        userHint = (TextView) findViewById(R.id.userHint);
        yourReview = (EditText) findViewById(R.id.yourReview);

        keyboardDetect = new KeyboardDetect(this);
        keyboardDetect.setKeyboardListener(new KeyboardDetect.KeyboardListener() {
            @Override
            public void onSoftKeyboardShown(boolean isShowing) {
                if (isShowing) {
                    imageHolder.setVisibility(View.GONE);
                } else {
                    showImageHolder();

                }
            }
        });

        yourReview.addTextChangedListener(new TextWatcher() {
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

        //User List
        users = new ArrayList<>();
        userSelectListAdapter = new UserSelectListAdapter(context, users);
        userSelectListAdapter.setClickListener(new UserSelectListAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {

                String s = yourReview.getText().toString();
                s = s + " ";

                int indexOf = s.lastIndexOf("@");

                String value = s.substring(0, indexOf);

                value = value + " " + users.get(pos).getName();

                yourReview.setText(value);
                yourReview.setSelection(value.length());


            }
        });
        userList = (RecyclerView) findViewById(R.id.userList);
        userList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        userList.setAdapter(userSelectListAdapter);

        showProgress(false);

        if (restaurant_id == null) {
            startActivityForResult(PlaceSearchActivity.getCallIntent(context), SEARCH_PLACE);
        }

    }

    private float updateRating() {

        float finalRating = 0;
        int rating = ratingBar.getRating();
        MyLg.e(TAG, "Change Rating Value " + rating);

        if (rating == 0) {
            ratingValue.setText("-");
            return finalRating;
        } else {
            finalRating = 1;

            int calRating = rating - 1;
            float tempRating = (float) calRating / 2;
            finalRating += tempRating;

            ratingValue.setText(""+finalRating+"");
        }

        return finalRating;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == SEARCH_PLACE) {

                restaurant_branch_id = data.getStringExtra(Constant.ID);
                restaurant_id = data.getStringExtra(Constant.RESTAURANT_ID);
                restaurant_name = data.getStringExtra(Constant.NAME);

            }

        } else {
            finish();
        }

    }

    private void checkText(String s) {
        if (s.contains("@")) {
            userHolder.setVisibility(View.VISIBLE);
            buttonHolder.setVisibility(View.GONE);

            s = s + " ";

            String[] allTexts = s.split("@");
            String search = allTexts[allTexts.length - 1];

            searchUser(search.trim());

            MyLg.e(TAG, "Search User " + search);

        } else {
            userHolder.setVisibility(View.GONE);
            buttonHolder.setVisibility(View.VISIBLE);
        }
    }

    private void searchUser(String search) {

        if (search.equals("")) {
            userHint.setText("Start Tying name");
            users = new ArrayList<>();
            userSelectListAdapter.refresh(users);
        } else {
            if (users.size() == 0) {
                userHint.setText("Fetching List");
            }
            getUsers(search);
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

    private void getUsers(String search) {
        this.search = search;
        checkQuery();
    }

    private void getUserSearch() {

        if (search != null && !search.equals("")) {

            if (BuildConfig.NETWORK_TEST && Build.VERSION.SDK_INT >= 14) {
                try {
                    TrafficStats.setThreadStatsTag(USER_INITIATED_REQUEST);
                    // Once tag has been applied, network request has to be made request
                } finally {
                    TrafficStats.clearThreadStatsTag();
                }
            }

            RetroInterface.getZomatoRestApi().getUserSearch(
                    SessionPreference.getUserId(context)+"",
                    search,
                    new Callback<UserListResponse>() {
                        @Override
                        public void success(UserListResponse userListResponse, Response response) {

                            if (userListResponse != null) {
                                users = userListResponse.getUsers();
                                userSelectListAdapter.refresh(users);

                                if (users.size() != 0) {
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

    private void showImageHolder() {
        if (imageList.size() != 0) {
            imageHolder.setVisibility(View.VISIBLE);
        } else {
            imageHolder.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        setRestaurantDetails();
        resetAdapter();
    }

    private void setRestaurantDetails() {
        if (restaurant_name != null) {
            title.setText(restaurant_name);
        }
    }

    private void resetAdapter() {
        imageList = DaoController.getPhotoItems(context);
        adapter.refresh(imageList);
        showImageHolder();
    }

    public void closeClick(View view) {
        finish();
    }

    public void addPhoto(View view) {
        startActivity(PickPhotosActivity.getCallIntent(context, PickPhotosActivity.TYPE_SELECT));
    }

    public void nextClick(View view) {
        updateComplete();
    }

    private void updateComplete() {

        showProgress(true);

        List<PhotoItem> allList = DaoController.getRemainingPhotoItems(context);
        MyLg.e(TAG, "Upload Remaining " + allList.size());
        if (allList.size() != 0) {

            PhotoItem item = allList.get(0);

            String finalName = "Photo" + new Date().getTime() + "U" + SessionPreference.getUserId(context) + ".jpg";
            String path = item.getPath();
            String tag = item.getTag();

            item.setName(finalName);
            item.setStatus(true);
            DaoController.addPhotoItem(context, item);

            MyLg.e(TAG, "Name " + finalName + " tag " + tag);

            ImagePreference.setParams(context, finalName, path);
            if (!ServiceCheck.isMyServiceRunning(context, UploadImageService.class)) {
                startService(new Intent(context, UploadImageService.class));
            }
        } else {
            uploadComplete();
        }

    }

    private void uploadComplete() {

        List<PhotoItem> allList = DaoController.getPhotoItems(context);
        Toas.show(context, "Upload Complete");

        JSONArray array = new JSONArray();

        try {

            MyLg.e(TAG, "Update Size" + allList.size());

            for (int i = 0; i < allList.size(); i++) {

                MyLg.e(TAG, "Update Name" + allList.get(i).getName());

                JSONObject object = new JSONObject();
                object.put(Constant.IMAGE, allList.get(i).getName());
                object.put(Constant.TAGS, allList.get(i).getTag());
                array.put(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        MyLg.e(TAG, "Update Array " + array.toString());

        String data = array.toString();
        String reviewData = yourReview.getText().toString();

        RetroInterface.getZomatoRestApi().addRestaurantReview(
                SessionPreference.getUserId(context) + "",
                restaurant_id,
                restaurant_branch_id,
                updateRating()+"",
                reviewData+"",
                data,
                new Callback<MealDetailResponse>() {
                    @Override
                    public void success(MealDetailResponse mealDetailResponse, Response response) {
                        showProgress(false);
                        List<PhotoItem> items = new ArrayList<PhotoItem>();
                        DaoController.addPhotoItems(context, items);
                        finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        showProgress(false);
                    }
                }
        );


    }

    private void showProgress(boolean b) {
        if (b) {
            findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);
        }
    }


    public void tagFriend(View view) {
        yourReview.append("@");
    }

    public void onEvent(ImageUploadItem item) {
        MyLg.e(TAG, "Image Complete Received");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateComplete();
            }
        }, 200);

    }


    @Override
    protected void onStart() {
        super.onStart();
        MyLg.e(TAG, "onStart");
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
