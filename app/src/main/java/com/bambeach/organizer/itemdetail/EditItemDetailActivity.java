package com.bambeach.organizer.itemdetail;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bambeach.organizer.R;
import com.bambeach.organizer.camera.CameraActivity;
import com.bambeach.organizer.data.Category;
import com.bambeach.organizer.data.FileIO;
import com.bambeach.organizer.data.ImageIO;
import com.bambeach.organizer.data.Item;
import com.bambeach.organizer.data.ItemImage;
import com.bambeach.organizer.data.database.OrganizerDataSource;
import com.bambeach.organizer.data.database.OrganizerRepository;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

public class EditItemDetailActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int RESULT_LOAD_IMAGE = 0x1;
    private static final int RESULT_CAPTURE_IMAGE = 0x2;
    private static final int REQUEST_CAMERA_PERMISSION = 0x3;

    private ItemImage mImage;
    private ItemImage mTempImage;
    private Item mItem;
    private String mItemId;
    private String mImageId;
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

        FileIO.initialize(getApplicationContext());
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
                if (ContextCompat.checkSelfPermission(EditItemDetailActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditItemDetailActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    return;
                }
                mImageId = UUID.randomUUID().toString();
                File imageFile = ImageIO.getImageFile(mImageId);
                Uri imageUri = FileProvider.getUriForFile(EditItemDetailActivity.this, "com.bambeach.organizer.fileprovider", imageFile);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, RESULT_CAPTURE_IMAGE);
//                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditItemDetailActivity.this);
//                dialogBuilder.setTitle(R.string.title_change_photo_dialog)
//                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .setItems(R.array.change_photo_dialog_items_array, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                switch (which) {
//                                    case 0:
//                                        takePhotoOnClick();
//                                        break;
//                                    case 1:
//                                        choosePhotoOnClick();
//                                        break;
//                                }
//                                dialog.dismiss();
//                            }
//                        })
//                        .create()
//                        .show();
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
                if (mTempImage != null) {
                    if (isAddMode) {
                        savedItem = new Item(itemName, itemDescription, mTempImage.getImageId(), mCategoryId);
                        mRepository.saveItem(savedItem);
                        mImage = new ItemImage(mTempImage.getFileName(), mTempImage.getImageId(), savedItem.getItemId());
                    } else {
                        savedItem = new Item(itemName, itemDescription, mItemId, mTempImage.getImageId(), mItem.getCategoryId());
                        mRepository.updateItem(savedItem);
                    }

                    if (mImage != null) {
                        mRepository.saveImage(mImage);
                    }
                } else {
                    if (isAddMode) {
                        savedItem = new Item(itemName, itemDescription, "", mCategoryId);
                        mRepository.saveItem(savedItem);
                    } else {
                        savedItem = new Item(itemName, itemDescription, mItemId, "", mItem.getCategoryId());
                        mRepository.updateItem(savedItem);
                    }
                }
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

        switch (requestCode) {
            case RESULT_CAPTURE_IMAGE:
                if (resultCode == RESULT_OK) {
                    File imageFile = ImageIO.getImageFile(mImageId);
                    Picasso.with(EditItemDetailActivity.this)
                            .load(imageFile)
                            .into(itemImageView);
                    mTempImage = new ItemImage(String.valueOf(imageFile.lastModified()), mImageId, null);
                }
                break;
//            case RESULT_LOAD_IMAGE:
//                if (resultCode == RESULT_OK && data != null) {
//                    Uri imageData = data.getData();
//                    Bitmap bitmap;
//                    try {
//                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageData);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    String[] filePath = { MediaStore.Images.Media.DATA };
//                    String picturePath = null;
//                    Cursor cursor = getContentResolver().query(imageData,filePath, null, null, null);
//
//                    if (cursor != null && cursor.getCount() > 0) {
//                        cursor.moveToFirst();
//                        int columnIndex = cursor.getColumnIndex(filePath[0]);
//                        picturePath = cursor.getString(columnIndex);
//                    }
//                    if (cursor != null) {
//                        cursor.close();
//                    }
//
//                    String[] segments = picturePath.split("/");
//                    int lastIndex = segments.length - 1;
//                    byte[] bytes = Base64.decode(segments[lastIndex], Base64.DEFAULT);
//                    String decodedString = new String(bytes);
//
//                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
//                    mImageId = UUID.randomUUID().toString();
//                    ImageIO.saveImage(mImageId, thumbnail);
//                    File imageFile = ImageIO.getImageFile(mImageId);
//                    Picasso.with(EditItemDetailActivity.this)
//                            .load(imageFile)
//                            .into(itemImageView);
//                    mTempImage = new ItemImage(imageData.getLastPathSegment(), UUID.randomUUID().toString(), null);
//                }
//                break;
        }
    }

    public void takePhotoOnClick() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            return;
        }
        mImageId = UUID.randomUUID().toString();
        File imageFile = ImageIO.getImageFile(mImageId);
        Uri imageUri = FileProvider.getUriForFile(this, "com.bambeach.organizer.fileprovider", imageFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, RESULT_CAPTURE_IMAGE);
    }

//    public void choosePhotoOnClick() {
//        mImageId = UUID.randomUUID().toString();
//        File imageFile = ImageIO.getImageFile(mImageId);
//        Uri imageUri = FileProvider.getUriForFile(this, "com.bambeach.organizer.fileprovider", imageFile);
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, RESULT_LOAD_IMAGE);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhotoOnClick();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
