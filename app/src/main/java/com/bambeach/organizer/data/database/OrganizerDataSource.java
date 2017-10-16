package com.bambeach.organizer.data.database;

import com.bambeach.organizer.data.Category;
import com.bambeach.organizer.data.Item;
import com.bambeach.organizer.data.ItemImage;

import java.util.List;

public interface OrganizerDataSource {

    interface LoadCategoriesCallback {
        void onCategoriesLoaded(List<Category> categories);
        void onDataNotAvailable();
    }

    interface LoadCategoryCallback {
        void onCategoryLoaded(Category category);
        void onDataNotAvailable();
    }

    interface LoadItemsCallback {
        void onItemsLoaded(List<Item> items);
        void onDataNotAvailable();
    }

    interface LoadItemCallback {
        void onItemLoaded(Item item);
        void onDataNotAvailable();
    }

    interface LoadImageCallback {
        void onImageLoaded(ItemImage image);
        void onDataNotAvailable();
    }

    void getCategories(LoadCategoriesCallback categoriesCallback);

    void getCategory(String categoryId, LoadCategoryCallback categoryCallback);

    void saveCategory(Category category);

    void updateCategory(Category category);

    void deleteCategory(String categoryId);

    void getItems(String categoryId, LoadItemsCallback itemsCallback);

    void getItem(String itemId, LoadItemCallback itemCallback);

    void saveItem(Item item);

    void updateItem(Item item);

    void deleteItem(String itemId);

    void deleteAllItems(String categoryId);

    void getImage(String imageId, LoadImageCallback imageCallback);

    void saveImage(ItemImage image);

    void updateImage(ItemImage image);

    void deleteImage(String imageId);
}
