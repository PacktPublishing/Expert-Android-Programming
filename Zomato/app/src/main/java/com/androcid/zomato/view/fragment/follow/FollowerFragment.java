package com.androcid.zomato.view.fragment.follow;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androcid.zomato.BuildConfig;
import com.androcid.zomato.R;
import com.androcid.zomato.model.User;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.retro.UserListResponse;
import com.androcid.zomato.util.Constant;
import com.androcid.zomato.view.adapter.FriendAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.androcid.zomato.util.Constant.APP_INITIATED_REQUEST;

/**
 * Created by Androcid on 30-12-2016.
 */

public class FollowerFragment extends Fragment {

    Context context;
    RecyclerView recyclerView;
    FriendAdapter recyclerViewAdapter;
    List<User> list;
    private String user_id;

    public static Fragment newInstance(String user_id) {
        FollowerFragment fragment = new FollowerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.USER_ID, user_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);
        user_id = getArguments().getString(Constant.USER_ID);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.context = getActivity();

        list = new ArrayList<>();
        recyclerViewAdapter = new FriendAdapter(getActivity(), list);
        /*recyclerViewAdapter.setClickListener(new FriendAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                gotoDetailsActivity();
            }

            @Override
            public void onFriendListener(int pos, boolean isFollowing) {

            }
        });*/

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);

        getFollowers();
    }

    private void getFollowers() {

        if (BuildConfig.NETWORK_TEST && Build.VERSION.SDK_INT >= 14) {
            try {
                TrafficStats.setThreadStatsTag(APP_INITIATED_REQUEST);
                // Once tag has been applied, network request has to be made request
            } finally {
                TrafficStats.clearThreadStatsTag();
            }
        }

        RetroInterface.getZomatoRestApi().getUserFollowers(
                SessionPreference.getUserId(context) + "",
                user_id + "",
                new Callback<UserListResponse>() {
                    @Override
                    public void success(UserListResponse userListResponse, Response response) {
                        if(userListResponse!=null){
                            list = userListResponse.getUsers();
                            recyclerViewAdapter.refresh(list);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );

    }

    private void gotoDetailsActivity() {

       // startActivity(UserProfileActivity.getCallIntent(context));

    }
}
