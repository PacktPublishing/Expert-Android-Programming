package com.androcid.zomato.activity.review;

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
import com.androcid.zomato.view.adapter.ReviewDraftsAdapter;

import java.util.ArrayList;
import java.util.List;


public class ReviewDraftActivity extends AppCompatActivity {

    private static final String TAG = ReviewDraftActivity.class.getSimpleName();
    private Context context = ReviewDraftActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, ReviewDraftActivity.class);
        return intent;
    }

    RecyclerView recyclerView;
    List<String> allList;
    ReviewDraftsAdapter allAdapter;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_draft);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Review Drafts");
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        allList = new ArrayList<>();
        allAdapter = new ReviewDraftsAdapter(context, allList);
        allAdapter.setClickListener(new ReviewDraftsAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                gotoDetailsActivity();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(allAdapter);

        setList();
    }

    private void setList() {

        for (int i = 0; i < 10; i++) {
            allList.add("Item " + i);
        }
        allAdapter.refresh(allList);

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

    private void gotoDetailsActivity() {

        startActivity(AddReviewActivity.getCallIntent(context));

    }

}
