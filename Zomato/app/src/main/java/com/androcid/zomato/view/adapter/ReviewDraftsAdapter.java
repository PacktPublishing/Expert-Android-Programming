package com.androcid.zomato.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androcid.zomato.R;
import com.androcid.zomato.util.MyFont;

import java.util.List;


/**
 * Created by Androcid on 12/27/2016.
 */
public class ReviewDraftsAdapter extends RecyclerView.Adapter<ReviewDraftsAdapter.ViewHolder> {

    private static final String TAG = ReviewDraftsAdapter.class.getSimpleName();
    Context context;
    MyFont myFont;
    private List<String> list;
    private ClickListener clickListener;

    public ReviewDraftsAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
        myFont = new MyFont(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_review_drafts, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String item = list.get(position);

        //TODO EXTRA
        holder.name.setText("Place name " + position);
        holder.location.setText("Draft " + position);


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

    public void refresh(List<String> list) {
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

        TextView name;
        TextView location;


        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            location = (TextView) itemView.findViewById(R.id.location);
            myFont.setAppFont((ViewGroup) itemView, MyFont.FONT_REGULAR);
            myFont.setFont(name, MyFont.FONT_BOLD);

        }
    }

}
