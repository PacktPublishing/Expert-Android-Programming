package com.androcid.zomato.activity.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androcid.zomato.R;
import com.androcid.zomato.activity.base.SignInBaseActivity;
import com.androcid.zomato.util.MyLg;
import com.androcid.zomato.util.Validate;


/**
 * Created by Androcid-7 on 21-12-2016.
 */
public class LoginActivity extends SignInBaseActivity {

    private Context context = LoginActivity.this;
    private static final String TAG = LoginActivity.class.getSimpleName();

    //Email
    TextView logEmailHint;
    EditText logEmail;
    ImageView logEmailLine, logEmailCancel;
    //Password
    TextView logPasswordHint;
    EditText logPassword;
    ImageView logPasswordLine, logPasswordCancel;



    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logEmailHint = (TextView) findViewById(R.id.logEmailHint);
        logEmail = (EditText) findViewById(R.id.logEmail);
        logEmailLine = (ImageView) findViewById(R.id.logEmailLine);
        logEmailCancel = (ImageView) findViewById(R.id.logEmailCancel);

        logPasswordHint = (TextView) findViewById(R.id.logPasswordHint);
        logPassword = (EditText) findViewById(R.id.logPassword);
        logPasswordLine = (ImageView) findViewById(R.id.logPasswordLine);
        logPasswordCancel = (ImageView) findViewById(R.id.logPasswordCancel);

        //For Focus
        logEmail.setOnFocusChangeListener(hasFocusListener);
        logPassword.setOnFocusChangeListener(hasFocusListener);

        //For TextChange
        logEmail.addTextChangedListener(textLengthListener);
        logPassword.addTextChangedListener(textLengthListener);

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
                handleFocus(v);
            }

        }
    };

    private void checkText() {

        if (logEmail.getText().toString().length() != 0) {
            logEmailCancel.setVisibility(View.VISIBLE);
            showHint(logEmailHint);
        } else {
            logEmailCancel.setVisibility(View.INVISIBLE);
            hideHint(logEmailHint);
        }

        if (logPassword.getText().toString().length() != 0) {
            logPasswordCancel.setVisibility(View.VISIBLE);
            showHint(logPasswordHint);
        } else {
            logPasswordCancel.setVisibility(View.INVISIBLE);
            hideHint(logPasswordHint);
        }

    }

    private void hideHint(final View view) {
        view.setPivotX(0);
        view.setPivotY(view.getHeight());

        Animator iconAnim = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0f));
        iconAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });
        iconAnim.start();
    }

    private void showHint(View view) {
        view.setPivotX(0);
        view.setPivotY(view.getHeight());

        if (view.getVisibility() == View.INVISIBLE) {
            view.setVisibility(View.VISIBLE);
            Animator iconAnim = ObjectAnimator.ofPropertyValuesHolder(view,
                    PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f, 1f));
            iconAnim.start();
        }

    }

    private void handleFocus(View v) {

        logEmailLine.setImageResource(R.color.login_line_normal);
        logPasswordLine.setImageResource(R.color.login_line_normal);

        switch (v.getId()) {
            case R.id.logEmail:
                logEmailLine.setImageResource(R.color.login_line_focused);
                break;
            case R.id.logPassword:
                logPasswordLine.setImageResource(R.color.login_line_focused);
                break;
        }


    }

    public void facebookClick(View view) {
        facebookLoginClick();
        /*startActivity(HomeActivity.getCallIntent(context));*/
    }

    public void googleClick(View view) {
        googleLoginClick();
        /*startActivity(HomeActivity.getCallIntent(context));*/
    }

    public void logInClick(View view) {

        if (validateData()) {
            MyLg.e(TAG, "All Details Got ");

            String email = logEmail.getText().toString();
            String password = logPassword.getText().toString();

            loginClick(email, password);

        }

        /*startActivity(HomeActivity.getCallIntent(context));*/
    }

    private boolean validateData() {
        if (TextUtils.isEmpty(logEmail.getText().toString()) || !Validate.isValidEmail(logEmail.getText().toString())) {
            logEmail.setError(getString(R.string.error_field_required));
            return false;
        }
        if (TextUtils.isEmpty(logPassword.getText().toString()) || !Validate.isAtleastValidLength(logPassword.getText().toString(), 5)) {
            logPassword.setError(getString(R.string.error_field_required));
            return false;
        }
        return true;
    }

    public void forgotPasswordClick(View view) {
    }

    public void cancelClick(View view) {

        switch (view.getId()) {
            case R.id.logEmailCancel:
                logEmail.setText("");
                break;
            case R.id.logPasswordCancel:
                logPassword.setText("");
                break;
        }

    }

    public void closeClick(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.none, R.anim.topbottom);
    }
}