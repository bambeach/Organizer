package com.bambeach.organizer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bambeach.organizer.R;
import com.bambeach.organizer.data.ImageIO;
import com.bambeach.organizer.data.Item;
import com.bambeach.organizer.items.ItemFragment.OnListFragmentInteractionListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class ItemsRecyclerViewAdapter extends RecyclerView.Adapter<ItemsRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Item> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ItemsRecyclerViewAdapter(Context context, List<Item> items, OnListFragmentInteractionListener listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //holder.mImageView.setText(mValues.get(position).id);
        String imageId = holder.mItem.getImageId();
        File imageFile = ImageIO.getImageFile(imageId);
        if (!imageId.equals("")) {
            //holder.mImageView.setImageResource(android.R.drawable.gallery_thumb);
            Picasso.with(mContext)
                    .load(imageFile)
                    .resize(200, 200)
                    .centerCrop()
                    .into(holder.mImageView);
        }
        holder.mTextView.setText(mValues.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setItems(List<Item> items) {
        mValues.clear();
        mValues = items;
        notifyDataSetChanged();
//        notifyItemInserted(items.size() - 1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mTextView;
        public Item mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.item_image);
            mTextView = (TextView) view.findViewById(R.id.item_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText() + "'";
        }
    }
}
