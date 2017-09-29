package com.androcid.zomato.activity.profile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androcid.zomato.R;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.NormalResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.util.Validate;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SetHandleActivity extends AppCompatActivity {

    private static final String TAG = SetHandleActivity.class.getSimpleName();
    private Context context = SetHandleActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, SetHandleActivity.class);
        return intent;
    }

    EditText handle_text;

    LinearLayout error_text_lay;
    TextView error_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_handle);

        handle_text = (EditText) findViewById(R.id.handle_text);

        error_text_lay = (LinearLayout) findViewById(R.id.error_text_lay);
        error_text = (TextView) findViewById(R.id.error_text);
        error_text_lay.setVisibility(View.INVISIBLE);
    }


    public void closeClick(View view) {
        onBackPressed();
    }

    public void onDoneClick(View view) {

        hideKeyboard();

        final String handle = handle_text.getText().toString();
        if (!Validate.isAtleastValidLength(handle, 4) || handle.length() > 15) {
           // Toas.show(context, "Handle should be between 4 to 15 characters");
            showError("Handle should be between 4 to 15 characters", false);
            return;
        }

        new AlertDialog.Builder(context)
                .setTitle("Set Handle")
                .setMessage("Do you want to set your handle as " + handle)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateHandle(handle);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();



    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showError(String s, boolean b) {

        error_text_lay.setVisibility(View.VISIBLE);
        error_text.setText(s);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                error_text_lay.setVisibility(View.INVISIBLE);
            }
        }, 3000);

    }

    private void updateHandle(String handle) {

        RetroInterface.getZomatoRestApi().setUserHandle(
                SessionPreference.getUserId(context) + "",
                handle,
                new Callback<NormalResponse>() {
                    @Override
                    public void success(NormalResponse normalResponse, Response response) {
                        if (normalResponse.isSuccess()) {
                           // Toas.show(context, "Handle set Successfully");
                            showError("Handle set Successfully", true);
                            finish();
                        } else {
                           // Toas.show(context, "Handle already in use");
                            showError("Handle already in use", false);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );
    }
}
