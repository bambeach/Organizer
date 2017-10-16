package com.bambeach.organizer.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bambeach.organizer.R;
import com.bambeach.organizer.categories.CategoriesFragment.OnListFragmentInteractionListener;
import com.bambeach.organizer.categories.dummy.DummyContent.DummyItem;
import com.bambeach.organizer.data.Category;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CategoriesRecyclerViewAdapter extends RecyclerView.Adapter<CategoriesRecyclerViewAdapter.ViewHolder> {

    private final List<Category> categoryList;
    private final OnListFragmentInteractionListener mListener;

    public CategoriesRecyclerViewAdapter(List<Category> items, OnListFragmentInteractionListener listener) {
        categoryList = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.category = categoryList.get(position);
        holder.categoryName.setText(categoryList.get(position).getName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.category);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final CardView cardView;
        public final TextView categoryName;
        public Category category;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            categoryName = (TextView) view.findViewById(R.id.category_title);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + categoryName.getText() + "'";
        }
    }
}
