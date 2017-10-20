package com.bambeach.organizer.itemdetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bambeach.organizer.R;
import com.bambeach.organizer.camera.CameraActivity;
import com.bambeach.organizer.data.Category;
import com.bambeach.organizer.data.Item;
import com.bambeach.organizer.data.ItemImage;
import com.bambeach.organizer.data.database.OrganizerDataSource;
import com.bambeach.organizer.data.database.OrganizerRepository;
import com.squareup.picasso.Picasso;

public class EditItemDetailActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 0x1;

    private ItemImage mImage;
    private ItemImage mTempImage;
    private Item mItem;
    private String mItemId;
    private String mCategoryId;
    private OrganizerRepository mRepository;

    private ImageView itemImageView;
    private EditText itemNameEditText;
    private EditText itemDescriptionEditText;

    private boolean isAddMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.title_add_item);
        }

        mRepository = OrganizerRepository.getInstance(getApplicationContext());

        itemImageView = (ImageView) findViewById(R.id.item_detail_image);

        itemNameEditText = (EditText) findViewById(R.id.item_detail_name);
        itemNameEditText.clearFocus();

        itemDescriptionEditText = (EditText) findViewById(R.id.item_detail_description);
        itemDescriptionEditText.clearFocus();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Item.ITEM_ID_KEY)) {
            if (actionBar != null) {
                actionBar.setTitle(R.string.title_edit_item);
            }
            mItemId = intent.getStringExtra(Item.ITEM_ID_KEY);
            mRepository.getItem(mItemId, new OrganizerDataSource.LoadItemCallback() {
                @Override
                public void onItemLoaded(Item item) {
                    mItem = item;
                    if (mItem.getName() != null && !mItem.getName().isEmpty()) {
                        itemNameEditText.setText(mItem.getName());
                    }
                    if (mItem.getDescription() != null && !mItem.getDescription().isEmpty()) {
                        itemDescriptionEditText.setText(mItem.getDescription());
                    }
                }

                @Override
                public void onDataNotAvailable() {

                }
            });
        }

        if (intent != null && intent.hasExtra(Category.CATEGORY_ID_KEY)) {
            isAddMode = true;
            mCategoryId = intent.getStringExtra(Category.CATEGORY_ID_KEY);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditItemDetailActivity.this);
                dialogBuilder.setTitle(R.string.title_change_photo_dialog)
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setItems(R.array.change_photo_dialog_items_array, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        takePhotoOnClick();
                                        break;
                                    case 1:
                                        choosePhotoOnClick();
                                        break;
                                }
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_item_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                String itemName = itemNameEditText.getText().toString();
                String itemDescription = itemDescriptionEditText.getText().toString();

                Item savedItem;
                if (isAddMode) {
                    savedItem = new Item(itemName, itemDescription, mTempImage.getImageId(), mCategoryId);
                    mRepository.saveItem(savedItem);
                    mImage = new ItemImage(mTempImage.getFileName(), mTempImage.getImageId(), savedItem.getItemId());
                } else {
                    if (mItem.getImageId() != null && !mItem.getImageId().isEmpty()) {
                        savedItem = new Item(itemName, itemDescription, mItemId, mItem.getImageId(), mItem.getCategoryId());
                    } else {
                        savedItem = new Item(itemName, itemDescription, mItemId, mImage.getImageId(), mItem.getCategoryId());
                    }

                    mRepository.updateItem(savedItem);
                }

                mRepository.saveImage(mImage);

                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.putExtra(Item.ITEM_ID_KEY, savedItem.getItemId());
                NavUtils.navigateUpTo(this, upIntent);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            Picasso.with(EditItemDetailActivity.this)
                    .load(selectedImage)
                    .into(itemImageView);
            String path = selectedImage.toString();//.getEncodedPath();
            if (isAddMode) {
                mTempImage = new ItemImage(path);
            } else {
                mImage = new ItemImage(path, mItemId);
            }

//            String[] filePathArg = {MediaStore.Images.Media.DATA};
//
//            Cursor cursor = getContentResolver().query(selectedImage, filePathArg, null, null, null);
//            if (cursor != null && cursor.getCount() > 0) {
//                cursor.moveToFirst();
//                int index = cursor.getColumnIndex(filePathArg[0]);
//                String path = cursor.getString(index);
//
//                if (itemImageView != null) {
//                    itemImageView.setImageBitmap(BitmapFactory.decodeFile(path));
//                }
//            }
//            if (cursor != null) {
//                cursor.close();
//            }
        }
    }

    public void takePhotoOnClick() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    public void choosePhotoOnClick() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }
}
