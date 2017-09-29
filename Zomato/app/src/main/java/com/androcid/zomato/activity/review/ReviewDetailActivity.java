package com.androcid.zomato.activity.review;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.androcid.zomato.R;
import com.androcid.zomato.util.CircleTransform;
import com.squareup.picasso.Picasso;


public class ReviewDetailActivity extends AppCompatActivity {

    private static final String TAG = ReviewDetailActivity.class.getSimpleName();
    private Context context = ReviewDetailActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, ReviewDetailActivity.class);
        return intent;
    }

    Toolbar toolbar;

    //Place
    ImageView placeImage;

    //Main Image
    ImageView image;

    ImageView image1;
    ImageView image2;
    ImageView image3;
    ImageView image4;
    ImageView image5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Review Details");
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));

        placeImage = (ImageView) findViewById(R.id.placeImage);
        image = (ImageView) findViewById(R.id.image);
        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);
        image4 = (ImageView) findViewById(R.id.image4);
        image5 = (ImageView) findViewById(R.id.image5);

        Picasso.with(context)
                .load(R.drawable.im_backdrop)
                .placeholder(R.drawable.placeholder_200)
                .error(R.drawable.placeholder_200)
                .into(placeImage);

        Picasso.with(context)
                .load(R.drawable.profile_img)
                .transform(new CircleTransform())
                .placeholder(R.drawable.placeholder_200)
                .error(R.drawable.placeholder_200)
                .into(image);

        Picasso.with(context)
                .load(R.drawable.im_backdrop)
                .placeholder(R.drawable.placeholder_200)
                .error(R.drawable.placeholder_200)
                .into(image1);

        Picasso.with(context)
                .load(R.drawable.im_backdrop)
                .placeholder(R.drawable.placeholder_200)
                .error(R.drawable.placeholder_200)
                .into(image2);

        Picasso.with(context)
                .load(R.drawable.im_backdrop)
                .placeholder(R.drawable.placeholder_200)
                .error(R.drawable.placeholder_200)
                .into(image3);

        Picasso.with(context)
                .load(R.drawable.im_backdrop)
                .placeholder(R.drawable.placeholder_200)
                .error(R.drawable.placeholder_200)
                .into(image4);

        Picasso.with(context)
                .load(R.drawable.im_backdrop)
                .placeholder(R.drawable.placeholder_200)
                .error(R.drawable.placeholder_200)
                .into(image5);

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

}
