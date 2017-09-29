package com.androcid.zomato.view.pageradapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.androcid.zomato.model.RestaurantImage;
import com.androcid.zomato.view.fragment.ImageFragment;
import com.androcid.zomato.view.fragment.ImageZoomFragment;

import java.util.List;


public class ImagePagerAdapter extends FragmentPagerAdapter {

    public static final int ZOOM = 1;
    public static final int NORMAL = 2;

    private static final String TAG = ImagePagerAdapter.class.getSimpleName();

    private int type;
    List<RestaurantImage> list;
    int PAGE_COUNT;

    public ImagePagerAdapter(FragmentManager fm, List<RestaurantImage> list, int type) {
        super(fm);
        this.list = list;
        this.type = type;
        PAGE_COUNT = list.size();
    }

    /**
     * This method will be invoked when a page is requested to create
     */
    @Override
    public Fragment getItem(int pos) {

        RestaurantImage contentItem = list.get(pos);
        if(type == ZOOM) {
            return ImageZoomFragment.newInstance(contentItem, pos);
        }
        return ImageFragment.newInstance(contentItem, pos);
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return POSITION_NONE;
    }

    /**
     * Returns the number of pages
     */
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page #" + (position + 1);
    }

}