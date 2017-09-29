package com.androcid.zomato.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.androcid.zomato.R;
import com.androcid.zomato.activity.HomeActivity;
import com.androcid.zomato.activity.base.SignInBaseActivity;


/**
 * Created by Androcid-7 on 21-12-2016.
 */
public class StartActivity extends SignInBaseActivity {

    private static final String TAG = StartActivity.class.getSimpleName();
    private Context context = StartActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, StartActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_start);

    }

    public void skipClick(View view) {
        startActivity(HomeActivity.getCallIntent(context));
    }

    public void facebookClick(View view) {
        facebookLoginClick();
    }

    public void googleClick(View view) {
        googleLoginClick();
    }

    public void signUpClick(View view) {
        startActivity(RegisterActivity.getCallIntent(context));
        overridePendingTransition(R.anim.bottomtop, R.anim.none);
    }

    public void logInClick(View view) {
        startActivity(LoginActivity.getCallIntent(context));
        overridePendingTransition(R.anim.bottomtop, R.anim.none);
    }


}