package com.androcid.zomato.activity.friend;

import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.androcid.zomato.BuildConfig;
import com.androcid.zomato.R;
import com.androcid.zomato.activity.profile.UserProfileActivity;
import com.androcid.zomato.model.User;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.NormalResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.retro.UserListResponse;
import com.androcid.zomato.view.adapter.FriendAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.androcid.zomato.util.Constant.APP_INITIATED_REQUEST;
import static com.androcid.zomato.util.Constant.USER_INITIATED_REQUEST;


public class FindFriendActivity extends AppCompatActivity {

    private static final String TAG = FindFriendActivity.class.getSimpleName();
    private Context context = FindFriendActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, FindFriendActivity.class);
        return intent;
    }

    RecyclerView recyclerView;
    List<User> allList;
    FriendAdapter allAdapter;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Find foodies to follow");
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        allList = new ArrayList<>();
        allAdapter = new FriendAdapter(context, allList);

        /*
        allAdapter.setClickListener(new FriendAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                showProfileDetails(pos);
            }

            @Override
            public void onFriendListener(int pos, boolean isFollowing) {
                followUser(pos, isFollowing);
            }
        });*/

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(allAdapter);

        setList();
    }

    private void followUser(int pos, boolean isFollowing) {

        if (BuildConfig.NETWORK_TEST && Build.VERSION.SDK_INT >= 14) {
            try {
                TrafficStats.setThreadStatsTag(USER_INITIATED_REQUEST);
                // Once tag has been applied, network request has to be made request
            } finally {
                TrafficStats.clearThreadStatsTag();
            }
        }

        User user = allList.get(pos);
        RetroInterface.getZomatoRestApi().followUser(
                SessionPreference.getUserId(context) + "",
                user.getId() + "",
                (isFollowing ? 1 : 0) + "",
                new Callback<NormalResponse>() {
                    @Override
                    public void success(NormalResponse userListResponse, Response response) {

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );
    }

    private void showProfileDetails(int pos) {
        User user = allList.get(pos);
        startActivity(UserProfileActivity.getCallIntent(context, user.getId() + ""));
    }

    private void setList() {

        if (BuildConfig.NETWORK_TEST && Build.VERSION.SDK_INT >= 14) {
            try {
                TrafficStats.setThreadStatsTag(APP_INITIATED_REQUEST);
                // Once tag has been applied, network request has to be made request
            } finally {
                TrafficStats.clearThreadStatsTag();
            }
        }

        RetroInterface.getZomatoRestApi().getUserSuggestion(
                SessionPreference.getUserId(context) + "",
                new Callback<UserListResponse>() {
                    @Override
                    public void success(UserListResponse userListResponse, Response response) {

                        if (userListResponse != null) {

                            allList = userListResponse.getUsers();
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

    public void addFriendFacebook(View view) {
        startActivity(FriendFacebookActivity.getCallIntent(context));
    }

    public void addFindZomato(View view) {
        startActivity(FriendZomatoActivity.getCallIntent(context));

    }
}
