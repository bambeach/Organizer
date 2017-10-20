package com.bambeach.organizer.itemdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bambeach.organizer.R;
import com.bambeach.organizer.data.ImageIO;
import com.bambeach.organizer.data.Item;
import com.bambeach.organizer.data.database.OrganizerDataSource;
import com.bambeach.organizer.data.database.OrganizerRepository;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ItemDetailActivity extends AppCompatActivity {

    private Item mItem;
    private ImageView mItemImageView;
    private TextView mItemDescriptionTextView;
    private OrganizerRepository mRepository;

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

        mRepository = OrganizerRepository.getInstance(getApplicationContext());

        mItemDescriptionTextView = (TextView) findViewById(R.id.item_detail_description);
        mItemImageView = (ImageView) findViewById(R.id.item_detail_image);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Item.ITEM_ID_KEY)) {
            String itemId = intent.getStringExtra(Item.ITEM_ID_KEY);
            mRepository.getItem(itemId, new OrganizerDataSource.LoadItemCallback() {
                @Override
                public void onItemLoaded(Item item) {
                    if (item == null) {
                        return;
                    }
                    mItem = item;
                    loadItemDetails();
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
                Intent intent = new Intent(ItemDetailActivity.this, EditItemDetailActivity.class);
                intent.putExtra(Item.ITEM_ID_KEY, mItem.getItemId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mItem != null) {
            loadItemDetails();
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
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_delete:
                mRepository.deleteItem(mItem.getItemId());
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadItemDetails() {
        mItemDescriptionTextView.setText(mItem.getDescription());
        String imageId = mItem.getImageId();
        if (!imageId.equals("")) {
            File imageFile = ImageIO.getImageFile(imageId);
            Picasso.with(ItemDetailActivity.this)
                    .load(imageFile)
                    .resize(500, 500)
                    .centerCrop()
                    .into(mItemImageView);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mItem.getName());
        }
    }
}
