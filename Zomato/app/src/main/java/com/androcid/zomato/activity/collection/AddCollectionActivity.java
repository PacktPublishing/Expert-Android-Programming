package com.androcid.zomato.activity.collection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.androcid.zomato.R;
import com.androcid.zomato.model.CollectionItem;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.util.Toas;

import java.util.ArrayList;
import java.util.List;


public class AddCollectionActivity extends AppCompatActivity {

    private static final String TAG = AddCollectionActivity.class.getSimpleName();
    private static final int COLLECTION_CREATED = 111;
    private Context context = AddCollectionActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, AddCollectionActivity.class);
        return intent;
    }

    Toolbar toolbar;

    EditText collectionName;
    ImageView collectionNameLine, collectionNameCancel;

    EditText description;
    ImageView descriptionLine, descriptionCancel;

    EditText tag;
    ImageView tagLine, tagCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collection);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create a Collection");
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));

        collectionName = (EditText) findViewById(R.id.collectionName);
        collectionNameLine = (ImageView) findViewById(R.id.collectionNameLine);
        collectionNameCancel = (ImageView) findViewById(R.id.collectionNameCancel);

        description = (EditText) findViewById(R.id.description);
        descriptionLine = (ImageView) findViewById(R.id.descriptionLine);
        descriptionCancel = (ImageView) findViewById(R.id.descriptionCancel);

        tag = (EditText) findViewById(R.id.tag);
        tagLine = (ImageView) findViewById(R.id.tagLine);
        tagCancel = (ImageView) findViewById(R.id.tagCancel);

        //For Focus
        collectionName.setOnFocusChangeListener(hasFocusListener);
        description.setOnFocusChangeListener(hasFocusListener);
        tag.setOnFocusChangeListener(hasFocusListener);

        //For TextChange
        collectionName.addTextChangedListener(textLengthListener);
        description.addTextChangedListener(textLengthListener);
        tag.addTextChangedListener(textLengthListener);

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

        if (collectionName.getText().toString().length() != 0) {
            collectionNameCancel.setVisibility(View.VISIBLE);
        } else {
            collectionNameCancel.setVisibility(View.INVISIBLE);
        }

        if (description.getText().toString().length() != 0) {
            descriptionCancel.setVisibility(View.VISIBLE);
        } else {
            descriptionCancel.setVisibility(View.INVISIBLE);
        }

        if (tag.getText().toString().length() != 0) {
            tagCancel.setVisibility(View.VISIBLE);
            // addHash();
        } else {
            tagCancel.setVisibility(View.INVISIBLE);
        }

        return true;
    }

    String tagString = "";

    private void addHash() {

        if (!tag.getText().toString().equals(tagString)) {//Format text

            String tagText = tag.getText().toString();

            if (tagText.length() != 0) {


                tagString = tag.getText().toString();
            }
        }

    }

    private void removeError() {
        tag.setError(null);
        description.setError(null);
        collectionName.setError(null);
    }

    private void handleFocus(View v) {

        collectionNameLine.setImageResource(R.color.login_line_normal);
        descriptionLine.setImageResource(R.color.login_line_normal);
        tagLine.setImageResource(R.color.login_line_normal);

        switch (v.getId()) {
            case R.id.collectionName:
                collectionNameLine.setImageResource(R.color.login_line_focused);
                break;
            case R.id.description:
                descriptionLine.setImageResource(R.color.login_line_focused);
                break;
            case R.id.tag:
                tagLine.setImageResource(R.color.login_line_focused);
                break;
        }

    }

    public void cancelClick(View view) {
        switch (view.getId()) {
            case R.id.collectionNameCancel:
                collectionName.setText("");
                break;
            case R.id.descriptionCancel:
                description.setText("");
                break;
            case R.id.tagCancel:
                tag.setText("");
                break;
        }
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

    public void addToCollection(View view) {

        if (validDetails()) {

            int user_id = SessionPreference.getUserId(context);
            String name = collectionName.getText().toString();
            String desc = description.getText().toString();
            String tags = tag.getText().toString();
            List<String> list = new ArrayList<>();
            CollectionItem item = new CollectionItem(0, user_id, name, desc, tags, list);
            startActivityForResult(AddPlaceToCollectionActivity.getCallIntent(context, item), COLLECTION_CREATED);

        } else {
            Toas.show(context, "Please enter name for your collection");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == COLLECTION_CREATED){
                finish();
            }
        }

    }

    private boolean validDetails() {

        if (collectionName.getText().toString().length() != 0) {
            return true;
        }
        return false;

    }
}
