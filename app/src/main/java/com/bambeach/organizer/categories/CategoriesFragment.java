package com.bambeach.organizer.categories;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bambeach.organizer.R;
import com.bambeach.organizer.adapters.CategoriesRecyclerViewAdapter;
import com.bambeach.organizer.data.Category;
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
public class CategoriesFragment extends Fragment {

    public static final String TAG = "CategoriesFragment";
    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;
    private OrganizerRepository mRepository;
    private CategoriesRecyclerViewAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CategoriesFragment() {
    }

    public static CategoriesFragment newInstance(int columnCount) {
        return new CategoriesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRepository = OrganizerRepository.getInstance(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);

        // Set the mAdapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            recyclerView.setHasFixedSize(true);
            mRepository.getCategories(new OrganizerDataSource.LoadCategoriesCallback() {
                @Override
                public void onCategoriesLoaded(List<Category> categories) {
                    mAdapter = new CategoriesRecyclerViewAdapter(categories, mListener);
                    recyclerView.setAdapter(mAdapter);
                }

                @Override
                public void onDataNotAvailable() {
                    mAdapter = new CategoriesRecyclerViewAdapter(new ArrayList<Category>(0), mListener);
                    recyclerView.setAdapter(mAdapter);
                }
            });

        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            refreshData();
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

    public void refreshData() {
        if (mAdapter != null) {
            mRepository.getCategories(new OrganizerDataSource.LoadCategoriesCallback() {
                @Override
                public void onCategoriesLoaded(List<Category> categories) {
                    mAdapter.setCategoryList(categories);
                }

                @Override
                public void onDataNotAvailable() {
                    mAdapter.setCategoryList(new ArrayList<Category>());
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
        void onListFragmentInteraction(Category category);
    }
}
