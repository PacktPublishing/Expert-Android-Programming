package com.androcid.zomato.activity.address;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androcid.zomato.R;
import com.androcid.zomato.model.UserAddress;
import com.androcid.zomato.model.room.UserLocation;
import com.androcid.zomato.preference.LocationPreference;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.retro.UserAddressResponse;
import com.androcid.zomato.util.CommonFunctions;
import com.androcid.zomato.util.Constant;
import com.androcid.zomato.util.Toas;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class AddAddressActivity extends AppCompatActivity {

    private static final String TAG = AddAddressActivity.class.getSimpleName();
    private static final int REQUEST_AREA = 110;
    private Context context = AddAddressActivity.this;

    public static Intent getCallIntent(Context context, UserAddress userAddress) {
        Intent intent = new Intent(context, AddAddressActivity.class);
        if (userAddress != null) {
            intent.putExtra(Constant.ADDRESS, userAddress);
        }
        return intent;
    }

    Toolbar toolbar;
    TextView del_area;
    TextView del_area_selected;

    //Type
    LinearLayout otherType, workType, homeType;
    EditText otherText;
    ImageView otherTextLine, otherTextCancel;

    EditText instruction;
    ImageView instructionLine, instructionCancel;

    EditText address;
    ImageView addressLine, addressCancel;

    UserAddress userAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Address");
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));

        userAddress = (UserAddress) getIntent().getSerializableExtra(Constant.ADDRESS);

        del_area = (TextView) findViewById(R.id.del_area);
        del_area_selected = (TextView) findViewById(R.id.del_area_selected);

        otherType = (LinearLayout) findViewById(R.id.otherType);
        workType = (LinearLayout) findViewById(R.id.workType);
        homeType = (LinearLayout) findViewById(R.id.homeType);

        otherText = (EditText) findViewById(R.id.otherText);
        otherTextLine = (ImageView) findViewById(R.id.otherTextLine);
        otherTextCancel = (ImageView) findViewById(R.id.otherTextCancel);

        instruction = (EditText) findViewById(R.id.instruction);
        instructionLine = (ImageView) findViewById(R.id.instructionLine);
        instructionCancel = (ImageView) findViewById(R.id.instructionCancel);

        address = (EditText) findViewById(R.id.address);
        addressLine = (ImageView) findViewById(R.id.addressLine);
        addressCancel = (ImageView) findViewById(R.id.addressCancel);

        //For Focus
        otherText.setOnFocusChangeListener(hasFocusListener);
        instruction.setOnFocusChangeListener(hasFocusListener);
        address.setOnFocusChangeListener(hasFocusListener);

        //For TextChange
        otherText.addTextChangedListener(textLengthListener);
        instruction.addTextChangedListener(textLengthListener);
        address.addTextChangedListener(textLengthListener);

        setDefaultDeliveryArea();

        if (userAddress != null) {
            setEditValues();
        }

    }

    private void setEditValues() {

        area_id = userAddress.getLocation_id();
        area_name = userAddress.getLocation_name();

        address.setText(userAddress.getAddress());
        instruction.setText(userAddress.getInstruction());

        if (userAddress.getType().equals("Home")) {
            otherText.setText("");
            homeType.setSelected(true);
        } else if (userAddress.getType().equals("Work")) {
            otherText.setText("");
            workType.setSelected(true);
        } else {
            otherText.setText(userAddress.getType());
            otherType.setSelected(true);
            findViewById(R.id.otherLayout).setVisibility(View.VISIBLE);
        }
        setDeliveryArea();
    }

    //For TextChange
    TextWatcher textLengthListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkText();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    //For Focus
    View.OnFocusChangeListener hasFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if (hasFocus) {
                removeError();
                handleFocus(v);
            }

        }
    };

    private boolean checkText() {

        if (otherText.getText().toString().length() != 0) {
            otherTextCancel.setVisibility(View.VISIBLE);
        } else {
            otherTextCancel.setVisibility(View.INVISIBLE);
        }

        if (instruction.getText().toString().length() != 0) {
            instructionCancel.setVisibility(View.VISIBLE);
        } else {
            instructionCancel.setVisibility(View.INVISIBLE);
        }

        if (address.getText().toString().length() != 0) {
            addressCancel.setVisibility(View.VISIBLE);
        } else {
            addressCancel.setVisibility(View.INVISIBLE);
        }

        return true;
    }

    private void removeError() {
        address.setError(null);
        instruction.setError(null);
        otherText.setError(null);
    }

    private void handleFocus(View v) {

        otherTextLine.setImageResource(R.color.login_line_normal);
        instructionLine.setImageResource(R.color.login_line_normal);
        addressLine.setImageResource(R.color.login_line_normal);

        switch (v.getId()) {
            case R.id.otherText:
                otherTextLine.setImageResource(R.color.login_line_focused);
                break;
            case R.id.instruction:
                instructionLine.setImageResource(R.color.login_line_focused);
                break;
            case R.id.address:
                addressLine.setImageResource(R.color.login_line_focused);
                break;
        }

    }

    public void cancelClick(View view) {
        switch (view.getId()) {
            case R.id.otherTextCancel:
                otherText.setText("");
                break;
            case R.id.instructionCancel:
                instruction.setText("");
                break;
            case R.id.addressCancel:
                address.setText("");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    int area_id;
    String area_name;

    private void setDefaultDeliveryArea() {
        if (LocationPreference.hasLocation(context)) {
            UserLocation location = LocationPreference.getUserLocation(context);
            area_id = location.getId();
            area_name = location.getName();
            setDeliveryArea();
        }
    }

    private void setDeliveryArea() {

        if (area_name != null) {
            del_area_selected.setVisibility(View.VISIBLE);
            del_area.setText(area_name);
        } else {
            del_area_selected.setVisibility(View.GONE);
            del_area.setText("");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_save:
                addAddress();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addAddress() {

        String addressValue = address.getText().toString();
        String instructionValue = instruction.getText().toString();
        String addressType = getAddressType();

        boolean isValid = true;
        if (addressValue.trim().length() == 0) {
            isValid = false;
        }
        if (area_id == 0) {
            isValid = false;
        }
        if (addressType.trim().length() == 0) {
            addressType = "Other";
        }

        if (isValid) {

            UserAddress saveAddress = new UserAddress(
                    0,
                    SessionPreference.getUserId(context),
                    area_id,
                    area_name,
                    addressValue,
                    instructionValue,
                    addressType
            );


            if(userAddress!=null){
                deleteCurrent(saveAddress);
            }else{
                addAddress(saveAddress);
            }

        } else {
            Toas.show(context, "Please Enter All Required Details");
        }
    }

    private void deleteCurrent(final UserAddress saveAddress) {

        RetroInterface.getZomatoRestApi().deleteUserAddress(
                userAddress.getId() + "",
                new Callback<UserAddressResponse>() {
                    @Override
                    public void success(UserAddressResponse userAddressResponse, Response response) {
                        addAddress(saveAddress);
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );
    }

    private void addAddress(UserAddress saveAddress) {

        RetroInterface.getZomatoRestApi().addUserAddress(
                CommonFunctions.getHashMap(saveAddress),
                new Callback<UserAddressResponse>() {
                    @Override
                    public void success(UserAddressResponse userResponse, Response response) {

                        if (userResponse != null && userResponse.isSuccess()) {
                            finish();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );
    }

    private String getAddressType() {

        if (homeType.isSelected()) {
            return "Home";
        }

        if (workType.isSelected()) {
            return "Work";
        }

        if (otherType.isSelected()) {
            return otherText.getText().toString();
        }
        return "";

    }

    public void selectLocationClick(View view) {
        startActivityForResult(SelectDeliveryAreaActivity.getCallIntent(context), REQUEST_AREA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_AREA) {

                area_id = data.getIntExtra(Constant.ID, 0);
                area_name = data.getStringExtra(Constant.NAME);
                setDeliveryArea();
            }
        }
    }

    public void typeSelectClick(View view) {

        homeType.setSelected(false);
        workType.setSelected(false);
        otherType.setSelected(false);

        switch (view.getId()) {
            case R.id.homeType:
                showOther(false);
                homeType.setSelected(true);
                break;
            case R.id.workType:
                showOther(false);
                workType.setSelected(true);
                break;
            case R.id.otherType:
                showOther(true);
                otherType.setSelected(true);
                break;

        }
    }

    private void showOther(boolean b) {
        if (b) {
            otherText.setText("");
            findViewById(R.id.otherLayout).setVisibility(View.VISIBLE);
        } else {
            otherText.setText("");
            findViewById(R.id.otherLayout).setVisibility(View.GONE);
        }
    }
}
