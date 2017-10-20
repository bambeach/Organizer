package com.bambeach.organizer.data;

import java.util.UUID;

public final class ItemImage {
    private final String mFileName;
    private final String mImageId;
    private final String mItemId;

    public ItemImage(String filename) {
        this(filename, UUID.randomUUID().toString(), null);
    }

    public ItemImage(String filename, String itemId) {
        this(filename, UUID.randomUUID().toString(), itemId);
    }

    public ItemImage(String fileName, String imageId, String itemId) {
        mFileName = fileName;
        mImageId = imageId;
        mItemId = itemId;
    }

    public String getFileName() {
        return mFileName;
    }

    public String getImageId() {
        return mImageId;
    }

    public String getItemId() {
        return mItemId;
    }
}
