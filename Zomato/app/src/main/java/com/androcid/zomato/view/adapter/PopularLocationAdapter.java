package com.androcid.zomato.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androcid.zomato.R;
import com.androcid.zomato.model.room.UserLocation;
import com.androcid.zomato.util.MyFont;

import java.util.List;


/**
 * Created by Androcid on 12/27/2016.
 */
public class PopularLocationAdapter extends RecyclerView.Adapter<PopularLocationAdapter.ViewHolder> {

    private static final String TAG = PopularLocationAdapter.class.getSimpleName();
    Context context;
    MyFont myFont;
    private List<UserLocation> list;
    //FOR NEW ACTIVITY
    private ClickListener clickListener;

    public PopularLocationAdapter(Context context, List<UserLocation> list) {
        this.list = list;
        this.context = context;
        myFont = new MyFont(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_popular_location, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        UserLocation item = list.get(position);

        //TODO EXTRA
        String name = item.getName() != null ? item.getName() : "";
        String location = "";//item.getAddress() != null ? item.getAddress() : "";

        holder.name.setText(name);
        holder.location.setText(location);

        holder.card_view.setPreventCornerOverlap(false);


        holder.card_view.setOnClickListener(new View.OnClickListener() {
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

    public void refresh(List<UserLocation> list) {
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

        CardView card_view;
        TextView name;
        TextView location;

        public ViewHolder(View itemView) {
            super(itemView);

            card_view = (CardView) itemView.findViewById(R.id.card_view);
            name = (TextView) itemView.findViewById(R.id.name);
            location = (TextView) itemView.findViewById(R.id.location);

            myFont.setAppFont((ViewGroup) itemView, MyFont.FONT_REGULAR);
            myFont.setFont(name, MyFont.FONT_BOLD);

        }
    }

}
