package com.androcid.zomato.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.androcid.zomato.R;
import com.androcid.zomato.activity.base.HomeBaseActivity;
import com.androcid.zomato.view.fragment.FeedFragment;
import com.androcid.zomato.view.pageradapter.ViewPagerAdapter;


/**
 *
 */
public class FeedActivity extends HomeBaseActivity {

    private static final String TAG = FeedActivity.class.getSimpleName();
    ViewPager viewPager;
    private Context context = FeedActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, FeedActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FeedFragment(), "LOCAL FEED");
        adapter.addFrag(new FeedFragment(), "MY FEED");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseBottomView(SCREEN_FEED);
    }
}