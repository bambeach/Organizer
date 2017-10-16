package com.bambeach.organizer.items;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.bambeach.organizer.R;
import com.bambeach.organizer.data.Category;
import com.bambeach.organizer.data.Item;
import com.bambeach.organizer.data.database.OrganizerDataSource;
import com.bambeach.organizer.data.database.OrganizerRepository;
import com.bambeach.organizer.itemdetail.ItemDetailActivity;

public class ItemsActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    private Category mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        Intent intent = getIntent();
        String categoryId = null;
        if (intent != null && intent.hasExtra(Category.CATEGORY_ID_KEY)) {
            categoryId = intent.getStringExtra(Category.CATEGORY_ID_KEY);
        }

        if (categoryId != null) {
            OrganizerRepository.getInstance(this).getCategory(categoryId, new OrganizerDataSource.LoadCategoryCallback() {
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(ItemsActivity.this);
                builder.setView(R.layout.dialog_add_item)
                        .setTitle(R.string.title_add_item_dialog)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlertDialog view = (AlertDialog) dialog;
                                EditText text = (EditText) view.findViewById(R.id.item_name_edit_text);
                                String itemName = null;
                                if (text != null) {
                                    itemName = text.getText().toString();
                                }
                                if (itemName == null) {
                                    dialog.dismiss();
                                }
                                Item item = new Item(itemName, mCategory.getCategoryId());
                                OrganizerRepository repository = OrganizerRepository.getInstance(ItemsActivity.this);
                                repository.saveItem(item);
                                ItemFragment fragment = (ItemFragment) getSupportFragmentManager().findFragmentByTag(ItemFragment.TAG);
                                if (fragment != null) {
                                    fragment.refreshData(mCategory.getCategoryId());
                                }
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
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
                OrganizerRepository.getInstance(this).deleteAllItems(mCategory.getCategoryId());
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
}
