package com.androcid.zomato.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androcid.zomato.R;
import com.androcid.zomato.model.BucketItem;
import com.androcid.zomato.util.MyFont;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.util.List;


/**
 * Created by Androcid on 12/27/2016.
 */
public class BucketAdapter extends RecyclerView.Adapter<BucketAdapter.ViewHolder> {

    private static final String TAG = BucketAdapter.class.getSimpleName();
    Context context;
    MyFont myFont;
    private List<BucketItem> list;
    private ClickListener clickListener;

    public BucketAdapter(Context context, List<BucketItem> list) {
        this.list = list;
        this.context = context;
        myFont = new MyFont(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bucket, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        BucketItem item = list.get(position);

        holder.title.setText(item.getName());
        holder.count.setText(item.getCount() + "");

       // MyLg.e(TAG, "Value "+item.getSelected());

        if (item.getSelected() == 0) {
            holder.selected.setVisibility(View.GONE);
        } else {
            holder.selected.setVisibility(View.VISIBLE);
            holder.selected.setText(item.getSelected() + " selected");
        }

        /*Picasso.with(context)
                .load(getPath(item.getPath()))
                .resize(200,200)
                .placeholder(R.drawable.placeholder_200)
                .error(R.drawable.placeholder_200)
                .into(holder.image);*/

        Ion.with(context)
                .load(getPath(item.getPath()))
                .intoImageView(holder.image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (clickListener != null) {
                    clickListener.onItemClickListener(view, position);
                }

            }
        });

    }

    private File getPath(String path) {

       // MyLg.e(TAG, "path " + path);
        try {
            return new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void refresh(List<BucketItem> list) {
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
        TextView title, selected, count;


        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            selected = (TextView) itemView.findViewById(R.id.selected);
            count = (TextView) itemView.findViewById(R.id.count);

        }
    }

}
