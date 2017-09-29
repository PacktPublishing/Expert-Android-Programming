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
public class RegisterActivity extends SignInBaseActivity {

    private Context context = RegisterActivity.this;
    private static final String TAG = RegisterActivity.class.getSimpleName();

    //Name
    TextView regNameHint;
    EditText regName;
    ImageView regNameLine, regNameCancel;
    //Email
    TextView regEmailHint;
    EditText regEmail;
    ImageView regEmailLine, regEmailCancel;
    //Password
    TextView regPasswordHint;
    EditText regPassword;
    ImageView regPasswordLine, regPasswordCancel;


    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regNameHint = (TextView) findViewById(R.id.regNameHint);
        regName = (EditText) findViewById(R.id.regName);
        regNameLine = (ImageView) findViewById(R.id.regNameLine);
        regNameCancel = (ImageView) findViewById(R.id.regNameCancel);

        regEmailHint = (TextView) findViewById(R.id.regEmailHint);
        regEmail = (EditText) findViewById(R.id.regEmail);
        regEmailLine = (ImageView) findViewById(R.id.regEmailLine);
        regEmailCancel = (ImageView) findViewById(R.id.regEmailCancel);

        regPasswordHint = (TextView) findViewById(R.id.regPasswordHint);
        regPassword = (EditText) findViewById(R.id.regPassword);
        regPasswordLine = (ImageView) findViewById(R.id.regPasswordLine);
        regPasswordCancel = (ImageView) findViewById(R.id.regPasswordCancel);


        //For Focus
        regName.setOnFocusChangeListener(hasFocusListener);
        regEmail.setOnFocusChangeListener(hasFocusListener);
        regPassword.setOnFocusChangeListener(hasFocusListener);

        //For TextChange
        regName.addTextChangedListener(textLengthListener);
        regEmail.addTextChangedListener(textLengthListener);
        regPassword.addTextChangedListener(textLengthListener);

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

        if (regName.getText().toString().length() != 0) {
            regNameCancel.setVisibility(View.VISIBLE);
            showHint(regNameHint);
        } else {
            regNameCancel.setVisibility(View.INVISIBLE);
            hideHint(regNameHint);
            return false;
        }

        if (regEmail.getText().toString().length() != 0) {
            regEmailCancel.setVisibility(View.VISIBLE);
            showHint(regEmailHint);
        } else {
            regEmailCancel.setVisibility(View.INVISIBLE);
            hideHint(regEmailHint);
            return false;
        }

        if (regPassword.getText().toString().length() != 0) {
            regPasswordCancel.setVisibility(View.VISIBLE);
            showHint(regPasswordHint);
        } else {
            regPasswordCancel.setVisibility(View.INVISIBLE);
            hideHint(regPasswordHint);
            return false;
        }

        return true;
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

        regNameLine.setImageResource(R.color.login_line_normal);
        regEmailLine.setImageResource(R.color.login_line_normal);
        regPasswordLine.setImageResource(R.color.login_line_normal);

        switch (v.getId()) {
            case R.id.regName:
                regNameLine.setImageResource(R.color.login_line_focused);
                break;
            case R.id.regEmail:
                regEmailLine.setImageResource(R.color.login_line_focused);
                break;
            case R.id.regPassword:
                regPasswordLine.setImageResource(R.color.login_line_focused);
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

    public void signUpClick(View view) {

        if (validateData()) {
            MyLg.e(TAG, "All Details Got ");

            String name = regName.getText().toString();
            String email = regEmail.getText().toString();
            String password = regPassword.getText().toString();

            registerClick(name, email, password);

        }

        /*startActivity(HomeActivity.getCallIntent(context));*/
    }

    private void removeError() {
        regName.setError(null);
        regEmail.setError(null);
        regPassword.setError(null);
    }

    private boolean validateData() {

        if (TextUtils.isEmpty(regName.getText().toString())) {
            regName.setError(getString(R.string.error_field_required));
            return false;
        }
        if (TextUtils.isEmpty(regEmail.getText().toString()) || !Validate.isValidEmail(regEmail.getText().toString())) {
            regEmail.setError(getString(R.string.error_field_required));
            return false;
        }
        if (TextUtils.isEmpty(regPassword.getText().toString()) || !Validate.isAtleastValidLength(regPassword.getText().toString(), 5)) {
            regPassword.setError(getString(R.string.error_field_required));
            return false;
        }
        return true;
    }

    public void logInClick(View view) {
        startActivity(LoginActivity.getCallIntent(context));
        overridePendingTransition(R.anim.leftrightin, R.anim.leftrightout);
        finish();
    }

    public void cancelClick(View view) {
        switch (view.getId()) {
            case R.id.regNameCancel:
                regName.setText("");
                break;
            case R.id.regEmailCancel:
                regEmail.setText("");
                break;
            case R.id.regPasswordCancel:
                regPassword.setText("");
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