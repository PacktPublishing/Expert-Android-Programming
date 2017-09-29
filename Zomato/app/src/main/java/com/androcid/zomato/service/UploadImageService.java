package com.androcid.zomato.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import com.androcid.zomato.retro.NormalResponse;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.util.Base64;
import com.androcid.zomato.util.Constant;
import com.androcid.zomato.util.MyLg;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UploadImageService extends Service {

	private static final String TAG = UploadImageService.class.getSimpleName();

    public UploadImageService() {
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        MyLg.e(TAG, "Service Started");
		startUpload(ImagePreference.getImageName(getApplication()),
				ImagePreference.getImagePath(getApplicationContext()));
		return super.onStartCommand(intent, flags, startId);
	}

	private void startUpload(String name, String path) {

		String content = getImageFromPath(path);
		if(content!=null) {

			RetroInterface.getImageApi().uploadImageService(
					""+content,
					"" + name,
					callbackDetails);
		}
		else {
			MyLg.e(TAG, "Could not decode image..");
			stopMyService();
		}
	}

	private void stopMyService() {
		EventBus.getDefault().post(new ImageUploadItem(true));
		stopSelf();

	}


	private Callback<NormalResponse> callbackDetails = new Callback<NormalResponse>() {
        @Override
        public void success(NormalResponse menuDetailResponse, Response response) {

			stopMyService();
        }

        @Override
        public void failure(RetrofitError error) {

			stopMyService();
        }
    };


	private String getImageFromPath(String imagePath) {

		try {
			if(imagePath!=null)
            {
                if(imagePath.length()!=0)
                {
                    Bitmap bitmap = decodeFile(imagePath);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream); /* compress to which format you want. */
                    byte [] byte_arr = stream.toByteArray();
                    String imageStr = Base64.encodeBytes(byte_arr);
                    return imageStr;
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Bitmap decodeFile(String path) {
		File f = new File(path);
		try {
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);
			final int REQUIRED_SIZE = Constant.IMAGESIZE;
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			//Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}

		return null;
	}


	@Override
	public void onDestroy() {
        MyLg.e(TAG, "Service Closed");
		super.onDestroy();
	}
	
}