package com.androcid.zomato.activity.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androcid.zomato.R;
import com.androcid.zomato.activity.level.StatisticsActivity;
import com.androcid.zomato.model.ProfileDetailItem;
import com.androcid.zomato.model.User;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.ProfileDetailResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.util.CircleTransform;
import com.androcid.zomato.util.Constant;
import com.androcid.zomato.view.fragment.profile.ProfileDinelineFragment;
import com.androcid.zomato.view.fragment.profile.ProfilePhotoFragment;
import com.androcid.zomato.view.fragment.profile.ProfileReviewFragment;
import com.androcid.zomato.view.pageradapter.ViewPagerAdapter;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 *
 */
public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = UserProfileActivity.class.getSimpleName();
    private Context context = UserProfileActivity.this;
    private String setHandle;

    public static Intent getCallIntent(Context context, String user_id) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(Constant.USER_ID, user_id);
        return intent;
    }

    ViewPager viewPager;

    //USER DETAILS
    String user_id;
    TextView title;
    ImageView user_image;
    TextView user_name, user_location;

    //COUNT
    TextView followerCount, beenthereCount, collectionCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        user_id = getIntent().getStringExtra(Constant.USER_ID);

        title = (TextView) findViewById(R.id.title);
        user_image = (ImageView) findViewById(R.id.user_image);
        user_name = (TextView) findViewById(R.id.user_name);
        user_location = (TextView) findViewById(R.id.user_location);

        followerCount = (TextView) findViewById(R.id.followerCount);
        beenthereCount = (TextView) findViewById(R.id.beenthereCount);
        collectionCount = (TextView) findViewById(R.id.collectionCount);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    private void setUserDetails(User item) {

        if (item != null) {
            user_name.setText(item.getName());
            if (item.getImage() != null && !item.getImage().equals("")) {

                Picasso.with(context)
                        .load(item.getImage())
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.placeholder_200)
                        .error(R.drawable.placeholder_200)
                        .into(user_image);

            }
            user_location.setText(item.getLocation());

            setHandle = item.getHandle();
            if (item.getHandle() != null && !item.getHandle().equals("")) {
                title.setText("@"+item.getHandle());
            } else {
                if (item.getId() == SessionPreference.getUserId(context)) {
                    title.setText("set handle");
                } else {
                    title.setText(item.getName());
                }
            }


        }
    }

    private void getUserProfile() {

        RetroInterface.getZomatoRestApi().getProfileDetails(
                SessionPreference.getUserId(context) + "",
                user_id,
                new Callback<ProfileDetailResponse>() {
                    @Override
                    public void success(ProfileDetailResponse profileDetailResponse, Response response) {

                        if (profileDetailResponse != null && profileDetailResponse.getItems() != null) {
                            ProfileDetailItem detailItem = profileDetailResponse.getItems();

                            setUserDetails(detailItem.getUser());

                            followerCount.setText(detailItem.getFollower_count() + "");
                            beenthereCount.setText(detailItem.getBeenthere_count() + "");
                            collectionCount.setText(detailItem.getCollection_count() + "");
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }

        );

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(ProfileDinelineFragment.newInstance(user_id), "DINELINE");
        adapter.addFrag(ProfileReviewFragment.newInstance(user_id), "REVIEWS");
        adapter.addFrag(ProfilePhotoFragment.newInstance(user_id), "PHOTOS");
        viewPager.setAdapter(adapter);
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
        getUserProfile();
    }

    public void followerClick(View view) {
        startActivity(FollowerActivity.getCallIntent(context, user_id));
    }

    public void collectionClick(View view) {
        startActivity(UserCollectionActivity.getCallIntent(context, user_id));
    }

    public void beenThereClick(View view) {
        startActivity(BeenThereActivity.getCallIntent(context, user_id));
    }

    public void statisticsClick(View view) {
        startActivity(StatisticsActivity.getCallIntent(context));
    }

    public void setHandleClicked(View view) {

        if (user_id.equals(SessionPreference.getUserId(context) + "")) {
            if (setHandle != null && setHandle.equals("")){
                startActivity(SetHandleActivity.getCallIntent(context));
            }
        }

    }

    public void closeClick(View view) {
        finish();
    }
}