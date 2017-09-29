package com.androcid.zomato.activity.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androcid.zomato.R;
import com.androcid.zomato.activity.SelectLocationActivity;
import com.androcid.zomato.model.ProfileDetailItem;
import com.androcid.zomato.model.User;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.NormalResponse;
import com.androcid.zomato.retro.ProfileDetailResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.util.CircleTransform;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = EditProfileActivity.class.getSimpleName();
    private Context context = EditProfileActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, EditProfileActivity.class);
        return intent;
    }

    ImageView user_image;
    EditText userName, phoneNo, description;
    TextView location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        user_image = (ImageView) findViewById(R.id.user_image);
        userName = (EditText) findViewById(R.id.userName);
        phoneNo = (EditText) findViewById(R.id.phoneNo);
        description = (EditText) findViewById(R.id.description);
        location = (TextView) findViewById(R.id.location);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SelectLocationActivity.getCallIntent(context));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserProfile();
    }

    private void getUserProfile() {

        RetroInterface.getZomatoRestApi().getProfileDetails(
                SessionPreference.getUserId(context) + "",
                SessionPreference.getUserId(context) + "",
                new Callback<ProfileDetailResponse>() {
                    @Override
                    public void success(ProfileDetailResponse profileDetailResponse, Response response) {

                        if (profileDetailResponse != null && profileDetailResponse.getItems() != null) {
                            ProfileDetailItem detailItem = profileDetailResponse.getItems();

                            setUserDetails(detailItem.getUser());
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }

        );

    }

    private void setUserDetails(User item) {

        if (item.getImage() != null && !item.getImage().equals("")) {

            Picasso.with(context)
                    .load(item.getImage())
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.placeholder_200)
                    .error(R.drawable.placeholder_200)
                    .into(user_image);

        }


        userName.setText(item.getName());
        phoneNo.setText(item.getPhone_no());
        description.setText(item.getDescription());
        location.setText(item.getLocation());
    }

    public void closeClick(View view) {
        onBackPressed();
    }

    public void onDoneClick(View view) {

        String user_name = userName.getText().toString();
        String user_phone_no = phoneNo.getText().toString();
        String user_description = description.getText().toString();

        RetroInterface.getZomatoRestApi().setUserDetails(
                SessionPreference.getUserId(context) + "",
                user_name,
                user_phone_no,
                user_description,
                new Callback<NormalResponse>() {
                    @Override
                    public void success(NormalResponse normalResponse, Response response) {
                        finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );

    }
}
