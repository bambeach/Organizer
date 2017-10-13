package com.bambeach.organizer.data;

public final class Item {
    private String mName;
    private String mDescription;
    private String mItemId;
    private String mCategoryId;

    public Item(String name, String description, String itemId, String categoryId) {
        mName = name;
        mDescription = description;
        mItemId = itemId;
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

    public String getCategoryId() {
        return  mCategoryId;
    }
}
