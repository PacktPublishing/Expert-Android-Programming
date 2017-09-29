package com.androcid.zomato.activity.photos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.androcid.zomato.R;
import com.androcid.zomato.dao.DaoController;
import com.androcid.zomato.model.RestaurantImage;
import com.androcid.zomato.util.MyFont;
import com.androcid.zomato.util.MyLg;
import com.androcid.zomato.view.pageradapter.ImagePagerAdapter;
import com.androcid.zomato.view.pageradapter.transform.ScaleInOutTransformer;

import java.util.List;


public class PhotosPagerActivity extends AppCompatActivity {

    private static final String TAG = PhotosPagerActivity.class.getSimpleName();
    private Context context = PhotosPagerActivity.this;

    MyFont myFont;
    List<RestaurantImage> contentItemList;

    public static Intent getCallIntent(Context context, int pos) {
        Intent intent = new Intent(context, PhotosPagerActivity.class);
        intent.putExtra("pos", pos);
        return intent;
    }

    int pos;
    ViewPager pager;
    ImagePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        myFont = new MyFont(context);
        myFont.setAppFont((ViewGroup) findViewById(R.id.root), MyFont.FONT_REGULAR);
        pos = getIntent().getIntExtra("pos", 0);

        contentItemList = DaoController.getRestaurantImages(context);

        MyLg.e(TAG, "Size " + contentItemList.size());
        pager = (ViewPager) findViewById(R.id.myViewPager);
        adapter = new ImagePagerAdapter(getSupportFragmentManager(), contentItemList, ImagePagerAdapter.ZOOM);

        pager.setAdapter(adapter);
        pager.setPageTransformer(true, new ScaleInOutTransformer());

        pager.setCurrentItem(pos);
    }

}
