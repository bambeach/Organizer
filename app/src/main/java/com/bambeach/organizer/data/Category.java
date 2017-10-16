package com.bambeach.organizer.data;

public final class Category {

    public static final String CATEGORY_ID_KEY = "category_id_key";

    private final String mName;
    private final String mCategoryId;

    public Category(String name, String categoryId) {
        mName = name;
        mCategoryId = categoryId;
    }

    public String getName() {
        return mName;
    }

    public String getCategoryId() {
        return mCategoryId;
    }
}
