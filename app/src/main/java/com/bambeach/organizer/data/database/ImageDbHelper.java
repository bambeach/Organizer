package com.bambeach.organizer.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ImageDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Images.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ItemImagePersistenceContract.ImageEntry.TABLE_NAME + " (" +
                    ItemImagePersistenceContract.ImageEntry.COLUMN_NAME_IMAGE_ID + TEXT_TYPE + " PRIMARY KEY," +
                    ItemImagePersistenceContract.ImageEntry.COLUMN_NAME_FILENAME + TEXT_TYPE + COMMA_SEP +
                    ItemImagePersistenceContract.ImageEntry.COLUMN_NAME_ITEM_ID + TEXT_TYPE + " )";

    public ImageDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
