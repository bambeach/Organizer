package com.bambeach.organizer.items;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bambeach.organizer.R;
import com.bambeach.organizer.adapters.ItemsRecyclerViewAdapter;
import com.bambeach.organizer.data.Category;
import com.bambeach.organizer.data.Item;
import com.bambeach.organizer.data.database.OrganizerDataSource;
import com.bambeach.organizer.data.database.OrganizerRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment {

    public static final String TAG = "ItemFragment";

    private int mColumnCount = 2;
    private String mCategoryId;
    private OnListFragmentInteractionListener mListener;
    private OrganizerRepository mRepository;
    private ItemsRecyclerViewAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    public static ItemFragment newInstance(String categoryId) {
        ItemFragment itemFragment = new ItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Category.CATEGORY_ID_KEY, categoryId);
        itemFragment.setArguments(bundle);
        return itemFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(Category.CATEGORY_ID_KEY)) {
            mCategoryId = bundle.getString(Category.CATEGORY_ID_KEY);
        }
        mRepository = OrganizerRepository.getInstance(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the mAdapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            if (mCategoryId != null) {
                mRepository.getItems(mCategoryId, new OrganizerDataSource.LoadItemsCallback() {
                    @Override
                    public void onItemsLoaded(List<Item> items) {
                        mAdapter = new ItemsRecyclerViewAdapter(getContext(), items, mListener);
                        recyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        mAdapter = new ItemsRecyclerViewAdapter(getContext(), new ArrayList<Item>(0), mListener);
                        recyclerView.setAdapter(mAdapter);
                    }
                });
            }
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            refreshData(mCategoryId);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void refreshData(String categoryId) {
        if (mAdapter != null) {
            mRepository.getItems(categoryId, new OrganizerDataSource.LoadItemsCallback() {
                @Override
                public void onItemsLoaded(List<Item> items) {
                    mAdapter.setItems(items);
                }

                @Override
                public void onDataNotAvailable() {
                    mAdapter.setItems(new ArrayList<Item>());
                }
            });
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Item item);
    }
}
