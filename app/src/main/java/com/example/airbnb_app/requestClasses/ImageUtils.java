package com.example.airbnb_app.requestClasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {

    public static File saveBitmapToFile(Context context, Bitmap bitmap, String fileName) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos); // Adjust quality as needed
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return imageFile;
    }

    public static Bitmap loadBitmapFromFile(String filePath) {
        return BitmapFactory.decodeFile(filePath);
    }
}
