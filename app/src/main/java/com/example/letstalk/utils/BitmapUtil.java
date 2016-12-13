package com.example.letstalk.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class BitmapUtil {

    public static String encodeImage(String path){
        Bitmap bmp =  BitmapFactory.decodeFile(path);//your image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        bmp.recycle();
        byte[] byteArray = bytes.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encodedImage;
    }

    public static Bitmap decodeImage(String encodedImage){
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
