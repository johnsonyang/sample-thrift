package com.sample.userprofile;

/**
 * Created by keerthana_baskaran on 12/29/14.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class CommonUtility {

    private static final String TAG = "CommonUtility";

    public static void hideSoftKeyboard(final View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void pickPhoto(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        activity.startActivityForResult(intent, requestCode);
    }

    public static Uri takePhoto(Activity activity, int requestCode) {
        Uri uri = getOutputMediaFileUri();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(android.provider.MediaStore.EXTRA_VIDEO_QUALITY, 0);
        activity.startActivityForResult(intent, requestCode);
        return uri;
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (FileNotFoundException e) {
            Log.d(TAG, "getBitmapFromUri", e);
        } catch (IOException e) {
            Log.d(TAG, "getBitmapFromUri", e);
        } catch (Exception e) {
            Log.d(TAG, "getBitmapFromUri", e);
        }
        return null;
    }

    private static Uri getOutputMediaFileUri() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Camera");

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");

        return Uri.fromFile(mediaFile);
    }

}
