package com.bambeach.organizer.data.database;

import android.content.Context;

import com.bambeach.organizer.data.Category;
import com.bambeach.organizer.data.Item;
import com.bambeach.organizer.data.ItemImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganizerRepository implements OrganizerDataSource {

    private static OrganizerRepository INSTANCE;

    private final LocalOrganizerDataSource dataSource;

    Map<String, Category> cachedCategories;
    Map<String, Item> cachedItems;
    Map<String, ItemImage> cachedImages;

    private OrganizerRepository(LocalOrganizerDataSource localOrganizerDataSource){
        dataSource = localOrganizerDataSource;
    }

    public static OrganizerRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new OrganizerRepository(LocalOrganizerDataSource.getInstance(context));
        }
        return INSTANCE;
    }

    @Override
    public void getCategories(final LoadCategoriesCallback categoriesCallback) {
        if (categoriesCallback == null) {
            return;
        }

        if (cachedCategories != null) {
            categoriesCallback.onCategoriesLoaded(new ArrayList<Category>(cachedCategories.values()));
            return;
        }

        dataSource.getCategories(new LoadCategoriesCallback() {
            @Override
            public void onCategoriesLoaded(List<Category> categories) {
                //refreshCategoriesCache(categories);
                categoriesCallback.onCategoriesLoaded(new ArrayList<Category>(cachedCategories.values()));
            }

            @Override
            public void onDataNotAvailable() {
                categoriesCallback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getCategory(String categoryId, final LoadCategoryCallback categoryCallback) {
        if (categoryId == null || categoryCallback == null) {
            return;
        }

        Category category = null;
        if (cachedCategories != null && !cachedCategories.isEmpty()) {
            category = cachedCategories.get(categoryId);
        }

        if (category != null) {
            categoryCallback.onCategoryLoaded(category);
            return;
        }

        dataSource.getCategory(categoryId, new LoadCategoryCallback() {
            @Override
            public void onCategoryLoaded(Category category) {
                if (cachedCategories == null) {
                    cachedCategories = new HashMap<String, Category>();
                }
                cachedCategories.put(category.getCategoryId(), category);
                categoryCallback.onCategoryLoaded(category);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void saveCategory(Category category) {

    }

    @Override
    public void updateCategory(Category category) {

    }

    @Override
    public void deleteCategory(String categoryId) {

    }

    @Override
    public void getItems(LoadItemsCallback itemsCallback) {

    }

    @Override
    public void getItem(String itemId, LoadItemCallback itemCallback) {

    }

    @Override
    public void saveItem(Item item) {

    }

    @Override
    public void updateItem(Item item) {

    }

    @Override
    public void deleteItem(String itemId) {

    }

    @Override
    public void deleteAllItems(String categoryId) {

    }

    @Override
    public void getImage(String imageId, LoadImageCallback imageCallback) {

    }

    @Override
    public void saveImage(ItemImage image) {

    }

    @Override
    public void updateImage(ItemImage image) {

    }

    @Override
    public void deleteImage(String imageId) {

    }
}
