package com.bambeach.organizer.data.database;

import android.provider.BaseColumns;

public final class ItemPersistenceContract {
    private ItemPersistenceContract() {

    }

    public static abstract class ItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "items";
        public static final String COLUMN_NAME_ITEM_ID = "itemid";
        public static final String COLUMN_NAME_ITEM_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_CATEGORY_ID = "categoryid";
    }
}
