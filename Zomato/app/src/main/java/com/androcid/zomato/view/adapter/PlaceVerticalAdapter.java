package com.androcid.zomato.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androcid.zomato.R;
import com.androcid.zomato.model.PlaceDisplayItem;
import com.androcid.zomato.model.RestaurantItem;
import com.androcid.zomato.model.MealTypeItem;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.util.CommonFunctions;
import com.androcid.zomato.util.MyFont;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Androcid on 12/27/2016.
 */
public class PlaceVerticalAdapter extends RecyclerView.Adapter<PlaceVerticalAdapter.ViewHolder> {

    public static final int TYPE_HEADER = 101;
    public static final int TYPE_ITEM = 102;
    public static final int TYPE_MORE = 103;
    private static final String TAG = PlaceVerticalAdapter.class.getSimpleName();
    Context context;
    MyFont myFont;
    private List<PlaceDisplayItem> list;
    //FOR NEW ACTIVITY
    private ClickListener clickListener;

    public PlaceVerticalAdapter(Context context, List<PlaceDisplayItem> list) {
        this.list = list;
        this.context = context;
        myFont = new MyFont(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = null;
        if (viewType == TYPE_ITEM) {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_place_vertical, viewGroup, false);
        } else if (viewType == TYPE_MORE) {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_place_more, viewGroup, false);
        } else if (viewType == TYPE_HEADER) {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_place_header, viewGroup, false);
        }

        return new ViewHolder(itemView);
    }

    //added a method that returns viewType for a given position
    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        PlaceDisplayItem displayItem = list.get(position);

        if (displayItem.getType() == TYPE_ITEM) {
            RestaurantItem item = displayItem.getRestaurantItem();

            //TODO EXTRA
            String name = item.getName() != null ? item.getName() : "";
            String description = item.getDescription() != null ? item.getDescription() : "";
            String location = item.getLocation() != null ? item.getLocation() : "";

            holder.name.setText(name);
            holder.location.setText(location);
            holder.description.setText(description);

            holder.rating.setText(CommonFunctions.formatRating(item.getRating()));
            holder.distance.setText(CommonFunctions.formatDistance(item.getDistance()));

            if (!CommonFunctions.checkNull(item.getImage()).equals("")) {
                Picasso.with(context)
                        .load(RetroInterface.IMAGE_URL+item.getImage())
                        .resize(200,200)
                        .placeholder(R.drawable.placeholder_200)
                        .error(R.drawable.placeholder_200)
                        .into(holder.image);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.onItemClickListener(view, position);
                    }
                }
            });

        } else {
            if (displayItem.getType() == TYPE_HEADER) {
                MealTypeItem item = displayItem.getMealTypeItem();
                holder.name.setText(item.getName());
                holder.description.setText(item.getDescription());
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void refresh(List<PlaceDisplayItem> list) {
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

        ImageView image;

        TextView name;
        TextView location;
        TextView description;

        TextView rating;
        TextView distance;

        public ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image);

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
