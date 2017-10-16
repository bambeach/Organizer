package com.bambeach.organizer.categories;

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
import com.bambeach.organizer.data.database.OrganizerRepository;
import com.bambeach.organizer.items.ItemsActivity;

public class CategoriesActivity extends AppCompatActivity implements CategoriesFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (toolbar != null) {
//            setSupportActionBar(toolbar);
//        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.categories);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(CategoriesActivity.this);
                builder.setView(R.layout.dialog_add_category)
                        .setTitle(R.string.title_add_category_dialog)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlertDialog view = (AlertDialog) dialog;
                                EditText text = (EditText) view.findViewById(R.id.category_name_edit_text);
                                String categoryName = null;
                                if (text != null) {
                                    categoryName = text.getText().toString();
                                }
                                if (categoryName == null) {
                                    dialog.dismiss();
                                }
                                Category category = new Category(categoryName);
                                OrganizerRepository repository = OrganizerRepository.getInstance(CategoriesActivity.this);
                                repository.saveCategory(category);
                                CategoriesFragment fragment = (CategoriesFragment) getSupportFragmentManager().findFragmentByTag(CategoriesFragment.TAG);
                                if (fragment != null) {
                                    fragment.refreshData();
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

        CategoriesFragment fragment = CategoriesFragment.newInstance(2);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.category_fragment_container, fragment, CategoriesFragment.TAG)
                .commit();
    }

    @Override
    public void onListFragmentInteraction(Category category) {
        Intent intent = new Intent(this, ItemsActivity.class);
        intent.putExtra(Category.CATEGORY_ID_KEY, category.getCategoryId());
        startActivity(intent);
    }
}
