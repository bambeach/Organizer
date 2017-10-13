package com.bambeach.organizer.data.database;

import android.provider.BaseColumns;

public final class ItemImagePersistenceContract {
    private ItemImagePersistenceContract() {

    }

    public static abstract class ImageEntry implements BaseColumns {
        public static final String TABLE_NAME = "images";
        public static final String COLUMN_NAME_IMAGE_ID = "imageid";
        public static final String COLUMN_NAME_FILENAME = "filename";
        public static final String COLUMN_NAME_ITEM_ID = "itemid";
    }
}
