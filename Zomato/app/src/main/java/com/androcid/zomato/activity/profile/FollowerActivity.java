package com.androcid.zomato.activity.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.androcid.zomato.R;
import com.androcid.zomato.util.Constant;
import com.androcid.zomato.view.fragment.follow.FollowerFragment;
import com.androcid.zomato.view.fragment.follow.FollowingFragment;
import com.androcid.zomato.view.pageradapter.ViewPagerAdapter;


/**
 *
 */
public class FollowerActivity extends AppCompatActivity {

    private static final String TAG = FollowerActivity.class.getSimpleName();
    private Context context = FollowerActivity.this;

    public static Intent getCallIntent(Context context, String user_id) {
        Intent intent = new Intent(context, FollowerActivity.class);
        intent.putExtra(Constant.USER_ID, user_id);
        return intent;
    }
    String user_id;

    Toolbar toolbar;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("User Name");
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));

        user_id = getIntent().getStringExtra(Constant.USER_ID);

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
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(FollowerFragment.newInstance(user_id), "FOLLOWERS");
        adapter.addFrag(FollowingFragment.newInstance(user_id), "FOLLOWING");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}