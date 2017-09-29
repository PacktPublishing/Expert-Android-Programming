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
import com.androcid.zomato.model.SaveItem;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.retro.SaveResponse;
import com.androcid.zomato.util.CommonFunctions;
import com.androcid.zomato.util.MyFont;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Androcid on 12/27/2016.
 */
public class RecentViewedPlacesAdapter extends RecyclerView.Adapter<RecentViewedPlacesAdapter.ViewHolder> {

    private static final String TAG = RecentViewedPlacesAdapter.class.getSimpleName();
    Context context;
    MyFont myFont;
    private List<RestaurantItem> list;
    private ClickListener clickListener;

    public RecentViewedPlacesAdapter(Context context, List<RestaurantItem> list) {
        this.list = list;
        this.context = context;
        myFont = new MyFont(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recent_viewed_places, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        RestaurantItem item = list.get(position);

        holder.name.setText(CommonFunctions.checkNull(item.getName()));
        holder.location.setText(CommonFunctions.checkNull(item.getLocation()));

        if (!CommonFunctions.checkNull(item.getImage()).equals("")) {
            Picasso.with(context)
                    .load(RetroInterface.IMAGE_URL + item.getImage())
                    .resize(200, 200)
                    .placeholder(R.drawable.placeholder_200)
                    .error(R.drawable.placeholder_200)
                    .into(holder.image);
        }

        if (item.isBookmark()) {
            holder.bookmark.setImageResource(R.drawable.ic_bookmarked);
            holder.bookmark.setSelected(true);
        } else {
            holder.bookmark.setImageResource(R.drawable.ic_bookmark);
            holder.bookmark.setSelected(false);
        }

        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestaurantItem restaurantItem = list.get(position);
                boolean isBookmark = restaurantItem.isBookmark();
                saveBookmark(position, isBookmark?1:0);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (clickListener != null) {
                    clickListener.onItemClickListener(view, position);
                }

            }
        });

    }

    private void saveBookmark(final int pos, int save) {

        RestaurantItem restaurantItem = list.get(pos);

        RetroInterface.getZomatoRestApi().bookmarkRestaurant(
                SessionPreference.getUserId(context) + "",
                restaurantItem.getId() + "",
                save + "",
                new Callback<SaveResponse>() {
                    @Override
                    public void success(SaveResponse normalResponse, Response response) {

                        if (normalResponse != null && normalResponse.getItems() != null) {

                            try {
                                SaveItem item = normalResponse.getItems();
                                if (item.isStatus()) {
                                    list.get(pos).setBookmark(true);
                                } else {
                                    list.get(pos).setBookmark(false);
                                }
                                notifyItemChanged(pos);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }

        );
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

        ImageView image;
        TextView name;
        TextView location;
        ImageView bookmark;

        public ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            location = (TextView) itemView.findViewById(R.id.location);
            bookmark = (ImageView) itemView.findViewById(R.id.bookmark);
            myFont.setAppFont((ViewGroup) itemView, MyFont.FONT_REGULAR);
            myFont.setFont(name, MyFont.FONT_BOLD);

        }
    }

}
