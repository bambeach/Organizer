package com.bambeach.organizer.categories;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bambeach.organizer.R;
import com.bambeach.organizer.data.Category;
import com.bambeach.organizer.items.ItemsActivity;

public class CategoriesActivity extends AppCompatActivity implements CategoriesFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        CategoriesFragment fragment = CategoriesFragment.newInstance(2);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment, CategoriesFragment.TAG)
                .commit();
    }

    @Override
    public void onListFragmentInteraction(Category category) {
        Intent intent = new Intent(this, ItemsActivity.class);
        intent.putExtra(Category.CATEGORY_ID_KEY, category.getCategoryId());
        startActivity(intent);
    }
}
