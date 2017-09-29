package com.androcid.zomato.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androcid.zomato.R;
import com.androcid.zomato.model.RestaurantItem;
import com.androcid.zomato.util.MyFont;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Androcid on 12/27/2016.
 */
public class PlaceDetailsAdapter extends RecyclerView.Adapter<PlaceDetailsAdapter.ViewHolder> {

    private static final String TAG = PlaceDetailsAdapter.class.getSimpleName();
    Context context;
    MyFont myFont;
    private List<RestaurantItem> list;
    //FOR NEW ACTIVITY
    private ClickListener clickListener;

    public PlaceDetailsAdapter(Context context, List<RestaurantItem> list) {
        this.list = list;
        this.context = context;
        myFont = new MyFont(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_place_details, viewGroup, false);
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

        holder.name.setText(name);
        holder.location.setText(location);
        holder.description.setText(description);

        holder.rating.setText(item.getRating() + "");
        holder.distance.setText(item.getDistance()+"");

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


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (clickListener != null) {
                    clickListener.onItemClickListener(view, position);
                }

            }
        });

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

        ImageView image1;
        ImageView image2;
        ImageView image3;
        ImageView image4;

        TextView name;
        TextView location;
        TextView description;

        TextView rating;
        TextView distance;

        public ViewHolder(View itemView) {
            super(itemView);

            image1 = (ImageView) itemView.findViewById(R.id.image1);
            image2 = (ImageView) itemView.findViewById(R.id.image2);
            image3 = (ImageView) itemView.findViewById(R.id.image3);
            image4 = (ImageView) itemView.findViewById(R.id.image4);

            name = (TextView) itemView.findViewById(R.id.name);
            location = (TextView) itemView.findViewById(R.id.location);
            description = (TextView) itemView.findViewById(R.id.description);

            rating = (TextView) itemView.findViewById(R.id.rating);
            distance = (TextView) itemView.findViewById(R.id.distance);

            myFont.setAppFont((ViewGroup) itemView, MyFont.FONT_REGULAR);
            myFont.setFont(name, MyFont.FONT_BOLD);

        }
    }

}
