package com.androcid.zomato.activity.review;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.androcid.zomato.R;
import com.androcid.zomato.view.fragment.ReviewFragment;
import com.androcid.zomato.view.pageradapter.ViewPagerAdapter;


/**
 *
 */
public class AllReviewActivity extends AppCompatActivity {

    private static final String TAG = AllReviewActivity.class.getSimpleName();
    ViewPager viewPager;
    private Context context = AllReviewActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, AllReviewActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reviews);

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
        adapter.addFrag(new ReviewFragment(), "POPULAR");
        adapter.addFrag(new ReviewFragment(), "FOLLOWING");
        adapter.addFrag(new ReviewFragment(), "ALL");
        adapter.addFrag(new ReviewFragment(), "BLOGS");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}