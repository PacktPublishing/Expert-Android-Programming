package com.androcid.zomato.activity.address;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.androcid.zomato.BuildConfig;
import com.androcid.zomato.R;
import com.androcid.zomato.model.room.UserLocation;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.retro.UserLocationResponse;
import com.androcid.zomato.util.Constant;
import com.androcid.zomato.util.MarshmallowPermission;
import com.androcid.zomato.util.MyLg;
import com.androcid.zomato.view.adapter.UserLocationAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.androcid.zomato.util.Constant.APP_INITIATED_REQUEST;
import static com.androcid.zomato.util.Constant.USER_INITIATED_REQUEST;

/**
 *
 */
public class SelectDeliveryAreaActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = SelectDeliveryAreaActivity.class.getSimpleName();
    Context context = SelectDeliveryAreaActivity.this;

    //Toolbar
    TextView title;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, SelectDeliveryAreaActivity.class);
        return intent;
    }

    MarshmallowPermission marshmallowPermission;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    List<UserLocation> userLocations;
    TextView userHint;
    EditText searchText;
    RecyclerView searchList;
    UserLocationAdapter userLocationAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_delivery_area);

        //Set toolbar
        title = (TextView) findViewById(R.id.title);
        title.setText(R.string.txt_select_location);

        userHint = (TextView) findViewById(R.id.userHint);
        searchText = (EditText) findViewById(R.id.searchText);


        //SEARCH
        userLocations = new ArrayList<>();
        userLocationAdapter = new UserLocationAdapter(context, userLocations);
        userLocationAdapter.setClickListener(new UserLocationAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                UserLocation location = userLocations.get(pos);
                selectLocation(location);
            }
        });

        searchList = (RecyclerView) findViewById(R.id.searchList);
        searchList.setLayoutManager(new LinearLayoutManager(context));
        searchList.setAdapter(userLocationAdapter);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        closeHandler = new Handler();
        closeRun = new Runnable() {

            @Override
            public void run() {
                getUserLocationSearch();
            }
        };

        marshmallowPermission = new MarshmallowPermission(this);
        buildGoogleApiClient();

    }

    private void selectLocation(UserLocation location) {
        Intent intent = new Intent();
        intent.putExtra(Constant.ID, location.getId());
        intent.putExtra(Constant.NAME, location.getName());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void checkText(String search) {
        if (search.equals("")) {
            userHint.setText("Start Tying");
            userLocations = new ArrayList<>();
            userLocationAdapter.refresh(userLocations);
        } else {
            if (userLocations.size() == 0) {
                userHint.setText("Fetching List");
            }
            getUserLocations(search);
        }
    }

    private Handler closeHandler;
    private Runnable closeRun;

    private void checkQuery() {
        StopRunnable();
    }

    public void StopRunnable() {
        try {
            closeHandler.removeCallbacks(closeRun);
        } catch (Exception e) {
            // TODO: handle exception
        }

        StartRunnable();
    }

    private void StartRunnable() {
        closeHandler.postDelayed(closeRun, Constant.SPLASH_TIME_OUT);
    }

    String search;

    private void getUserLocations(String search) {
        this.search = search;
        checkQuery();
    }

    private void getUserLocationSearch() {

        if (search != null && !search.equals("")) {

            if (BuildConfig.NETWORK_TEST && Build.VERSION.SDK_INT >= 14) {
                try {
                    TrafficStats.setThreadStatsTag(USER_INITIATED_REQUEST);
                    // Once tag has been applied, network request has to be made request
                } finally {
                    TrafficStats.clearThreadStatsTag();
                }
            }

            RetroInterface.getZomatoRestApi().getSearchLocation(
                    search,
                    new Callback<UserLocationResponse>() {
                        @Override
                        public void success(UserLocationResponse userLocationResponse, Response response) {

                            if (userLocationResponse != null) {
                                userLocations = userLocationResponse.getUserLocations();
                                userLocationAdapter.refresh(userLocations);

                                if (userLocations.size() != 0) {
                                    userHint.setText("");
                                } else {
                                    userHint.setText("No Users Found");
                                }
                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            userHint.setText("No Users Found");
                        }
                    }
            );

        }

    }


    private void checkPermissions() {
        if (!marshmallowPermission.checkPermissionForLocation()) {
            marshmallowPermission.requestPermissionForLocation();
        } else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            MyLg.e(TAG, "Start Listener");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MarshmallowPermission.COARSE_LOCATION_PERMISSION_REQUEST_CODE:
            case MarshmallowPermission.FINE_LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                }
                return;
            }
        }
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void closeClick(View view) {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        MyLg.e(TAG, "Listener Ready");
        createListener();
    }

    private void createListener() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        listenerReady = true;

        if (startListener) {
            startListener();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        MyLg.e(TAG, "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        MyLg.e(TAG, "GoogleApiClient connection has failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        handleLocation(location);
    }

    private void handleLocation(Location location) {
        if (location != null) {
            MyLg.e(TAG, "getLatitude() = " + String.valueOf(location.getLatitude()) + "\n getLongitude() = " + String.valueOf(location.getLongitude()));
            stopListener();

            getNearbyLocation(location);

        }
    }

    private void getNearbyLocation(Location location) {

        if (BuildConfig.NETWORK_TEST && Build.VERSION.SDK_INT >= 14) {
            try {
                TrafficStats.setThreadStatsTag(APP_INITIATED_REQUEST);
                // Once tag has been applied, network request has to be made request
            } finally {
                TrafficStats.clearThreadStatsTag();
            }
        }

        RetroInterface.getZomatoRestApi().getNearbyLocation(
                location.getLatitude() + "",
                location.getLongitude() + "",
                new Callback<UserLocationResponse>() {
                    @Override
                    public void success(UserLocationResponse userLocationResponse, Response response) {


                        List<UserLocation> locations = userLocationResponse.getUserLocations();
                        if (locations.size() != 0) {
                            UserLocation location = locations.get(0);
                            selectLocation(location);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );


    }

    private void startListener() {
        checkPermissions();
    }


    private void stopListener() {
        MyLg.e(TAG, "Stop Listener");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    boolean listenerReady = false;
    boolean startListener = false;

    public void detectLocation(View view) {
        startListener = true;
        startListener();
    }
}