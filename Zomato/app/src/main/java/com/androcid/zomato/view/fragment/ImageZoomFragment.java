package com.androcid.zomato.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androcid.zomato.R;
import com.androcid.zomato.model.RestaurantImage;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.util.MyLg;
import com.squareup.picasso.Picasso;

/**
 * Created by Androcid on 21-01-2016.
 */
public class ImageZoomFragment extends Fragment {

    private static final String TAG = ImageZoomFragment.class.getSimpleName();
    private Context context;

    RestaurantImage contentItem;
    int pos;


    public static ImageZoomFragment newInstance(RestaurantImage contentItem, int pos) {
        ImageZoomFragment f = new ImageZoomFragment();
        Bundle args = new Bundle();
        args.putSerializable("content", contentItem);
        args.putInt("pos", pos);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        contentItem = (RestaurantImage) data.getSerializable("content");
        pos = data.getInt("pos" , 0);
    }

    //FOR ACTUAL DATA VIEW AND RECEIVER
    ImageView content_image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_zoom_images, container, false);
        content_image = (ImageView) v.findViewById(R.id.content_image);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();

        String image = contentItem.getImage();

        if(!image.equals("")){

            String url = RetroInterface.IMAGE_URL+image;
            Picasso.with(context)
                    .load(url)
                    .resize(1000,1000)
                    .centerInside()
                    .error(R.drawable.placeholder_200)
                    .into(content_image);


            MyLg.e(TAG, "Image Url " + url);
        }

    }
}
