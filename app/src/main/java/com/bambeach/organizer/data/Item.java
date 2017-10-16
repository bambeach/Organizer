package com.bambeach.organizer.data;

import java.util.UUID;

public final class Item {

    public static final String ITEM_ID_KEY = "item_id_key";

    private String mName;
    private String mDescription;
    private String mItemId;
    private String mImageId;
    private String mCategoryId;

    public Item(String name, String categoryId) {
        this(name, null, UUID.randomUUID().toString(), "", categoryId);
    }
    public Item(String name, String description, String categoryId) {
        this(name, description, UUID.randomUUID().toString(), "", categoryId);
    }

    public Item(String name, String description, String itemId, String imageId, String categoryId) {
        mName = name;
        mDescription = description;
        mItemId = itemId;
        mImageId = imageId;
        mCategoryId = categoryId;
    }

    public String getName() {
        return mName;
    }

//    public void setName(String name) {
//        mName = name;
//    }

    public String getDescription() {
        return mDescription;
    }

    public String getItemId() {
        return mItemId;
    }

    public String getImageId() {
        return mImageId;
    }

    public String getCategoryId() {
        return  mCategoryId;
    }
}
