package com.androcid.zomato.view.fragment.profile;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androcid.zomato.R;
import com.androcid.zomato.model.ReviewItem;
import com.androcid.zomato.retro.ProfileReviewResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.util.Constant;
import com.androcid.zomato.view.adapter.profile.ProfileReviewAdapter;
import com.androcid.zomato.view.appbarlayout.SmoothAppBarLayout;
import com.androcid.zomato.view.appbarlayout.base.ObservableFragment;
import com.androcid.zomato.view.appbarlayout.base.Utils;
import com.androcid.zomato.view.recyclerview.SimpleRecyclerViewAdapter;
import com.androcid.zomato.view.recyclerview.holder.HeaderHolder;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Androcid on 30-12-2016.
 */

public class ProfileReviewFragment extends Fragment implements ObservableFragment {

    protected static final String ARG_HEADER_LAYOUT = "ARG_HEADER_LAYOUT";

    public static Fragment newInstance(String user_id) {
        return newInstance(R.layout.item_header_view_pager_parallax_spacing, user_id);
    }

    public static Fragment newInstance(@LayoutRes int headerLayout, String user_id) {
        ProfileReviewFragment fragment = new ProfileReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_HEADER_LAYOUT, headerLayout);
        bundle.putString(Constant.USER_ID, user_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    String user_id;
    List<ReviewItem> reviewItems;
    ProfileReviewAdapter reviewAdapter;
    RecyclerView recyclerView;

    private int mHeaderLayout;

    @Override
    public View getScrollTarget() {
        return recyclerView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_dineline, container, false);
        mHeaderLayout = getArguments().getInt(ARG_HEADER_LAYOUT);
        user_id = getArguments().getString(Constant.USER_ID);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public boolean onOffsetChanged(SmoothAppBarLayout smoothAppBarLayout, View target, int verticalOffset) {
        return Utils.syncOffset(smoothAppBarLayout, target, verticalOffset, getScrollTarget());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reviewItems = new ArrayList<>();
        reviewAdapter = new ProfileReviewAdapter(getActivity(), reviewItems);
        reviewAdapter.setClickListener(new ProfileReviewAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {

            }
        });

        RecyclerView.Adapter adapter = new SimpleRecyclerViewAdapter(reviewAdapter) {
            @Override
            public RecyclerView.ViewHolder onCreateFooterViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
                return null;
            }

            @Override
            public RecyclerView.ViewHolder onCreateHeaderViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
                return new HeaderHolder(layoutInflater, viewGroup, mHeaderLayout);
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        getUserReviews();

    }

    private void getUserReviews() {

        RetroInterface.getZomatoRestApi().getUserReviews(
                user_id,
                new Callback<ProfileReviewResponse>() {
                    @Override
                    public void success(ProfileReviewResponse restaurantDetailResponse, Response response) {

                        if(restaurantDetailResponse!=null)
                        {
                            reviewItems = restaurantDetailResponse.getItems();
                            reviewAdapter.refresh(reviewItems);
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );


    }

}
