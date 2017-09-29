package com.androcid.zomato.activity.photos;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;

import com.androcid.zomato.R;
import com.androcid.zomato.dao.DaoController;
import com.androcid.zomato.model.PhotoItem;
import com.androcid.zomato.util.CommonFunctions;
import com.androcid.zomato.util.Files;
import com.androcid.zomato.util.MarshmallowPermission;
import com.androcid.zomato.util.MyLg;
import com.androcid.zomato.view.camera.CameraUtil;
import com.androcid.zomato.view.camera.DefaultEasyCamera;
import com.androcid.zomato.view.camera.EasyCamera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;


/**
 *
 */
public class CameraActivity extends AppCompatActivity {

    private static final String TAG = CameraActivity.class.getSimpleName();
    private Context context = CameraActivity.this;

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent(context, CameraActivity.class);
        return intent;
    }

    TextureView cameraPreview;
    boolean surfaceReady;
    boolean hasPermissions;
    MarshmallowPermission marshmallowPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

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

        marshmallowPermission = new MarshmallowPermission(this);
        checkPermissions();

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



    EasyCamera.PictureCallback callback = new EasyCamera.PictureCallback() {
        public void onPictureTaken(byte[] data, EasyCamera.CameraActions actions) {

            MyLg.e(TAG, "Picture Taken");

            String path = "/test"+new Date().getTime()+".jpg";
            File file = new File(Files.getFolder(context, Files.CAPTURE),path);
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            Log.e(TAG, "Bitmap "+bitmap.getWidth()+" "+bitmap.getHeight());

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
            bitmap = CommonFunctions.scaleDownBitmap(bitmap, 1000, false);
            //,rxCameraData.rotateMatrix, false);

            Log.e(TAG, "Bitmap "+bitmap.getWidth()+" "+bitmap.getHeight());

            try {
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            DaoController.addPhotoItem(context, new PhotoItem(null, "", "Captured", file.getAbsolutePath(), "", false));

            camera.close();
           // startActivity(AddPhotosActivity.getCallIntent(context));
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    private boolean takingPicture;
    public void takePicture(View view) {

        if (!takingPicture) {
            takingPicture = true;
            actions.takePicture(EasyCamera.Callbacks.create().withJpegCallback(callback));
        }
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

    public void closeClick(View view) {
        finish();
    }
}