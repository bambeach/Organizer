package com.bambeach.organizer.items;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bambeach.organizer.R;
import com.bambeach.organizer.data.Category;
import com.bambeach.organizer.data.FileIO;
import com.bambeach.organizer.data.Item;
import com.bambeach.organizer.data.database.OrganizerDataSource;
import com.bambeach.organizer.data.database.OrganizerRepository;
import com.bambeach.organizer.itemdetail.EditItemDetailActivity;
import com.bambeach.organizer.itemdetail.ItemDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class ItemsActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    private Category mCategory;
    private OrganizerRepository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        Intent intent = getIntent();
        String categoryId = null;
        if (intent != null && intent.hasExtra(Category.CATEGORY_ID_KEY)) {
            categoryId = intent.getStringExtra(Category.CATEGORY_ID_KEY);
        }

        FileIO.initialize(getApplicationContext());
        mRepository = OrganizerRepository.getInstance(getApplicationContext());

        if (categoryId != null) {
            mRepository.getCategory(categoryId, new OrganizerDataSource.LoadCategoryCallback() {
                @Override
                public void onCategoryLoaded(Category category) {
                    if (category != null) {
                        mCategory = category;
                    }
                    ActionBar actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.setTitle(mCategory.getName());
                    }

                    ItemFragment fragment = ItemFragment.newInstance(mCategory.getCategoryId());
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.items_fragment_container, fragment, ItemFragment.TAG)
                            .commit();
                }

                @Override
                public void onDataNotAvailable() {

                }
            });
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemsActivity.this, EditItemDetailActivity.class);
                intent.putExtra(Category.CATEGORY_ID_KEY, mCategory.getCategoryId());
                startActivity(intent);
//                mRepository.getItems(mCategory.getCategoryId(), new OrganizerDataSource.LoadItemsCallback() {
//                    @Override
//                    public void onItemsLoaded(List<Item> items) {
//                        List<String> itemNames = new ArrayList<>(items.size());
//                        for (Item item : items) {
//                            itemNames.add(item.getName());
//                        }
//                        addNewItem(itemNames);
//                    }
//
//                    @Override
//                    public void onDataNotAvailable() {
//                        addNewItem(new ArrayList<String>());
//                    }
//                });
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item_detail_activity, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                mRepository.deleteAllItems(mCategory.getCategoryId());
                mRepository.deleteCategory(mCategory.getCategoryId());
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Item item) {
        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra(Item.ITEM_ID_KEY, item.getItemId());
        startActivity(intent);
    }

    private void addNewItem(List<String> itemNames) {
        int counter = 1;
        String newItemName = "Item" + counter;
        while (itemNames.contains(newItemName)) {
            counter++;
            newItemName = "Item" + counter;
        }

        Item item = new Item(newItemName, mCategory.getCategoryId());
        mRepository.saveItem(item);

        Intent intent = new Intent(ItemsActivity.this, EditItemDetailActivity.class);
        intent.putExtra(Item.ITEM_ID_KEY, item.getItemId());
        startActivity(intent);
    }
}
