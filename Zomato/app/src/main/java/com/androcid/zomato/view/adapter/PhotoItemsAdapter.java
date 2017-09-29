package com.androcid.zomato.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.androcid.zomato.R;
import com.androcid.zomato.model.PhotoItem;
import com.androcid.zomato.util.CommonFunctions;
import com.androcid.zomato.util.MyFont;
import com.androcid.zomato.util.MyLg;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.util.List;


/**
 * Created by Androcid on 12/27/2016.
 */
public class PhotoItemsAdapter extends RecyclerView.Adapter<PhotoItemsAdapter.ViewHolder> {

    private static final String TAG = PhotoItemsAdapter.class.getSimpleName();
    Context context;
    MyFont myFont;
    private List<PhotoItem> list;
    private ClickListener clickListener;

    public PhotoItemsAdapter(Context context, List<PhotoItem> list) {
        this.list = list;
        this.context = context;
        myFont = new MyFont(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_photo_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PhotoItem item = list.get(position);

        /*Picasso.with(context)
                .load(R.drawable.im_backdrop)
                .placeholder(R.drawable.placeholder_200)
                .error(R.drawable.placeholder_200)
                .into(holder.image);*/

        Ion.with(context)
                .load(getPath(item.getPath()))
                .intoImageView(holder.image);

        holder.tag.setText(CommonFunctions.checkNull(item.getTag()));
        holder.tag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();

                if (!text.equals(list.get(position).getTag())) {
                    list.get(position).setTag(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (clickListener != null) {
                    clickListener.onRemoveClickListener(view, position);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private File getPath(String path) {

        MyLg.e(TAG, "path " + path);
        try {
            return new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void refresh(List<PhotoItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        public void onRemoveClickListener(View v, int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        ImageView remove;
        EditText tag;


        public ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image);
            remove = (ImageView) itemView.findViewById(R.id.remove);
            tag = (EditText) itemView.findViewById(R.id.tag);

        }
    }

}
