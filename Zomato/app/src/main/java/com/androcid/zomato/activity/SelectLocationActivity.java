package com.androcid.zomato.activity;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androcid.zomato.BuildConfig;
import com.androcid.zomato.R;
import com.androcid.zomato.activity.base.ClearBaseActivity;
import com.androcid.zomato.model.room.UserLocation;
import com.androcid.zomato.model.room.viewmodel.UserLocationViewModel;
import com.androcid.zomato.preference.LocationPreference;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.NormalResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.retro.UserLocationResponse;
import com.androcid.zomato.util.Constant;
import com.androcid.zomato.util.KeyboardDetect;
import com.androcid.zomato.util.MarshmallowPermission;
import com.androcid.zomato.util.MyLg;
import com.androcid.zomato.view.adapter.PopularLocationAdapter;
import com.androcid.zomato.view.adapter.UserLocationAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

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
public class SelectLocationActivity extends ClearBaseActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = SelectLocationActivity.class.getSimpleName();
    private static final int REQUEST_CHECK_SETTINGS = 111;
    Context context = SelectLocationActivity.this;

    //Toolbar
    TextView title;

    //Popular

    List<UserLocation> recentItems;
    UserLocationAdapter recentLocationAdapter;
    RecyclerView recentRecycler;

    List<UserLocation> locationItems;
    PopularLocationAdapter popularAdapter;
    RecyclerView popularRecycler;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, SelectLocationActivity.class);
        return intent;
    }

    MarshmallowPermission marshmallowPermission;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    List<UserLocation> userLocations;
    RelativeLayout searchView;
    TextView userHint;
    EditText searchText;
    RecyclerView searchList;
    UserLocationAdapter userLocationAdapter;


    //ROOM
    UserLocationViewModel userLocationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        //Set toolbar
        title = (TextView) findViewById(R.id.title);
        title.setText(R.string.txt_select_location);

        searchView = (RelativeLayout) findViewById(R.id.searchView);
        searchView.setVisibility(View.INVISIBLE);
        userHint = (TextView) findViewById(R.id.userHint);
        searchText = (EditText) findViewById(R.id.searchText);

        //Might
        locationItems = new ArrayList<>();
        popularAdapter = new PopularLocationAdapter(context, locationItems);
        popularAdapter.setClickListener(new PopularLocationAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                UserLocation location = locationItems.get(pos);

                updateLocation(location);
            }
        });

        popularRecycler = (RecyclerView) findViewById(R.id.popularRecycler);
        popularRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        popularRecycler.setAdapter(popularAdapter);

        recentItems = new ArrayList<>();
        recentLocationAdapter = new UserLocationAdapter(context, recentItems);
        recentLocationAdapter.setClickListener(new UserLocationAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                UserLocation location = recentItems.get(pos);
                updateLocation(location);
            }
        });

        recentRecycler = (RecyclerView) findViewById(R.id.recentRecycler);
        recentRecycler.setLayoutManager(new LinearLayoutManager(context));
        recentRecycler.setNestedScrollingEnabled(false);
        recentRecycler.setAdapter(recentLocationAdapter);

        //SEARCH

        userLocations = new ArrayList<>();
        userLocationAdapter = new UserLocationAdapter(context, userLocations);
        userLocationAdapter.setClickListener(new UserLocationAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                UserLocation location = userLocations.get(pos);
                updateLocation(location);
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


        KeyboardDetect keyboardDetect = new KeyboardDetect(this);
        keyboardDetect.setKeyboardListener(new KeyboardDetect.KeyboardListener() {
            @Override
            public void onSoftKeyboardShown(boolean isShowing) {
                String search = searchText.getText().toString();
                if (isShowing) {
                    searchView.setVisibility(View.VISIBLE);
                    checkText(search);
                } else {
                    if (search.length() != 0) {
                        searchView.setVisibility(View.VISIBLE);
                    } else {
                        searchView.setVisibility(View.INVISIBLE);
                    }
                }
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

        checkText("");
        userLocationViewModel = ViewModelProviders.of(this).get(UserLocationViewModel.class);

    }

    private void updateLocation(final UserLocation location) {

        RetroInterface.getZomatoRestApi().setUserLocation(
                SessionPreference.getUserId(context) + "",
                location.getId() + "",
                new Callback<NormalResponse>() {
                    @Override
                    public void success(NormalResponse normalResponse, Response response) {
                        LocationPreference.setLocationParams(context, location);
                        finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPopularLocations();
        getRecentLocations();
    }

    private void getPopularLocations() {

        if (BuildConfig.NETWORK_TEST && Build.VERSION.SDK_INT >= 14) {
            try {
                TrafficStats.setThreadStatsTag(APP_INITIATED_REQUEST);
                // Once tag has been applied, network request has to be made request
            } finally {
                TrafficStats.clearThreadStatsTag();
            }
        }

        RetroInterface.getZomatoRestApi().getPopularLocation(
                "",
                new Callback<UserLocationResponse>() {
                    @Override
                    public void success(UserLocationResponse userLocationResponse, Response response) {
                        locationItems = userLocationResponse.getUserLocations();
                        popularAdapter.refresh(locationItems);
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );

    }

    private void getRecentLocations() {

        RetroInterface.getZomatoRestApi().getPopularLocation(
                "",
                new Callback<UserLocationResponse>() {
                    @Override
                    public void success(UserLocationResponse userLocationResponse, Response response) {
                        recentItems = userLocationResponse.getUserLocations();
                        recentLocationAdapter.refresh(recentItems);
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );

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


    LiveData<List<UserLocation>> liveData;
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
                                //userLocations = userLocationResponse.getUserLocations();
                                userLocationViewModel.addUserLocations(userLocationResponse.getUserLocations());
                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            userHint.setText("No Users Found");
                        }
                    }
            );

            if (liveData != null) {
                liveData.removeObservers(this);
                MyLg.e(TAG, "Removed Observers");
            }

            liveData = userLocationViewModel.getUserLocationsByName("%" + search + "%");
            liveData.observe(this, new Observer<List<UserLocation>>() {
                @Override
                public void onChanged(@Nullable List<UserLocation> serviceItems) {
                    userLocations = serviceItems;
                    if (!(search.equals(""))) {
                        refreshList();
                    }
                }
            });

        }

    }

    private void refreshList() {
        userLocationAdapter.refresh(userLocations);

        if (userLocations.size() != 0) {
            userHint.setText("");
        } else {
            userHint.setText("No Users Found");
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

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            Log.i(TAG, "All location settings are satisfied.");
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the result
                                // in onActivityResult().
                                status.startResolutionForResult(SelectLocationActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                            break;
                    }
                }
            });

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
                        if(locations.size()!=0) {
                            UserLocation location = locations.get(0);
                            updateLocation(location);
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