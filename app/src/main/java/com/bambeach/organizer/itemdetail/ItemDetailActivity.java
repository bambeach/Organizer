package com.bambeach.organizer.itemdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bambeach.organizer.R;
import com.bambeach.organizer.data.Item;
import com.bambeach.organizer.data.database.OrganizerDataSource;
import com.bambeach.organizer.data.database.OrganizerRepository;

public class ItemDetailActivity extends AppCompatActivity {

    private Item mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Item.ITEM_ID_KEY)) {
            String itemId = intent.getStringExtra(Item.ITEM_ID_KEY);
            OrganizerRepository.getInstance(this).getItem(itemId, new OrganizerDataSource.LoadItemCallback() {
                @Override
                public void onItemLoaded(Item item) {
                    if (item == null) {
                        return;
                    }
                    mItem = item;
                    ActionBar actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.setTitle(mItem.getName());
                    }
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
                Snackbar.make(view, "Editing the Item would happen here", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_delete:
                OrganizerRepository.getInstance(this).deleteItem(mItem.getItemId());
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
