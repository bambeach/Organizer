package com.bambeach.organizer.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bambeach.organizer.data.Category;
import com.bambeach.organizer.data.Item;
import com.bambeach.organizer.data.ItemImage;

import java.util.ArrayList;
import java.util.List;

class LocalOrganizerDataSource implements OrganizerDataSource {
    private static LocalOrganizerDataSource INSTANCE;

    private CategoryDbHelper categoryDbHelper;
    private ItemsDbHelper itemsDbHelper;
    private ImageDbHelper imageDbHelper;

    static LocalOrganizerDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalOrganizerDataSource(context);
        }
        return INSTANCE;
    }

    private LocalOrganizerDataSource(Context context) {
        categoryDbHelper = new CategoryDbHelper(context);
        itemsDbHelper = new ItemsDbHelper(context);
        imageDbHelper = new ImageDbHelper(context);
    }

    @Override
    public void getCategories(LoadCategoriesCallback categoriesCallback) {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase database = categoryDbHelper.getReadableDatabase();

        String[] projection = {
                CategoryPersistenceContract.CategoryEntry.COLUMN_NAME_CATEGORY_ID,
                CategoryPersistenceContract.CategoryEntry.COLUMN_NAME_CATEGORY_NAME
        };

        Cursor cursor = database.query(CategoryPersistenceContract.CategoryEntry.TABLE_NAME, projection, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String categoryId = cursor.getString(cursor.getColumnIndexOrThrow(CategoryPersistenceContract.CategoryEntry.COLUMN_NAME_CATEGORY_ID));
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(CategoryPersistenceContract.CategoryEntry.COLUMN_NAME_CATEGORY_NAME));
                Category category = new Category(categoryName, categoryId);
                categories.add(category);
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        database.close();

        if (categories.isEmpty()) {
            categoriesCallback.onDataNotAvailable();
        } else {
            categoriesCallback.onCategoriesLoaded(categories);
        }
    }

    @Override
    public void getCategory(String categoryId, LoadCategoryCallback categoryCallback) {
        SQLiteDatabase database = categoryDbHelper.getReadableDatabase();

        String[] projection = {
                CategoryPersistenceContract.CategoryEntry.COLUMN_NAME_CATEGORY_ID,
                CategoryPersistenceContract.CategoryEntry.COLUMN_NAME_CATEGORY_NAME
        };

        String selection = CategoryPersistenceContract.CategoryEntry.COLUMN_NAME_CATEGORY_ID + " LIKE ?";
        String[] selectionArgs = {categoryId};

        Cursor cursor = database.query(CategoryPersistenceContract.CategoryEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        Category category = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String entryId = cursor.getString(cursor.getColumnIndexOrThrow(CategoryPersistenceContract.CategoryEntry.COLUMN_NAME_CATEGORY_ID));
            String entryName = cursor.getString(cursor.getColumnIndexOrThrow(CategoryPersistenceContract.CategoryEntry.COLUMN_NAME_CATEGORY_NAME));
            category = new Category(entryName, entryId);
        }
        if (cursor != null) {
            cursor.close();
        }

        database.close();

        if (category != null) {
            categoryCallback.onCategoryLoaded(category);
        } else {
            categoryCallback.onDataNotAvailable();
        }
    }

    @Override
    public void saveCategory(Category category) {
        if (category == null) {
            return;
        }

        SQLiteDatabase database = categoryDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CategoryPersistenceContract.CategoryEntry.COLUMN_NAME_CATEGORY_ID, category.getCategoryId());
        values.put(CategoryPersistenceContract.CategoryEntry.COLUMN_NAME_CATEGORY_NAME, category.getName());

        database.insert(CategoryPersistenceContract.CategoryEntry.TABLE_NAME, null, values);
    }

    @Override
    public void updateCategory(Category category) {
        if (category == null) {
            return;
        }

        SQLiteDatabase database = categoryDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CategoryPersistenceContract.CategoryEntry.COLUMN_NAME_CATEGORY_NAME, category.getName());

        String selection = CategoryPersistenceContract.CategoryEntry.COLUMN_NAME_CATEGORY_ID + " LIKE ?";
        String[] selectionArgs = {category.getCategoryId()};

        database.update(CategoryPersistenceContract.CategoryEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    @Override
    public void deleteCategory(String categoryId) {
        SQLiteDatabase database = categoryDbHelper.getWritableDatabase();

        String selection = CategoryPersistenceContract.CategoryEntry.COLUMN_NAME_CATEGORY_ID + " LIKE ?";
        String[] selectionArgs = {categoryId};

        database.delete(CategoryPersistenceContract.CategoryEntry.TABLE_NAME, selection, selectionArgs);
        database.close();
    }

    @Override
    public void getItems(LoadItemsCallback itemsCallback) {
        List<Item> items = new ArrayList<>();
        SQLiteDatabase database = itemsDbHelper.getReadableDatabase();

        String[] projection = {
                ItemPersistenceContract.ItemEntry.COLUMN_NAME_ITEM_ID,
                ItemPersistenceContract.ItemEntry.COLUMN_NAME_ITEM_NAME,
                ItemPersistenceContract.ItemEntry.COLUMN_NAME_DESCRIPTION,
                ItemPersistenceContract.ItemEntry.COLUMN_NAME_CATEGORY_ID
        };

        Cursor cursor = database.query(ItemPersistenceContract.ItemEntry.TABLE_NAME, projection, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String itemId = cursor.getString(cursor.getColumnIndexOrThrow(ItemPersistenceContract.ItemEntry.COLUMN_NAME_ITEM_ID));
                String itemName = cursor.getString(cursor.getColumnIndexOrThrow(ItemPersistenceContract.ItemEntry.COLUMN_NAME_ITEM_NAME));
                String itemDescription = cursor.getString(cursor.getColumnIndexOrThrow(ItemPersistenceContract.ItemEntry.COLUMN_NAME_DESCRIPTION));
                String categoryId = cursor.getString(cursor.getColumnIndexOrThrow(ItemPersistenceContract.ItemEntry.COLUMN_NAME_CATEGORY_ID));
                Item item = new Item(itemName, itemDescription, itemId, categoryId);
                items.add(item);
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        database.close();

        if (items.isEmpty()) {
            itemsCallback.onDataNotAvailable();
        } else {
            itemsCallback.onItemsLoaded(items);
        }
    }

    @Override
    public void getItem(String itemId, LoadItemCallback itemCallback) {
        SQLiteDatabase database = itemsDbHelper.getReadableDatabase();

        String[] projection = {
                ItemPersistenceContract.ItemEntry.COLUMN_NAME_ITEM_ID,
                ItemPersistenceContract.ItemEntry.COLUMN_NAME_ITEM_NAME,
                ItemPersistenceContract.ItemEntry.COLUMN_NAME_DESCRIPTION,
                ItemPersistenceContract.ItemEntry.COLUMN_NAME_CATEGORY_ID
        };

        String selection = ItemPersistenceContract.ItemEntry.COLUMN_NAME_ITEM_ID + " LIKE ?";
        String[] selectionArgs = {itemId};

        Cursor cursor = database.query(ItemPersistenceContract.ItemEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        Item item = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String entryId = cursor.getString(cursor.getColumnIndexOrThrow(ItemPersistenceContract.ItemEntry.COLUMN_NAME_ITEM_ID));
            String entryName = cursor.getString(cursor.getColumnIndexOrThrow(ItemPersistenceContract.ItemEntry.COLUMN_NAME_ITEM_NAME));
            String entryDescription = cursor.getString(cursor.getColumnIndexOrThrow(ItemPersistenceContract.ItemEntry.COLUMN_NAME_DESCRIPTION));
            String categoryId = cursor.getString(cursor.getColumnIndexOrThrow(ItemPersistenceContract.ItemEntry.COLUMN_NAME_CATEGORY_ID));
            item = new Item(entryName, entryDescription, entryId, categoryId);
        }
        if (cursor != null) {
            cursor.close();
        }

        database.close();

        if (item != null) {
            itemCallback.onTaskLoaded(item);
        } else {
            itemCallback.onDataNotAvailable();
        }
    }

    @Override
    public void saveItem(Item item) {
        if (item == null) {
            return;
        }

        SQLiteDatabase database = itemsDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ItemPersistenceContract.ItemEntry.COLUMN_NAME_ITEM_ID, item.getItemId());
        values.put(ItemPersistenceContract.ItemEntry.COLUMN_NAME_ITEM_NAME, item.getName());
        values.put(ItemPersistenceContract.ItemEntry.COLUMN_NAME_DESCRIPTION, item.getDescription());
        values.put(ItemPersistenceContract.ItemEntry.COLUMN_NAME_CATEGORY_ID, item.getCategoryId());

        database.insert(ItemPersistenceContract.ItemEntry.TABLE_NAME, null, values);
        database.close();
    }

    @Override
    public void updateItem(Item item) {
        if (item == null) {
            return;
        }

        SQLiteDatabase database = itemsDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ItemPersistenceContract.ItemEntry.COLUMN_NAME_ITEM_NAME, item.getName());
        values.put(ItemPersistenceContract.ItemEntry.COLUMN_NAME_DESCRIPTION, item.getDescription());
        values.put(ItemPersistenceContract.ItemEntry.COLUMN_NAME_CATEGORY_ID, item.getCategoryId());

        String selection = ItemPersistenceContract.ItemEntry.COLUMN_NAME_ITEM_ID + " LIKE ?";
        String[] selectionArgs = {item.getItemId()};

        database.update(ItemPersistenceContract.ItemEntry.TABLE_NAME, values, selection, selectionArgs);
        database.close();
    }

    @Override
    public void deleteItem(String itemId) {
        SQLiteDatabase database = itemsDbHelper.getWritableDatabase();

        String selection = ItemPersistenceContract.ItemEntry.COLUMN_NAME_ITEM_ID + " LIKE ?";
        String[] selectionArgs = {itemId};

        database.delete(ItemPersistenceContract.ItemEntry.TABLE_NAME, selection, selectionArgs);
        database.close();
    }

    @Override
    public void deleteAllItems(String categoryId) {
        SQLiteDatabase database = itemsDbHelper.getWritableDatabase();

        String selection = ItemPersistenceContract.ItemEntry.COLUMN_NAME_CATEGORY_ID + " LIKE ?";
        String[] selectionArgs = {categoryId};

        database.delete(ItemPersistenceContract.ItemEntry.TABLE_NAME, selection, selectionArgs);
        database.close();
    }

    @Override
    public void getImage(String imageId, LoadImageCallback imageCallback) {
        SQLiteDatabase database = imageDbHelper.getReadableDatabase();

        String[] projection = {
                ItemImagePersistenceContract.ImageEntry.COLUMN_NAME_IMAGE_ID,
                ItemImagePersistenceContract.ImageEntry.COLUMN_NAME_FILENAME,
                ItemImagePersistenceContract.ImageEntry.COLUMN_NAME_ITEM_ID
        };

        String selection = ItemImagePersistenceContract.ImageEntry.COLUMN_NAME_IMAGE_ID + " LIKE ?";
        String[] selectionArgs = {imageId};

        Cursor cursor = database.query(ItemImagePersistenceContract.ImageEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        ItemImage image = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String entryId = cursor.getString(cursor.getColumnIndexOrThrow(ItemImagePersistenceContract.ImageEntry.COLUMN_NAME_IMAGE_ID));
            String entryFilename = cursor.getString(cursor.getColumnIndexOrThrow(ItemImagePersistenceContract.ImageEntry.COLUMN_NAME_FILENAME));
            String itemId = cursor.getString(cursor.getColumnIndexOrThrow(ItemImagePersistenceContract.ImageEntry.COLUMN_NAME_ITEM_ID));
            image = new ItemImage(entryFilename, entryId, itemId);
        }
        if (cursor != null) {
            cursor.close();
        }

        database.close();

        if (image != null) {
            imageCallback.onImageLoaded(image);
        } else {
            imageCallback.onDataNotAvailable();
        }
    }

    @Override
    public void saveImage(ItemImage image) {
        if (image == null) {
            return;
        }

        SQLiteDatabase database = imageDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ItemImagePersistenceContract.ImageEntry.COLUMN_NAME_IMAGE_ID, image.getImageId());
        values.put(ItemImagePersistenceContract.ImageEntry.COLUMN_NAME_FILENAME, image.getFileName());
        values.put(ItemImagePersistenceContract.ImageEntry.COLUMN_NAME_ITEM_ID, image.getItemId());

        database.insert(ItemImagePersistenceContract.ImageEntry.TABLE_NAME, null, values);
        database.close();
    }

    @Override
    public void updateImage(ItemImage image) {
        if (image == null) {
            return;
        }

        SQLiteDatabase database = imageDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ItemImagePersistenceContract.ImageEntry.COLUMN_NAME_FILENAME, image.getFileName());
        values.put(ItemImagePersistenceContract.ImageEntry.COLUMN_NAME_ITEM_ID, image.getItemId());

        String selection = ItemImagePersistenceContract.ImageEntry.COLUMN_NAME_IMAGE_ID + " LIKE ?";
        String[] selectionArgs = {image.getItemId()};

        database.update(ItemImagePersistenceContract.ImageEntry.TABLE_NAME, values, selection, selectionArgs);
        database.close();
    }

    @Override
    public void deleteImage(String imageId) {
        SQLiteDatabase database = imageDbHelper.getWritableDatabase();

        String selection = ItemImagePersistenceContract.ImageEntry.COLUMN_NAME_IMAGE_ID + " LIKE ?";
        String[] selectionArgs = {imageId};

        database.delete(ItemImagePersistenceContract.ImageEntry.TABLE_NAME, selection, selectionArgs);
        database.close();
    }
}
