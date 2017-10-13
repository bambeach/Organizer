package com.bambeach.organizer.data.database;

import android.provider.BaseColumns;

public final class CategoryPersistenceContract {
    private CategoryPersistenceContract() {

    }

    public static abstract class CategoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_NAME_CATEGORY_ID = "categoryid";
        public static final String COLUMN_NAME_CATEGORY_NAME = "name";
    }
}
