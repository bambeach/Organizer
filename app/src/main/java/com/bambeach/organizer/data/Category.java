package com.bambeach.organizer.data;

import java.util.UUID;

public final class Category {

    public static final String CATEGORY_ID_KEY = "category_id_key";

    private final String mName;
    private final String mCategoryId;

    public Category(String name) {
        this(name, UUID.randomUUID().toString());
    }

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
