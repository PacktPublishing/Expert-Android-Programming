package com.androcid.zomato.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androcid.zomato.R;
import com.androcid.zomato.activity.address.AddressBookActivity;
import com.androcid.zomato.activity.base.HomeBaseActivity;
import com.androcid.zomato.activity.friend.FindFriendActivity;
import com.androcid.zomato.activity.login.StartActivity;
import com.androcid.zomato.activity.profile.EditProfileActivity;
import com.androcid.zomato.activity.profile.UserProfileActivity;
import com.androcid.zomato.activity.review.ReviewDraftActivity;
import com.androcid.zomato.activity.support.SupportActivity;
import com.androcid.zomato.model.User;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.util.CircleTransform;
import com.androcid.zomato.util.FireBase;
import com.squareup.picasso.Picasso;


/**
 *
 */
public class AccountActivity extends HomeBaseActivity {

    private static final String TAG = AccountActivity.class.getSimpleName();
    private Context context = AccountActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, AccountActivity.class);
        return intent;
    }

    //User Profile Details
    ImageView userImage;
    TextView userName;

    FireBase fireBase = FireBase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        userImage = (ImageView) findViewById(R.id.userImage);
        userName = (TextView) findViewById(R.id.userName);

        setUserDetails();


    }

    private void setUserDetails() {

        User user = SessionPreference.getUser(context);
        if (user != null) {

            userName.setText(user.getName());

            if (user.getImage() != null && !user.getImage().equals("")) {
                Picasso.with(context)
                        .load(user.getImage())
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.placeholder_200)
                        .error(R.drawable.placeholder_200)
                        .into(userImage);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseBottomView(SCREEN_ACCOUNT);
    }

    public void onOptionCLick(View view) {
        switch (view.getId()) {
            case R.id.bookmarks:
                startActivity(BookmarkActivity.getCallIntent(context));
                break;

            case R.id.leaderboard:
                startActivity(LeaderBoardActivity.getCallIntent(context));
                break;

            case R.id.notifications:
                startActivity(NotificationActivity.getCallIntent(context));
                break;

        }
    }

    public void onlineOrderClick(View view) {

        switch (view.getId()) {
            case R.id.address_book:
                startActivity(AddressBookActivity.getCallIntent(context));
                break;
            case R.id.zomato_credits:
                startActivity(CreditsActivity.getCallIntent(context));
                break;
            case R.id.free_meals:
                startActivity(FreeMealActivity.getCallIntent(context));
                break;
            case R.id.chat_support:
                startActivity(SupportActivity.getCallIntent(context));
                break;
        }

    }

    public void profileClick(View view) {


        switch (view.getId()) {
            case R.id.edit_profile:
                startActivity(EditProfileActivity.getCallIntent(context));
                break;
            case R.id.connected_accounts:
                startActivity(ConnectedAccountsActivity.getCallIntent(context));
                break;
            case R.id.recent_restaurants:
                startActivity(RecentPlaceActivity.getCallIntent(context));
                break;
            case R.id.review_drafts:
                startActivity(ReviewDraftActivity.getCallIntent(context));
                break;
        }
    }

    public void otherClick(View view) {
        switch (view.getId()) {
            case R.id.zomato_friend:
                startActivity(FindFriendActivity.getCallIntent(context));
                break;
            case R.id.add_place:
                startActivity(AddPlaceActivity.getCallIntent(context));
                break;
            case R.id.sign_out:
                SessionPreference.clear(context);
                FireBase.getInstance().signOut();
                startActivity(StartActivity.getCallIntent(context));
                finish();
                break;
        }
    }

    public void myProfileClick(View view) {

        String user_id = SessionPreference.getUserId(context)+"";
        startActivity(UserProfileActivity.getCallIntent(context, user_id));
    }
}