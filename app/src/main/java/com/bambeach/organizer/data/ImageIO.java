package com.bambeach.organizer.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageIO {

    //region Data Members

    private static final String TAG = ImageIO.class.getSimpleName();
    private static final String JPG_EXTENSION = ".jpg";
    private static File rootDirectory = null;
    private static Context context;

    //endregion Data Members

    //region Constructors

    private ImageIO() { }

    protected static void initialize(Context context) {
        ImageIO.context = context;
//        rootDirectory = context.getExternalFilesDir()
    }

    //endregion Constructors

    public static void delete(String imageId) {
        File file = getImageFile(imageId);
        Picasso.with(context).invalidate(file);
        FileIO.delete(file);
    }

    public static String addExtension(String imageId) {
        return imageId + JPG_EXTENSION;
    }

//    public static String addExtension(String fileName){
//        return fileName + JPG_EXTENSION;
//    }

    //region Images

    public static void saveImage(String imageId, byte[] image) {
        File file = getImageFile(imageId);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(image);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    public static void saveImage(String imageId, Bitmap bitmap) {
        File file = getImageFile(imageId);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Error accessing file: " + e.getMessage());
        } finally {
            if (fos != null) { try { fos.close(); } catch (IOException ignored) { } }
        }
    }

//    public static void saveImage(String filename, Bitmap bitmap) {
//        File file = getImageFile(filename);
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos);
//            fos.flush();
//            fos.close();
//        } catch (FileNotFoundException e) {
//            Log.e(TAG, "File not found: " + e.getMessage());
//        } catch (IOException e) {
//            Log.e(TAG, "Error accessing file: " + e.getMessage());
//        } finally {
//            if (fos != null) { try { fos.close(); } catch (IOException ignored) { } }
//        }
//    }

    public static File getImageFile(String imageId) {
        File imageDirectory = FileIO.getImagesDirectory();
        return new File(imageDirectory, addExtension(imageId));
    }

    public static File getExternalImageFile(String path) {
        return new File(path);
    }

//    public static File getImageFile(String filename){
//        File imageDirectory = FileIO.getImagesDirectory();
//        return new File(imageDirectory, addExtension(filename));
//    }

    //endregion Images

//    //region Exif Interface
//
//    public static ExifInterface loadExifInterface(long imageId) {
//        File imageFile = getImageFile(imageId);
//        try {
//            return new ExifInterface(imageFile.toString());
//        } catch (IOException e) {
//            Log.e(TAG, "Unable to open exif interface file: " + imageFile.toString()
//                    + ": " + e.getMessage());
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    //endregion Exif Interface
}
