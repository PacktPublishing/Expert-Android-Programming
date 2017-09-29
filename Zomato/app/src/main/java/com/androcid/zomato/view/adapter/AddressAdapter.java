package com.androcid.zomato.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androcid.zomato.R;
import com.androcid.zomato.model.UserAddress;
import com.androcid.zomato.util.MyFont;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    Context context;
    List<UserAddress> list;
    MyFont myFont;
    //FOR NEW ACTIVITY
    private ClickListener clickListener;

    public AddressAdapter(Context context, List<UserAddress> list) {
        this.list = list;
        this.context = context;
        myFont = new MyFont(context);
    }

    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_address, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AddressAdapter.ViewHolder holder, final int position) {

        UserAddress item = list.get(position);


        holder.name.setText(item.getType());
        holder.location.setText(item.getAddress() + ", " + item.getLocation_name());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (clickListener != null) {
                    clickListener.onItemClickListener(view, position);
                }
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void refresh(List<UserAddress> list) {
        this.list = list;
        notifyDataSetChanged();
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
            //myFont.setFont(name, MyFont.FONT_BOLD);

        }
    }

}
