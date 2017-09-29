package com.androcid.zomato.activity.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.androcid.zomato.R;
import com.androcid.zomato.activity.collection.AddCollectionActivity;
import com.androcid.zomato.activity.collection.CollectionDetailActivity;
import com.androcid.zomato.model.CollectionItem;
import com.androcid.zomato.retro.CollectionResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.util.Constant;
import com.androcid.zomato.view.adapter.CollectionAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 *
 */
public class UserCollectionActivity extends AppCompatActivity {

    private static final String TAG = UserCollectionActivity.class.getSimpleName();
    private Context context = UserCollectionActivity.this;

    public static Intent getCallIntent(Context context, String user_id) {
        Intent intent = new Intent(context, UserCollectionActivity.class);
        intent.putExtra(Constant.USER_ID, user_id);
        return intent;
    }
    String user_id;

    RecyclerView recyclerView;
    List<CollectionItem> allList;
    CollectionAdapter allAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_collection);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Collections");
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));

        user_id = getIntent().getStringExtra(Constant.USER_ID);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        allList = new ArrayList<>();
        allAdapter = new CollectionAdapter(context, allList);
        allAdapter.setClickListener(new CollectionAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                CollectionItem item = allList.get(pos);
                startActivity(CollectionDetailActivity.getCallIntent(context, item));
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(allAdapter);

        getAllCollections();

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

    private void getAllCollections() {

        RetroInterface.getZomatoRestApi().getMyCollection(
                user_id + "",
                new Callback<CollectionResponse>() {
                    @Override
                    public void success(CollectionResponse collectionResponse, Response response) {

                        if(collectionResponse!=null && collectionResponse.isSuccess()){
                            allList = collectionResponse.getItems();
                            allAdapter.refresh(allList);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );

    }

    public void createCollection(View view) {
        startActivity(AddCollectionActivity.getCallIntent(context));
    }
}