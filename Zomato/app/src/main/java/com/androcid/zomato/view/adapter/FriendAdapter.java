package com.androcid.zomato.view.adapter;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androcid.zomato.BuildConfig;
import com.androcid.zomato.R;
import com.androcid.zomato.activity.profile.UserProfileActivity;
import com.androcid.zomato.model.User;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.NormalResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.util.CircleTransform;
import com.androcid.zomato.util.MyFont;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.androcid.zomato.util.Constant.USER_INITIATED_REQUEST;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    Context context;
    List<User> mList;
    MyFont myFont;
    int user_id;
    //FOR NEW ACTIVITY

    public FriendAdapter(Context context, List<User> list) {
        this.mList = list;
        this.context = context;
        user_id = SessionPreference.getUserId(context);
        myFont = new MyFont(context);
    }

    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FriendAdapter.ViewHolder holder, final int position) {

        User item = mList.get(position);

        holder.userName.setText(item.getName());
        holder.userDescription.setText(context.getString(R.string.txt_user_details, item.getReview_count(), item.getFollower_count()));

        if(item.getImage()!=null && !item.getImage().equals("")) {
            Picasso.with(context)
                    .load(item.getImage())
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.placeholder_200)
                    .error(R.drawable.placeholder_200)
                    .into(holder.userImage);
        }else{
            Picasso.with(context)
                    .load(R.drawable.placeholder_200)
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.placeholder_200)
                    .error(R.drawable.placeholder_200)
                    .into(holder.userImage);
        }

        if(item.getId() == user_id){
            holder.followUser.setVisibility(View.INVISIBLE);
        }else {
            holder.followUser.setVisibility(View.VISIBLE);

            if (item.isFollowing()) {
                holder.followUser.setImageResource(R.drawable.ic_added_user);
                holder.followUser.setSelected(true);
            } else {
                holder.followUser.setImageResource(R.drawable.ic_add_user);
                holder.followUser.setSelected(false);
            }
        }

        holder.followUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if(clickListener!=null)
                {
                    User user = mList.get(position);
                    boolean isFollowing = user.isFollowing();

                    //clickListener.onFriendListener(position, !isFollowing);
                    followUser(position, !isFollowing);

                    user.setFollowing(!isFollowing);
                    notifyItemChanged(position);

                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if (clickListener != null)
                {
                    showProfileDetails(position);
                    //clickListener.onItemClickListener(view, position);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /*

    private ClickListener clickListener;
    public interface ClickListener {
        public void onItemClickListener(View v, int pos);
        public void onFriendListener(int pos, boolean isFollowing);
    }
    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }
    */

    public void refresh(List<User> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView userName;
        TextView userDescription;
        ImageView followUser;

        public ViewHolder(View itemView) {
            super(itemView);

            userImage = (ImageView) itemView.findViewById(R.id.userImage);
            userName = (TextView) itemView.findViewById(R.id.userName);
            userDescription = (TextView) itemView.findViewById(R.id.userDescription);
            followUser = (ImageView)  itemView.findViewById(R.id.followUser);

            myFont.setAppFont((ViewGroup) itemView, MyFont.FONT_REGULAR);
            myFont.setFont(userName, MyFont.FONT_BOLD);

        }
    }

    //SERVER CALLS
    private void followUser(int pos, boolean isFollowing) {

        if (BuildConfig.NETWORK_TEST && Build.VERSION.SDK_INT >= 14) {
            try {
                TrafficStats.setThreadStatsTag(USER_INITIATED_REQUEST);
                // Once tag has been applied, network request has to be made request
            } finally {
                TrafficStats.clearThreadStatsTag();
            }
        }

        User user = mList.get(pos);
        RetroInterface.getZomatoRestApi().followUser(
                SessionPreference.getUserId(context) + "",
                user.getId() + "",
                (isFollowing ? 1 : 0) + "",
                new Callback<NormalResponse>() {
                    @Override
                    public void success(NormalResponse userListResponse, Response response) {

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );
    }

    private void showProfileDetails(int pos) {
        User user = mList.get(pos);
        context.startActivity(UserProfileActivity.getCallIntent(context, user.getId() + ""));
    }

}
