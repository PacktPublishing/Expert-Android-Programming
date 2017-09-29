package com.androcid.zomato.activity.friend;

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
import com.androcid.zomato.model.User;
import com.androcid.zomato.view.adapter.FriendAdapter;

import java.util.ArrayList;
import java.util.List;


public class FriendFacebookActivity extends AppCompatActivity {

    private static final String TAG = FriendFacebookActivity.class.getSimpleName();
    private Context context = FriendFacebookActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, FriendFacebookActivity.class);
        return intent;
    }

    RecyclerView recyclerView;
    List<User> allList;
    FriendAdapter allAdapter;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_search);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Find friends on Facebook");
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        allList = new ArrayList<>();
        allAdapter = new FriendAdapter(context, allList);
        /*
        allAdapter.setClickListener(new FriendAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {

            }

            @Override
            public void onFriendListener(int pos, boolean isFollowing) {

            }
        });*/

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(allAdapter);

        setList();
    }

    private void setList() {


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

    public void addFriendFacebook(View view) {

    }

    public void addFindZomato(View view) {

    }
}
