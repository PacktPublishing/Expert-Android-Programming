package com.androcid.zomato.view.pageradapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.androcid.zomato.view.appbarlayout.base.ObservableFragment;
import com.androcid.zomato.view.appbarlayout.base.ObservablePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Androcid on 30-12-2016.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter implements ObservablePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public ObservableFragment getObservableFragment(int position) {
        return (ObservableFragment) mFragmentList.get(position);
    }
}