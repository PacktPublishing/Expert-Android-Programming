package com.androcid.zomato.activity.photos;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;

import com.androcid.zomato.R;
import com.androcid.zomato.dao.DaoController;
import com.androcid.zomato.model.BucketItem;
import com.androcid.zomato.model.PhotoItem;
import com.androcid.zomato.util.Constant;
import com.androcid.zomato.util.MarshmallowPermission;
import com.androcid.zomato.util.MyLg;
import com.androcid.zomato.view.adapter.BucketAdapter;
import com.androcid.zomato.view.camera.CameraUtil;
import com.androcid.zomato.view.camera.DefaultEasyCamera;
import com.androcid.zomato.view.camera.EasyCamera;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 *
 */
public class PickPhotosActivity extends AppCompatActivity {

    private static final String TAG = PickPhotosActivity.class.getSimpleName();

    public static final int TYPE_NORMAL = 100;
    public static final int TYPE_SELECT = 101;

    private Context context = PickPhotosActivity.this;

    int type;
    public static Intent getCallIntent(Context context, int type) {
        Intent intent = new Intent(context, PickPhotosActivity.class);
        intent.putExtra(Constant.TYPE, type);
        return intent;
    }

    Toolbar toolbar;
    //Might Like
    HashMap<String, BucketItem> bucketItemHashMap;

    List<PhotoItem> allSelected;
    List<BucketItem> allList;
    BucketAdapter adapter;
    RecyclerView recyclerView;

    TextureView cameraPreview;
    boolean surfaceReady;
    boolean hasPermissions;
    int showHeight;
    int showWidth;
    MarshmallowPermission marshmallowPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photos);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Photos");
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));

        type = getIntent().getIntExtra(Constant.TYPE, TYPE_NORMAL);

        allList = new ArrayList<>();
        adapter = new BucketAdapter(context, allList);
        adapter.setClickListener(new BucketAdapter.ClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                gotoDetailsActivity(pos);
            }
        });

        //CAMERA
        cameraPreview = (TextureView) findViewById(R.id.cameraPreview);
        cameraPreview.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                surfaceReady = true;
                MyLg.e(TAG, "Ready "+width+" "+height);

                initCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {}

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(adapter);

        marshmallowPermission = new MarshmallowPermission(this);

        allSelected = DaoController.getPhotoItems(context);
        bucketItemHashMap = new HashMap<>();
        checkPermissions();

    }

    private void gotoDetailsActivity(int pos) {

        BucketItem item = allList.get(pos);
        startActivityForResult(SelectPhotosActivity.getCallIntent(context, item), type);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            MyLg.e(TAG, "Request Code "+type+" "+requestCode);

            if (requestCode == TYPE_NORMAL) {
                allSelected = DaoController.getPhotoItems(context);
                updateSelected();
                startActivity(AddPhotosActivity.getCallIntent(context));
            }else if(requestCode == TYPE_SELECT){
                finish();
            }
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
                    hasPermissions = true;
                    initCamera();
                    getImages();
                }
            }
        }
    }

    EasyCamera.CameraActions actions;
    Matrix matrix = new Matrix();
    EasyCamera camera;
    private void initCamera() {
        if(surfaceReady && hasPermissions) {
            MyLg.e(TAG, "surfaceReady "+" hasPermissions");
            try {
                int backCamera = CameraUtil.getBackCameraId();
                camera = DefaultEasyCamera.open(backCamera);
                int cameraOrientation= 0;
                Camera.CameraInfo cameraInfo = CameraUtil.getCameraInfo(backCamera);
                if (cameraInfo != null) {
                    cameraOrientation = cameraInfo.orientation;
                }

                matrix.setRotate(cameraOrientation);
                camera.setDisplayOrientation(cameraOrientation);

                Camera.Parameters parameters = camera.getParameters();
                Camera.Size bestPreviewSize = determineBestPreviewSize(parameters);
                Camera.Size bestPictureSize = determineBestPictureSize(parameters);
                parameters.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height);
                parameters.setPictureSize(bestPictureSize.width, bestPictureSize.height);
                camera.setParameters(parameters);

                actions = camera.startPreview(cameraPreview.getSurfaceTexture());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static final int PICTURE_SIZE_MAX_WIDTH = 1280;
    private static final int PREVIEW_SIZE_MAX_WIDTH = 640;
    private Camera.Size determineBestPreviewSize(Camera.Parameters parameters) {
        return determineBestSize(parameters.getSupportedPreviewSizes(), PREVIEW_SIZE_MAX_WIDTH);
    }

    private Camera.Size determineBestPictureSize(Camera.Parameters parameters) {
        return determineBestSize(parameters.getSupportedPictureSizes(), PICTURE_SIZE_MAX_WIDTH);
    }

    private Camera.Size determineBestSize(List<Camera.Size> sizes, int widthThreshold) {
        Camera.Size bestSize = null;
        Camera.Size size;
        int numOfSizes = sizes.size();
        for (int i = 0; i < numOfSizes; i++) {
            size = sizes.get(i);
            boolean isDesireRatio = (size.width / 4) == (size.height / 3);
            boolean isBetterSize = (bestSize == null) || size.width > bestSize.width;

            if (isDesireRatio && isBetterSize) {
                bestSize = size;
            }
        }

        if (bestSize == null) {
            Log.e(TAG, "cannot find the best camera size");
            return sizes.get(sizes.size() - 1);
        }

        return bestSize;
    }

    public void takePicture(View view) {

        camera.close();
        startActivityForResult(CameraActivity.getCallIntent(context), type);
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

// Make the query.
        Cursor cur = getContentResolver().query(images,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
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

                if (bucketItemHashMap.containsKey(bucket)) {
                    BucketItem bucketItem = bucketItemHashMap.get(bucket);
                    int count = bucketItem.getCount() + 1;
                    bucketItem.setCount(count);
                    bucketItemHashMap.put(bucket, bucketItem);
                } else {
                    BucketItem bucketItem = new BucketItem(id, bucket, path);
                    int count = bucketItem.getCount() + 1;
                    bucketItem.setCount(count);
                    bucketItemHashMap.put(bucket, bucketItem);
                }

            } while (cur.moveToNext());
        }


        processBucketList();
    }

    private void processBucketList() {

        String path = "";
        if (bucketItemHashMap.size() != 0) {

            for (String key : bucketItemHashMap.keySet()) {
                if (path.equals(""))
                    path = bucketItemHashMap.get(key).getPath();
                allList.add(bucketItemHashMap.get(key));
            }

        }

        int total = getTotal();
        BucketItem item = new BucketItem("", "ALL", path);
        item.setCount(total);
        allList.add(0, item);

        updateSelected();


    }

    private void updateSelected() {

        for (int i = 0; i < allList.size(); i++) {
            allList.get(i).setSelected(0);

            for (int j = 0; j < allSelected.size(); j++) {
                if (allSelected.get(j).get_id().equals(allList.get(i).getId())) {
                    int selected = allList.get(i).getSelected();
                    selected++;
                    allList.get(i).setSelected(selected);
                    adapter.refresh(allList);
                }
            }

        }

        adapter.refresh(allList);
    }

    private int getTotal() {
        int total = 0;
        for (int i = 0; i < allList.size(); i++) {
            total += allList.get(i).getCount();
        }
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

}