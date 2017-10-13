package com.bambeach.organizer.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CategoryDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Categories.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CategoryPersistenceContract.CategoryEntry.TABLE_NAME + " (" +
                    CategoryPersistenceContract.CategoryEntry.COLUMN_NAME_CATEGORY_ID + TEXT_TYPE + " PRIMARY KEY," +
                    CategoryPersistenceContract.CategoryEntry.COLUMN_NAME_CATEGORY_NAME + TEXT_TYPE + " )";

    public CategoryDbHelper(Context context) {
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
