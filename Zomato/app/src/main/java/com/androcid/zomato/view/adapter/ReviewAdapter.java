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

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    Context context;
    List<ReviewItem> mList;
    MyFont myFont;
    //FOR NEW ACTIVITY
    private ClickListener clickListener;

    public ReviewAdapter(Context context, List<ReviewItem> list) {
        this.mList = list;
        this.context = context;
        myFont = new MyFont(context);
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder holder, final int position) {

        ReviewItem item = mList.get(position);

        holder.user_name.setText(item.getUser().getName());
        holder.description.setText(item.getDescription());

        Picasso.with(context)
                .load(R.drawable.im_backdrop)
                .transform(new CircleTransform())
                .placeholder(R.drawable.placeholder_200)
                .error(R.drawable.placeholder_200)
                .into(holder.user_image);


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

        ImageView user_image;
        TextView user_name;
        TextView user_details;

        TextView created_time;
        TextView rating;
        TextView description;


        public ViewHolder(View itemView) {
            super(itemView);

            user_image = (ImageView) itemView.findViewById(R.id.user_image);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            user_details = (TextView) itemView.findViewById(R.id.user_details);

            created_time = (TextView) itemView.findViewById(R.id.created_time);
            rating = (TextView) itemView.findViewById(R.id.rating);
            description = (TextView) itemView.findViewById(R.id.description);


            myFont.setAppFont((ViewGroup) itemView, MyFont.FONT_REGULAR);
            myFont.setFont(user_name, MyFont.FONT_BOLD);

        }
    }

}
