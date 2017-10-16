package com.bambeach.organizer.data.database;

import android.content.Context;

import com.bambeach.organizer.data.Category;
import com.bambeach.organizer.data.Item;
import com.bambeach.organizer.data.ItemImage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrganizerRepository implements OrganizerDataSource {

    private static OrganizerRepository INSTANCE;

    private final LocalOrganizerDataSource dataSource;

    private Map<String, Category> cachedCategories;
    private Map<String, Item> cachedItems;
    private Map<String, ItemImage> cachedImages;

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
            categoriesCallback.onCategoriesLoaded(new ArrayList<>(cachedCategories.values()));
            return;
        }

        dataSource.getCategories(new LoadCategoriesCallback() {
            @Override
            public void onCategoriesLoaded(List<Category> categories) {
                if (cachedCategories == null) {
                    cachedCategories = new LinkedHashMap<>(categories.size());
                }
                cachedCategories.clear();
                for (Category category : categories) {
                    cachedCategories.put(category.getCategoryId(), category);
                }
                categoriesCallback.onCategoriesLoaded(new ArrayList<>(cachedCategories.values()));
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
                    cachedCategories = new LinkedHashMap<>();
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
        if (category == null) {
            return;
        }

        dataSource.saveCategory(category);
        if (cachedCategories == null) {
            cachedCategories = new LinkedHashMap<>();
        }
        cachedCategories.put(category.getCategoryId(), category);
    }

    @Override
    public void updateCategory(Category category) {
        dataSource.updateCategory(category);

        Category updatedCategory = new Category(category.getName(), category.getCategoryId());
        if (cachedCategories == null) {
            cachedCategories = new LinkedHashMap<>();
        }
        cachedCategories.put(category.getCategoryId(), updatedCategory);
    }

    @Override
    public void deleteCategory(String categoryId) {
        if (categoryId == null || categoryId.isEmpty()) {
            return;
        }
        dataSource.deleteCategory(categoryId);
        cachedCategories.remove(categoryId);
    }

    @Override
    public void getItems(String categoryId, final LoadItemsCallback itemsCallback) {
        if (categoryId == null || itemsCallback == null) {
            return;
        }

//        if (cachedItems != null) {
//            itemsCallback.onItemsLoaded(new ArrayList<>(cachedItems.values()));
//            return;
//        }

        dataSource.getItems(categoryId, new LoadItemsCallback() {
            @Override
            public void onItemsLoaded(List<Item> items) {
                if (cachedItems == null) {
                    cachedItems = new LinkedHashMap<>(items.size());
                }
                cachedItems.clear();
                for (Item item : items) {
                    cachedItems.put(item.getItemId(), item);
                }
                itemsCallback.onItemsLoaded(new ArrayList<>(cachedItems.values()));
            }

            @Override
            public void onDataNotAvailable() {
                itemsCallback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getItem(String itemId, final LoadItemCallback itemCallback) {
        if (itemId == null || itemCallback == null) {
            return;
        }

        Item item = null;
        if (cachedItems != null && !cachedItems.isEmpty()) {
            item = cachedItems.get(itemId);
        }

        if (item != null) {
            itemCallback.onItemLoaded(item);
            return;
        }

        dataSource.getItem(itemId, new LoadItemCallback() {
            @Override
            public void onItemLoaded(Item item) {
                if (cachedItems == null) {
                    cachedItems = new LinkedHashMap<>();
                }
                cachedItems.put(item.getItemId(), item);
                itemCallback.onItemLoaded(item);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void saveItem(Item item) {
        if (item == null) {
            return;
        }

        dataSource.saveItem(item);
        if (cachedItems == null) {
            cachedItems = new LinkedHashMap<>();
        }
        cachedItems.put(item.getItemId(), item);
    }

    @Override
    public void updateItem(Item item) {
        dataSource.updateItem(item);

        Item updatedItem = new Item(item.getName(), item.getDescription(), item.getItemId(), item.getImageId(), item.getCategoryId());
        if (cachedItems == null) {
            cachedItems = new LinkedHashMap<>();
        }
        cachedItems.put(item.getItemId(), updatedItem);
    }

    @Override
    public void deleteItem(String itemId) {
        if (itemId == null || itemId.isEmpty()) {
            return;
        }
        dataSource.deleteItem(itemId);
        cachedItems.remove(itemId);
    }

    @Override
    public void deleteAllItems(String categoryId) {
        if (categoryId == null || categoryId.isEmpty()) {
            return;
        }
        dataSource.deleteAllItems(categoryId);

        if (cachedItems != null) {
            cachedItems.clear();
        }
    }

    @Override
    public void getImage(String imageId, final LoadImageCallback imageCallback) {
        if (imageId == null || imageCallback == null) {
            return;
        }

        ItemImage image = null;
        if (cachedImages != null && !cachedImages.isEmpty()) {
            image = cachedImages.get(imageId);
        }

        if (image != null) {
            imageCallback.onImageLoaded(image);
            return;
        }

        dataSource.getImage(imageId, new LoadImageCallback() {
            @Override
            public void onImageLoaded(ItemImage image) {
                if (cachedImages == null) {
                    cachedImages = new LinkedHashMap<>();
                }
                cachedImages.put(image.getImageId(), image);
                imageCallback.onImageLoaded(image);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void saveImage(ItemImage image) {
        if (image == null) {
            return;
        }

        dataSource.saveImage(image);
        if (cachedImages == null) {
            cachedImages = new LinkedHashMap<>();
        }
        cachedImages.put(image.getImageId(), image);
    }

    @Override
    public void updateImage(ItemImage image) {
        dataSource.updateImage(image);

        ItemImage updatedImage = new ItemImage(image.getFileName(), image.getImageId(), image.getItemId());
        if (cachedImages == null) {
            cachedImages = new LinkedHashMap<>();
        }
        cachedImages.put(image.getImageId(), updatedImage);
    }

    @Override
    public void deleteImage(String imageId) {
        if (imageId == null || imageId.isEmpty()) {
            return;
        }
        dataSource.deleteImage(imageId);
        cachedItems.remove(imageId);
    }
}
