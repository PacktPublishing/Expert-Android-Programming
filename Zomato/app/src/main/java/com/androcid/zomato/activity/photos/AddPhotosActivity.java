package com.androcid.zomato.activity.photos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androcid.zomato.R;
import com.androcid.zomato.activity.PlaceSearchActivity;
import com.androcid.zomato.dao.DaoController;
import com.androcid.zomato.model.PhotoItem;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.MealDetailResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.service.ImagePreference;
import com.androcid.zomato.service.ImageUploadItem;
import com.androcid.zomato.service.UploadImageService;
import com.androcid.zomato.util.Constant;
import com.androcid.zomato.util.MyLg;
import com.androcid.zomato.util.ServiceCheck;
import com.androcid.zomato.util.Toas;
import com.androcid.zomato.view.adapter.PhotoItemsAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 *
 */
public class AddPhotosActivity extends AppCompatActivity {

    private static final String TAG = AddPhotosActivity.class.getSimpleName();
    private static final int SEARCH_PLACE = 111;
    private Context context = AddPhotosActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, AddPhotosActivity.class);
        return intent;
    }

    //Might Like
    List<PhotoItem> allList;
    PhotoItemsAdapter adapter;
    RecyclerView recyclerView;

    FloatingActionButton mFab;

    LinearLayout uploadReady;

    TextView selected_restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_photos);

        allList = DaoController.getPhotoItems(context);
        adapter = new PhotoItemsAdapter(context, allList);
        adapter.setClickListener(new PhotoItemsAdapter.ClickListener() {

            @Override
            public void onRemoveClickListener(View v, int pos) {
                allList.remove(pos);
                adapter.refresh(allList);

                DaoController.addPhotoItems(context, allList);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        selected_restaurant = (TextView) findViewById(R.id.selected_restaurant);

        uploadReady = (LinearLayout) findViewById(R.id.uploadReady);
        uploadReady.setVisibility(View.INVISIBLE);
        showProgress(false);
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {

        try {
            new AlertDialog.Builder(context)
                    .setTitle("Discard Selection")
                    .setMessage("Going back will discard the photos you selected.")
                    .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DaoController.deletePhotoItem(context);
                            finish();
                        }
                    }).setNegativeButton("Keep", null).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void closeClick(View view) {
        onBackPressed();
    }

    public void nextClick(View view) {
        uploadReady.setVisibility(View.VISIBLE);
    }

    public void closeUpload(View view) {
        uploadReady.setVisibility(View.INVISIBLE);
    }

    public void selectRestaurant(View view) {

        startActivityForResult(PlaceSearchActivity.getCallIntent(context), SEARCH_PLACE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == SEARCH_PLACE) {

                String id = data.getStringExtra(Constant.ID);
                String restaurant_id = data.getStringExtra(Constant.RESTAURANT_ID);
                String name = data.getStringExtra(Constant.NAME);

                selected_restaurant_id = restaurant_id;
                selected_branch_restaurant_id = id;
                selected_restaurant.setText(name);

            }

        }

    }

    private void updateComplete() {

        showProgress(true);

        List<PhotoItem> allList = DaoController.getRemainingPhotoItems(context);
        MyLg.e(TAG, "Upload Remaining " + allList.size());
        if (allList.size() != 0) {

            PhotoItem item = allList.get(0);

            String finalName = "Photo" + new Date().getTime() + "U" + SessionPreference.getUserId(context) + ".jpg";
            String path = item.getPath();
            String tag = item.getTag();

            item.setName(finalName);
            item.setStatus(true);
            DaoController.addPhotoItem(context, item);

            MyLg.e(TAG, "Name " + finalName + " tag " + tag);

            ImagePreference.setParams(context, finalName, path);
            if (!ServiceCheck.isMyServiceRunning(context, UploadImageService.class)) {
                startService(new Intent(context, UploadImageService.class));
            }
        } else {
            uploadComplete();
        }

    }

    private void showProgress(boolean b) {
        if (b) {
            findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);
        }
    }

    private void uploadComplete() {

        List<PhotoItem> allList = DaoController.getPhotoItems(context);
        Toas.show(context, "Upload Complete");

        JSONArray array = new JSONArray();

        try {

            MyLg.e(TAG, "Update Size"+allList.size());

            for (int i = 0; i < allList.size(); i++) {

                MyLg.e(TAG, "Update Name"+allList.get(i).getName());

                JSONObject object = new JSONObject();
                object.put(Constant.IMAGE, allList.get(i).getName());
                object.put(Constant.TAGS, allList.get(i).getTag());
                array.put(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        MyLg.e(TAG, "Update Array "+array.toString());

        String data = array.toString();

        RetroInterface.getZomatoRestApi().addRestaurantPhotos(
                SessionPreference.getUserId(context) + "",
                selected_restaurant_id,
                selected_branch_restaurant_id,
                data,
                new Callback<MealDetailResponse>() {
                    @Override
                    public void success(MealDetailResponse mealDetailResponse, Response response) {
                        showProgress(false);
                        List<PhotoItem> items = new ArrayList<PhotoItem>();
                        DaoController.addPhotoItems(context, items);
                        finish();

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        showProgress(false);
                    }
                }
        );


    }

    public void onEvent(ImageUploadItem item) {
        MyLg.e(TAG, "Image Complete Received");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateComplete();
            }
        }, 200);

    }

    @Override
    protected void onStart() {
        super.onStart();
        MyLg.e(TAG, "onStart");
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    String selected_restaurant_id;
    String selected_branch_restaurant_id;

    public void onUploadPhotoClick(View view) {

        if (!selected_restaurant_id.equals("")) {
            DaoController.addPhotoItems(context, allList);
            updateComplete();
            uploadReady.setVisibility(View.INVISIBLE);
        } else {
            Toas.show(context, "Select Restaurant");
        }
    }
}