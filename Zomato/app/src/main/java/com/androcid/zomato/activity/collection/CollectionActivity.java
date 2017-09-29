package com.androcid.zomato.activity.collection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.androcid.zomato.R;
import com.androcid.zomato.activity.base.HomeBaseActivity;
import com.androcid.zomato.view.fragment.collection.FollowingCollectionFragment;
import com.androcid.zomato.view.fragment.collection.MyCollectionFragment;
import com.androcid.zomato.view.fragment.collection.SavedCollectionFragment;
import com.androcid.zomato.view.fragment.collection.SuggestedCollectionFragment;
import com.androcid.zomato.view.pageradapter.ViewPagerAdapter;


/**
 *
 */
public class CollectionActivity extends HomeBaseActivity {

    private static final String TAG = CollectionActivity.class.getSimpleName();
    ViewPager viewPager;
    private Context context = CollectionActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, CollectionActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

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
        adapter.addFrag(new SuggestedCollectionFragment(), "HANDPICKED");
        adapter.addFrag(new SavedCollectionFragment(), "SAVED");
        adapter.addFrag(new FollowingCollectionFragment(), "FOLLOWING");
        adapter.addFrag(new MyCollectionFragment(), "MINE");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseBottomView(SCREEN_COLLECTION);
    }
}