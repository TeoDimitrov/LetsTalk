package com.wecode.letstalk.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraUtil {

    private Intent mTakePictureIntent;
    private File mPhotoFile;
    static final int REQUEST_TAKE_PHOTO = 1;
    private String mCurrentPhotoPath;

    public CameraUtil() {
        this.mTakePictureIntent = new Intent();
        this.mPhotoFile = new File(this.mCurrentPhotoPath);
    }

    public File dispatchTakePictureIntent(Activity chatActivity) {
        this.mTakePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (this.mTakePictureIntent.resolveActivity(chatActivity.getPackageManager()) != null) {
            this.mPhotoFile = null;
            try {
                this.mPhotoFile = createImageFile(chatActivity);
            } catch (IOException ioe) {

            }
            if (this.mPhotoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(chatActivity, "com.example.android.fileprovider", this.mPhotoFile);
                this.mTakePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                chatActivity.startActivityForResult(this.mTakePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

        return this.mPhotoFile;
    }


    private File createImageFile(Activity chatActivity) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = chatActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
