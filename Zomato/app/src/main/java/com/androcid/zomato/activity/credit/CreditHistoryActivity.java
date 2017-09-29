package com.androcid.zomato.activity.credit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.androcid.zomato.R;
import com.androcid.zomato.model.RestaurantImage;
import com.androcid.zomato.view.adapter.PhotosAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class CreditHistoryActivity extends AppCompatActivity {

    private static final String TAG = CreditHistoryActivity.class.getSimpleName();
    ViewPager viewPager;
    private Context context = CreditHistoryActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, CreditHistoryActivity.class);
        return intent;
    }

    Toolbar toolbar;
    //Might Like
    List<RestaurantImage> allList;
    PhotosAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_history);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Credit History");
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));

        allList = new ArrayList<>();

        adapter = new PhotosAdapter(context, allList);
        adapter.setClickListener(new PhotosAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                //gotoDetailsActivity();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.setAdapter(adapter);

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
    }
}