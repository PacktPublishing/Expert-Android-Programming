package com.androcid.zomato.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androcid.zomato.R;
import com.androcid.zomato.model.ReviewItem;
import com.androcid.zomato.util.CircleTransform;
import com.androcid.zomato.util.MyFont;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewPlaceAdapter extends RecyclerView.Adapter<ReviewPlaceAdapter.ViewHolder> {

    Context context;
    List<ReviewItem> mList;
    MyFont myFont;
    //FOR NEW ACTIVITY
    private ClickListener clickListener;

    public ReviewPlaceAdapter(Context context, List<ReviewItem> list) {
        this.mList = list;
        this.context = context;
        myFont = new MyFont(context);
    }

    @Override
    public ReviewPlaceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review_place, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewPlaceAdapter.ViewHolder holder, final int position) {

        holder.name.setText("User " + position);
       // holder.location.setText("Location " + position);

        Picasso.with(context)
                .load(R.drawable.im_backdrop)
                .transform(new CircleTransform())
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

        ImageView image;
        TextView name;
        TextView location;

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

        public ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            image1 = (ImageView) itemView.findViewById(R.id.image1);
            image2 = (ImageView) itemView.findViewById(R.id.image2);
            image3 = (ImageView) itemView.findViewById(R.id.image3);
            image4 = (ImageView) itemView.findViewById(R.id.image4);
            image5 = (ImageView) itemView.findViewById(R.id.image5);

            myFont.setAppFont((ViewGroup) itemView, MyFont.FONT_REGULAR);
            myFont.setFont(name, MyFont.FONT_BOLD);

        }
    }

}
