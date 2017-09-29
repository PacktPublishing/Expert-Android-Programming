package com.androcid.zomato.view.fragment.collection;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androcid.zomato.R;
import com.androcid.zomato.activity.collection.CollectionDetailActivity;
import com.androcid.zomato.model.CollectionItem;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.CollectionResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.view.adapter.CollectionAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Androcid on 30-12-2016.
 */

public class SavedCollectionFragment extends Fragment {

    Context context;
    List<CollectionItem> items;
    RecyclerView recyclerView;
    CollectionAdapter recyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.context = getActivity();

        items = new ArrayList<>();

        recyclerViewAdapter = new CollectionAdapter(getActivity(), items);
        recyclerViewAdapter.setClickListener(new CollectionAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                gotoDetailsActivity(pos);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        getAllCollections();
    }


    private void getAllCollections() {

        RetroInterface.getZomatoRestApi().getSavedCollection(
                SessionPreference.getUserId(context) + "",
                new Callback<CollectionResponse>() {
                    @Override
                    public void success(CollectionResponse collectionResponse, Response response) {

                        if (collectionResponse != null && collectionResponse.isSuccess()) {
                            items = collectionResponse.getItems();
                            recyclerViewAdapter.refresh(items);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );

    }


    private void gotoDetailsActivity(int pos) {
        CollectionItem item = items.get(pos);
        startActivity(CollectionDetailActivity.getCallIntent(context, item));
    }
}
