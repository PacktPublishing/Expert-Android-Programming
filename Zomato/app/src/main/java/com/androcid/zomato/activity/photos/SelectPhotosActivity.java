package com.androcid.zomato.activity.photos;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.androcid.zomato.R;
import com.androcid.zomato.dao.DaoController;
import com.androcid.zomato.model.BucketItem;
import com.androcid.zomato.model.PhotoItem;
import com.androcid.zomato.util.Constant;
import com.androcid.zomato.util.MarshmallowPermission;
import com.androcid.zomato.util.MyLg;
import com.androcid.zomato.view.adapter.BucketPhotoAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class SelectPhotosActivity extends AppCompatActivity {

    private static final String TAG = SelectPhotosActivity.class.getSimpleName();
    private Context context = SelectPhotosActivity.this;

    public static Intent getCallIntent(Context context, BucketItem item) {
        Intent intent = new Intent(context, SelectPhotosActivity.class);
        intent.putExtra(Constant.DATA, item);
        return intent;
    }

    TextView title;
    List<PhotoItem> allList;
    BucketPhotoAdapter adapter;
    RecyclerView recyclerView;

    List<PhotoItem> allSelected;
    BucketItem item;

    MarshmallowPermission marshmallowPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photos);

        item = (BucketItem) getIntent().getSerializableExtra(Constant.DATA);
        allSelected = DaoController.getPhotoItems(context);

        title = (TextView) findViewById(R.id.title);

        allList = new ArrayList<>();
        adapter = new BucketPhotoAdapter(context, allList);
        adapter.setClickListener(new BucketPhotoAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {

                boolean b = allList.get(pos).isSelected();
                allList.get(pos).setSelected(!b);
                adapter.refresh(allList, pos);

                countSelected();

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.setAdapter(adapter);

        marshmallowPermission = new MarshmallowPermission(this);

        title.setText(item.getName());

        checkPermissions();
    }

    private void countSelected() {

        int count = 0;
        for (int i = 0; i < allList.size(); i++) {
            count += (allList.get(i).isSelected() ? 1 : 0);
        }

        if (count != 0) {
            title.setText(count + " selected");
        } else {
            title.setText(item.getName());
        }

    }


    private void checkPermissions() {
        if (!marshmallowPermission.checkPermissionForReadExternalStorage()) {
            marshmallowPermission.requestPermissionForReadExternalStorage();
        } else {
            if (!marshmallowPermission.checkPermissionForWriteExternalStorage()) {
                marshmallowPermission.requestPermissionForWriteExternalStorage();
            } else {
                if (!marshmallowPermission.isPermissionForCameraGranted()) {
                    marshmallowPermission.requestPermissionForCamera();
                } else {
                    getImages();
                }
            }
        }
    }


    private void getImages() {

        // which image properties are we querying
        String[] projection = new String[]{
                MediaStore.Images.Media.BUCKET_ID,//MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.DATE_TAKEN
        };

// content:// style URI for the "primary" external storage volume
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String selection = item.getId().equals("") ? null : MediaStore.Images.ImageColumns.BUCKET_ID
                + " = '" + item.getId() + "'";

// Make the query.
        Cursor cur = getContentResolver().query(images,
                projection, // Which columns to return
                selection,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                null        // Ordering
        );

        MyLg.i("ListingImages", " query count=" + cur.getCount());

        if (cur.moveToFirst()) {
            String id;
            String path;
            String bucket;
            String date;

            int idColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);//MediaStore.Images.Media._ID);
            int bucketColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int dateColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATE_TAKEN);
            int dataColumn = cur.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);


            do {
                // Get the field values
                id = cur.getString(idColumn);
                bucket = cur.getString(bucketColumn);
                date = cur.getString(dateColumn);
                path = cur.getString(dataColumn);


                PhotoItem photoItem = new PhotoItem(null, id, bucket, path, "", false);
                allList.add(photoItem);

            } while (cur.moveToNext());
        }


        processBucketList();

    }

    private void processBucketList() {

       /* String path = "";
        if (bucketItemHashMap.size() != 0) {

            for (String key : bucketItemHashMap.keySet()) {
                if (path.equals(""))
                    path = bucketItemHashMap.get(key).getPath();
                allList.add(bucketItemHashMap.get(key));
            }

        }

        int total = getTotal();
        BucketItem item = new BucketItem("0", "ALL", path);
        item.setCount(total);
        allList.add(0, item);*/

        for (int i = 0; i < allSelected.size(); i++) {

            for (int j = 0; j < allList.size(); j++) {

                if (allSelected.get(i).getPath().equals(allList.get(j).getPath())) {
                    allList.get(j).setSelected(true);
                }

            }

        }

        adapter.refresh(allList);

    }

    private int getTotal() {
        int total = 0;
        /*for (int i = 0; i < allList.size(); i++) {
            total += allList.get(i).getCount();
        }*/
        return total;
    }


    @SuppressWarnings("NullableProblems")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MarshmallowPermission.CAMERA_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                }
                return;
            }
            case MarshmallowPermission.WRITEEXTERNAL_STORAGE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                }
                return;
            }
            case MarshmallowPermission.READEXTERNAL_STORAGE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                }
                return;
            }
            default:
                return;
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void nextClick(View view) {

        List<PhotoItem> items = new ArrayList<>();
        for (int i = 0; i < allList.size(); i++) {

            if (allList.get(i).isSelected()) {
                items.add(allList.get(i));
            }
        }

        MyLg.e(TAG, "SELECTED " + items.size());
        int size = allSelected.size() - 1;

        for (int i = size; i >= 0; i--) {

            if (allSelected.get(i).get_id().equals(item.getId())) {
                MyLg.e(TAG, i + " " + allSelected.get(i).get_id() + "  " + item.getId());
                allSelected.remove(i);
            }
        }

        allSelected.addAll(items);

        for (int i = 0; i < allSelected.size(); i++) {
            MyLg.e(TAG, "" + allSelected.get(i).get_id() + " " + allSelected.get(i).getPath());
        }

        DaoController.addPhotoItems(context, allSelected);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);

        finish();
    }
}