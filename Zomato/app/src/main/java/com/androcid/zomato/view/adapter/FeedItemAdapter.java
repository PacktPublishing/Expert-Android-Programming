package com.androcid.zomato.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androcid.zomato.R;
import com.androcid.zomato.model.RestaurantItem;
import com.androcid.zomato.util.CircleTransform;
import com.androcid.zomato.util.MyFont;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Androcid on 12/27/2016.
 */
public class FeedItemAdapter extends RecyclerView.Adapter<FeedItemAdapter.ViewHolder> {

    private static final String TAG = FeedItemAdapter.class.getSimpleName();
    Context context;
    MyFont myFont;
    private List<RestaurantItem> list;
    //FOR NEW ACTIVITY
    private ClickListener clickListener;

    public FeedItemAdapter(Context context, List<RestaurantItem> list) {
        this.list = list;
        this.context = context;
        myFont = new MyFont(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_feed, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        RestaurantItem item = list.get(position);

        //TODO EXTRA
        String name = item.getName() != null ? item.getName() : "";
        String description = item.getDescription() != null ? item.getDescription() : "";
        String location = item.getLocation() != null ? item.getLocation() : "";
        String placeType = item.getType() != null ? item.getType() : "";

        /*
        holder.name.setText(name);
        holder.location.setText(location);
        holder.description.setText(description);

        holder.rating.setText(item.getRating() + "");
        holder.distance.setText(item.getDistance());*/

        Picasso.with(context)
                .load(R.drawable.im_backdrop)
                .placeholder(R.drawable.placeholder_200)
                .error(R.drawable.placeholder_200)
                .into(holder.placeImage);

        Picasso.with(context)
                .load(R.drawable.profile_img)
                .transform(new CircleTransform())
                .placeholder(R.drawable.placeholder_200)
                .error(R.drawable.placeholder_200)
                .into(holder.userImage);

        Picasso.with(context)
                .load(R.drawable.im_backdrop)
                .placeholder(R.drawable.placeholder_200)
                .error(R.drawable.placeholder_200)
                .into(holder.image);

        Picasso.with(context)
                .load(R.drawable.im_backdrop)
                .placeholder(R.drawable.placeholder_200)
                .error(R.drawable.placeholder_200)
                .into(holder.image1);

        Picasso.with(context)
                .load(R.drawable.im_backdrop)
                .placeholder(R.drawable.placeholder_200)
                .error(R.drawable.placeholder_200)
                .into(holder.image2);

        Picasso.with(context)
                .load(R.drawable.im_backdrop)
                .placeholder(R.drawable.placeholder_200)
                .error(R.drawable.placeholder_200)
                .into(holder.image3);

        Picasso.with(context)
                .load(R.drawable.im_backdrop)
                .placeholder(R.drawable.placeholder_200)
                .error(R.drawable.placeholder_200)
                .into(holder.image4);

        Picasso.with(context)
                .load(R.drawable.im_backdrop)
                .placeholder(R.drawable.placeholder_200)
                .error(R.drawable.placeholder_200)
                .into(holder.image5);


        if (position % 2 == 0) {
            holder.followPLace.setImageResource(R.drawable.ic_bookmark_border_white_18dp);
            holder.followPLace.setSelected(true);
        } else {
            holder.followPLace.setImageResource(R.drawable.ic_bookmark_border_white_18dp);
            holder.followPLace.setSelected(false);
        }

        if (position % 2 == 0) {
            holder.followUser.setImageResource(R.drawable.ic_bookmark_border_white_18dp);
            holder.followUser.setSelected(true);
        } else {
            holder.followUser.setImageResource(R.drawable.ic_bookmark_border_white_18dp);
            holder.followUser.setSelected(false);
        }

       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (clickListener != null) {
                    clickListener.onItemClickListener(view, position);
                }

            }
        });
*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void refresh(List<RestaurantItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        public void onItemClickListener(View v, int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //Place
        ImageView placeImage;
        TextView placeName;
        TextView placeDescription;
        ImageView followPLace;

        //User
        ImageView userImage;
        TextView userName;
        TextView userDescription;
        ImageView followUser;

        TextView createdTime;

        //Main Image
        ImageView image;
        ImageView imageLike;
        TextView imageLikeCount;

        ImageView image1;
        ImageView image1Like;
        ImageView image2;
        ImageView image2Like;
        ImageView image3;
        ImageView image3Like;
        ImageView image4;
        ImageView image4Like;
        ImageView image5;
        ImageView image5Like;


        CardView shareLay;
        CardView commentLay;
        CardView likeLay;

        public ViewHolder(View itemView) {
            super(itemView);

            placeImage = (ImageView) itemView.findViewById(R.id.placeImage);
            placeName = (TextView) itemView.findViewById(R.id.placeName);
            placeDescription = (TextView) itemView.findViewById(R.id.placeDescription);
            followPLace = (ImageView) itemView.findViewById(R.id.followPLace);

            userImage = (ImageView) itemView.findViewById(R.id.userImage);
            userName = (TextView) itemView.findViewById(R.id.userName);
            userDescription = (TextView) itemView.findViewById(R.id.userDescription);
            followUser = (ImageView) itemView.findViewById(R.id.followUser);

            createdTime = (TextView) itemView.findViewById(R.id.createdTime);

            image = (ImageView) itemView.findViewById(R.id.image);
            image1 = (ImageView) itemView.findViewById(R.id.image1);
            image2 = (ImageView) itemView.findViewById(R.id.image2);
            image3 = (ImageView) itemView.findViewById(R.id.image3);
            image4 = (ImageView) itemView.findViewById(R.id.image4);
            image5 = (ImageView) itemView.findViewById(R.id.image5);


            myFont.setAppFont((ViewGroup) itemView, MyFont.FONT_REGULAR);
            myFont.setFont(placeName, MyFont.FONT_BOLD);
            myFont.setFont(userName, MyFont.FONT_BOLD);

        }
    }

}
