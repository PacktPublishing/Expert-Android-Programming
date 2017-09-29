package com.androcid.zomato.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.androcid.zomato.R;


/**
 *
 */
public class NotificationPreferenceActivity extends AppCompatActivity {

    private static final String TAG = NotificationPreferenceActivity.class.getSimpleName();
    private Context context = NotificationPreferenceActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, NotificationPreferenceActivity.class);
        return intent;
    }

    LinearLayout parent;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_preference);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notifications Preference");
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));
        parent = (LinearLayout) findViewById(R.id.parent);

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

    public void onOptionClick(View view) {
        if (view.isSelected()) {
            view.setSelected(false);
        } else {
            view.setSelected(true);
        }
    }

    public void onEnableAllClick(View view) {

        if (view.isSelected()) {
            enableAllChildren(parent, false);
        } else {
            enableAllChildren(parent, true);
        }

    }

    private void enableAllChildren(LinearLayout parent, boolean b) {

        try {
            for (int i = 0; i < parent.getChildCount(); i++) {
                View v = parent.getChildAt(i);
                v.setSelected(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}