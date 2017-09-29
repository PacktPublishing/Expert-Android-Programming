package com.androcid.zomato.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.androcid.zomato.R;
import com.androcid.zomato.activity.credit.CreditHistoryActivity;


public class CreditsActivity extends AppCompatActivity {

    private static final String TAG = CreditsActivity.class.getSimpleName();
    private Context context = CreditsActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, CreditsActivity.class);
        return intent;
    }
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Zomato Credits");
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));
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

    public void gotoCredits(View view) {
        startActivity(CreditHistoryActivity.getCallIntent(context));
    }
}
