package com.androcid.zomato.view.adapter.profile;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androcid.zomato.R;
import com.androcid.zomato.model.RestaurantImage;
import com.androcid.zomato.model.RestaurantItem;
import com.androcid.zomato.model.ReviewItem;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.util.CommonFunctions;
import com.androcid.zomato.util.MyFont;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileReviewAdapter extends RecyclerView.Adapter<ProfileReviewAdapter.ViewHolder> {

    Context context;
    List<ReviewItem> mList;
    MyFont myFont;
    //FOR NEW ACTIVITY
    private ClickListener clickListener;

    public ProfileReviewAdapter(Context context, List<ReviewItem> list) {
        this.mList = list;
        this.context = context;
        myFont = new MyFont(context);
    }

    @Override
    public ProfileReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_review, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProfileReviewAdapter.ViewHolder holder, final int position) {

        ReviewItem item = mList.get(position);

        RestaurantItem restaurantItem = item.getRestaurantItem();

        holder.place_name.setText(restaurantItem.getName());
        holder.place_location.setText(restaurantItem.getLocation());

        holder.created_time.setText(CommonFunctions.getTimeByCurrentTimeZone(CommonFunctions.Date_yyyyMMddHHmmss,
                item.getCreated_date()));
        holder.dineline_rating.setText("" + item.getRating());

        List<RestaurantImage> images = item.getRestaurantImages();

        if (images != null && images.size() != 0) {
            holder.review_image_lay.setVisibility(View.VISIBLE);

            holder.image1_holder.setVisibility(View.INVISIBLE);
            holder.image2_holder.setVisibility(View.INVISIBLE);
            holder.image3_holder.setVisibility(View.INVISIBLE);
            holder.image4_holder.setVisibility(View.INVISIBLE);
            holder.image5_holder.setVisibility(View.INVISIBLE);

            for (int i = 0; i < images.size(); i++) {
                showImage(holder, images.get(i), i, images.size());
                if (i == 4)
                    break;
            }

        } else {
            holder.review_image_lay.setVisibility(View.GONE);
        }


        if (!CommonFunctions.checkNull(restaurantItem.getImage()).equals("")) {
            Picasso.with(context)
                    .load(RetroInterface.IMAGE_URL + restaurantItem.getImage())
                    .resize(200, 200)
                    .placeholder(R.drawable.placeholder_200)
                    .error(R.drawable.placeholder_200)
                    .into(holder.place_image);
        }

        holder.review_details.setText(item.getDescription());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (clickListener != null) {
                    clickListener.onItemClickListener(view, position);
                }

            }
        });

    }

    private void showImage(ViewHolder holder, RestaurantImage restaurantImage, int i, int size) {

        switch (i) {
            case 0:
                holder.image1_holder.setVisibility(View.VISIBLE);
                if (!CommonFunctions.checkNull(restaurantImage.getImage()).equals("")) {
                    Picasso.with(context)
                            .load(RetroInterface.IMAGE_URL + restaurantImage.getImage())
                            .resize(200, 200)
                            .placeholder(R.drawable.placeholder_200)
                            .error(R.drawable.placeholder_200)
                            .into(holder.image1);
                }
                break;
            case 1:
                holder.image2_holder.setVisibility(View.VISIBLE);
                if (!CommonFunctions.checkNull(restaurantImage.getImage()).equals("")) {
                    Picasso.with(context)
                            .load(RetroInterface.IMAGE_URL + restaurantImage.getImage())
                            .resize(200, 200)
                            .placeholder(R.drawable.placeholder_200)
                            .error(R.drawable.placeholder_200)
                            .into(holder.image2);
                }
                break;
            case 2:
                holder.image3_holder.setVisibility(View.VISIBLE);
                if (!CommonFunctions.checkNull(restaurantImage.getImage()).equals("")) {
                    Picasso.with(context)
                            .load(RetroInterface.IMAGE_URL + restaurantImage.getImage())
                            .resize(200, 200)
                            .placeholder(R.drawable.placeholder_200)
                            .error(R.drawable.placeholder_200)
                            .into(holder.image3);
                }
                break;
            case 3:
                holder.image4_holder.setVisibility(View.VISIBLE);
                if (!CommonFunctions.checkNull(restaurantImage.getImage()).equals("")) {
                    Picasso.with(context)
                            .load(RetroInterface.IMAGE_URL + restaurantImage.getImage())
                            .resize(200, 200)
                            .placeholder(R.drawable.placeholder_200)
                            .error(R.drawable.placeholder_200)
                            .into(holder.image4);
                }
                break;
            case 4:
                holder.image5_holder.setVisibility(View.VISIBLE);
                if (!CommonFunctions.checkNull(restaurantImage.getImage()).equals("")) {
                    Picasso.with(context)
                            .load(RetroInterface.IMAGE_URL + restaurantImage.getImage())
                            .resize(200, 200)
                            .placeholder(R.drawable.placeholder_200)
                            .error(R.drawable.placeholder_200)
                            .into(holder.image5);
                }
                break;
        }

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void refresh(List<ReviewItem> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        public void onItemClickListener(View v, int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //PLACE DETAILS
        ImageView place_image;
        TextView place_name, place_location;

        //TIMING
        LinearLayout timing_lay;
        LinearLayout rating_view;
        TextView created_time;
        TextView dineline_rating;

        //FOR REVIEW
        LinearLayout review_lay;
        TextView review_details;
        LinearLayout review_image_lay;
        CardView image1_holder, image2_holder, image3_holder, image4_holder, image5_holder;
        ImageView image1, image2, image3, image4, image5;

        //FOR OPTIONS
        LinearLayout options_lay;

        public ViewHolder(View itemView) {
            super(itemView);

            place_image = (ImageView) itemView.findViewById(R.id.place_image);
            place_name = (TextView) itemView.findViewById(R.id.place_name);
            place_location = (TextView) itemView.findViewById(R.id.place_location);

            //TIMING
            timing_lay = (LinearLayout) itemView.findViewById(R.id.timing_lay);
            rating_view = (LinearLayout) itemView.findViewById(R.id.rating_view);
            created_time = (TextView) itemView.findViewById(R.id.created_time);
            dineline_rating = (TextView) itemView.findViewById(R.id.dineline_rating);

            //FOR REVIEW
            review_lay = (LinearLayout) itemView.findViewById(R.id.review_lay);
            review_details = (TextView) itemView.findViewById(R.id.review_details);
            review_image_lay = (LinearLayout) itemView.findViewById(R.id.review_image_lay);
            image1_holder = (CardView) itemView.findViewById(R.id.image1_holder);
            image2_holder = (CardView) itemView.findViewById(R.id.image2_holder);
            image3_holder = (CardView) itemView.findViewById(R.id.image3_holder);
            image4_holder = (CardView) itemView.findViewById(R.id.image4_holder);
            image5_holder = (CardView) itemView.findViewById(R.id.image5_holder);
            image1 = (ImageView) itemView.findViewById(R.id.image1);
            image2 = (ImageView) itemView.findViewById(R.id.image2);
            image3 = (ImageView) itemView.findViewById(R.id.image3);
            image4 = (ImageView) itemView.findViewById(R.id.image4);
            image5 = (ImageView) itemView.findViewById(R.id.image5);

            //FOR OPTIONS
            options_lay = (LinearLayout) itemView.findViewById(R.id.options_lay);


            myFont.setAppFont((ViewGroup) itemView, MyFont.FONT_REGULAR);
            //myFont.setFont(name, MyFont.FONT_BOLD);

        }
    }

}
