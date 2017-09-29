package com.androcid.zomato.activity.address;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.androcid.zomato.R;
import com.androcid.zomato.model.UserAddress;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.retro.UserAddressResponse;
import com.androcid.zomato.view.adapter.AddressAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class AddressBookActivity extends AppCompatActivity {

    private static final String TAG = AddressBookActivity.class.getSimpleName();
    private Context context = AddressBookActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, AddressBookActivity.class);
        return intent;
    }

    RecyclerView recyclerView;
    List<UserAddress> userAddresses;
    AddressAdapter addressAdapter;

    LinearLayout no_address_view;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Address Book");
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        userAddresses = new ArrayList<>();
        addressAdapter = new AddressAdapter(context, userAddresses);
        addressAdapter.setClickListener(new AddressAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                gotoDetailsActivity(pos);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(addressAdapter);

        no_address_view = (LinearLayout) findViewById(R.id.no_address_view);
        no_address_view.setVisibility(View.INVISIBLE);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserAddresses();
    }

    private void getUserAddresses() {

        RetroInterface.getZomatoRestApi().getUserAddress(
                SessionPreference.getUserId(context) + "",
                new Callback<UserAddressResponse>() {
                    @Override
                    public void success(UserAddressResponse userAddressResponse, Response response) {
                        if (userAddressResponse != null) {

                            userAddresses = userAddressResponse.getUserAddresses();
                            addressAdapter.refresh(userAddresses);

                            if (userAddresses.size() == 0) {
                                no_address_view.setVisibility(View.VISIBLE);
                            } else {
                                no_address_view.setVisibility(View.INVISIBLE);
                            }

                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoDetailsActivity(final int pos) {

        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            final CharSequence[] items = {
                    "Edit", "Delete"
            };

            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    switch (which) {
                        case 0:
                            openEditAddress(pos);
                            break;
                        case 1:

                            AlertDialog.Builder builderInner = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                            builderInner.setMessage("Are you sure you want to delete this address");
                            builderInner.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteAddress(pos);
                                    dialog.dismiss();
                                }
                            });
                            builderInner.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builderInner.show();

                            break;
                    }

                }
            });
            builder.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteAddress(int pos) {
        UserAddress address = userAddresses.get(pos);

        RetroInterface.getZomatoRestApi().deleteUserAddress(
                address.getId() + "",
                new Callback<UserAddressResponse>() {
                    @Override
                    public void success(UserAddressResponse userAddressResponse, Response response) {
                        getUserAddresses();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );

    }

    private void openEditAddress(int pos) {
        UserAddress address = userAddresses.get(pos);
        startActivity(AddAddressActivity.getCallIntent(context, address));
    }


    public void addAddressClick(View view) {
        startActivity(AddAddressActivity.getCallIntent(context, null));
    }
}
