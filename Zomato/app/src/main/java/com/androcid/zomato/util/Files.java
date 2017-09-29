package com.androcid.zomato.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class Files {
	
	private static final String TAG = Files.class.getSimpleName();
	
	public static final String MAIN_FOLDER = "Zomato";
	public static final String CAPTURE = "Capture";
	public static final String IMAGE = "Image";
	
	private static File getRootFolder(Context context) {
		File folder = null;
		File root = null;

		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			root = Environment.getExternalStorageDirectory();
		}
		else {
			root = new File("/storage/sdcard1/");
			if(!root.exists()) {
				root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
			}
		}

		//root = context.getFilesDir();
		
		if(root!=null) {
			if(root.exists()) {
				folder=new File(root,""+MAIN_FOLDER);
				if(!folder.exists()) {
					folder.mkdir();
				}
			}
		}		
		
		return folder;
	}

	public static File getFolder(Context context, String type) {
		/*
		if(type.equals(AppConstant.FILE_VIDEO)) {
			type = VIDEO_FOLDER;
		}
		else {
			type = IMAGE_FOLDER;
		}*/
		
		File folder = getOrCreateFolder(context, type);
		
		return folder;
	}
	
	private static File getOrCreateFolder(Context context, String folderName) {
		File folder = null;
		File root = getRootFolder(context);
		if (root!=null) {
			folder=new File(root,""+folderName);
			if(!folder.exists()) {
				folder.mkdir();
			}
		}
		return folder;
	}
}
