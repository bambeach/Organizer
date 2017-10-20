package com.bambeach.organizer.data;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileIO {

    //region Data Members

    private static File rootDirectory = null;

    //region Final Strings

    private static final String TAG = FileIO.class.getSimpleName();

    private static final String IMAGES_DIRECTORY_NAME = "images";

    private static final String CATEGORIES_DIRECTORY_NAME = "categories";
    private static final String ITEMS_DIRECTORY_NAME = "items";

    //region Database Constant Strings

    public final static String DATABASE_DIRECTORY_PATH = "/data/databases";

    //endregion Database Constant Strings

    //endregion Final Strings

    //endregion Data Members

    //region Constructors

    private FileIO() { }

    public static void initialize(Context context) {
        ImageIO.initialize(context);
        rootDirectory = context.getFilesDir();
    }

    //endregion Constructors

    /**
     * Recursively deletes this file/directory
     */
    public static void delete(File file) {
        if (file == null || !file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                delete(f);
            }
        }

        boolean isDeleted = file.delete();
        if (!isDeleted) {
            Log.e(TAG, "Unable to delete file: " + file.toString());
        }
    }

    //region Directory Access Methods

    protected static File getRootDirectory() { return rootDirectory; }

    public static File getImagesDirectory() {
        return getDirectory(getRootDirectory(), IMAGES_DIRECTORY_NAME);
    }

    public static File getDatabaseDirectory() {
        return getDirectory(Environment.getDataDirectory(), DATABASE_DIRECTORY_PATH);
    }

    protected static File getCategoriesDirectory() {
        return getDirectory(getRootDirectory(), CATEGORIES_DIRECTORY_NAME);
    }

    protected static File getCategoryDirectory(String categoryId) {
        return getDirectory(getCategoriesDirectory(), categoryId);
    }

    protected static File getItemsDirectory() {
        return getDirectory(getRootDirectory(), ITEMS_DIRECTORY_NAME);
    }

    protected static File getItemDirectory(String itemId) {
        return getDirectory(getItemsDirectory(), itemId);
    }

    private static File getDirectory(File parentDirectory, String directoryName) {
        File directory = new File(parentDirectory, directoryName);
        if (!directory.exists()) {
            boolean created = directory.mkdir();
            if (!created) {
                Log.e(TAG, errorString(directory));
            }
        }
        return directory;
    }

    //endregion Directory Access Methods

    //region File Copy Methods

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    //endregion File Copy Methods

    //region Debug Methods

    private static String errorString(File file) {
        String name = file.getName();
        String fileType = file.isDirectory() ? "directory" : "file";
        return "Unable to create " + name + " " + fileType;
    }

    public static long getFileSize() {
        return getFileSize(getRootDirectory());
    }

    private static long getFileSize(File f) {
        long size = 0;
        if (!f.exists()) return size;

        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFileSize(file);
            }
        } else {
            size = f.length();
        }
        return size;
    }

    public static String ls(File f) {
        if (!f.exists()) return "";

        StringBuilder output = new StringBuilder();
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                output.append(ls(file));
            }
        } else {
            String filename = f.toString();
            output.append(filename).append("\n");
        }
        return output.toString();
    }

    //endregion Debug Methods
}
