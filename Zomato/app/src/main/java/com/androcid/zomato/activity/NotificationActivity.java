package com.androcid.zomato.activity;

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
import com.androcid.zomato.view.adapter.NotificationAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = NotificationActivity.class.getSimpleName();
    private Context context = NotificationActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, NotificationActivity.class);
        return intent;
    }


    RecyclerView recyclerView;
    List<String> allList;
    NotificationAdapter allAdapter;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notifications");
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        allList = new ArrayList<>();
        allAdapter = new NotificationAdapter(context, allList);
        allAdapter.setClickListener(new NotificationAdapter.ClickListener() {
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

        for (int i = 0; i < 30; i++) {
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

        startActivity(PlaceDetailActivity.getCallIntent(context, null));

    }

    public void openPreference(View view) {
        startActivity(NotificationPreferenceActivity.getCallIntent(context));
    }
}