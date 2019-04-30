package com.example.tim.romaniitedomum.artefact;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tim.romaniitedomum.ApplicationClass;
import com.example.tim.romaniitedomum.R;
import com.example.tim.romaniitedomum.Util.UserScreen;
import com.example.tim.romaniitedomum.Util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TimStaats 03.03.2019
 */

public class ArtefactListFragment extends Fragment {

    private static final String TAG = "ArtefactListFragment";

    public static final int SPAN_COUNT = 2;

    private ArtefactActivity artefactActivity;

    private View mProgressViewArtefactList;
    private View mFormViewArtefactList;
    private TextView tvLoadArtefactList;

    private RecyclerView mRecyclerView;
    private ArtefactListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RelativeLayout filterLayout;
    private EditText etFilter;
    private Spinner spinnerFilterCategory;
    private CategoryAdapter mCategoryAdapter;
    private ArrayList<Category> mCategoryList;
    private Category mCategory;
    private boolean isFilterActive = false;

    private Button btnFilterApply;
    private String filterString = "";
    private List<Artefact> artefactsList;
    private List<Artefact> filteredList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artefact_list, container, false);

        initArtefactList(view);

        mAdapter = new ArtefactListAdapter(artefactsList, ApplicationClass.loader);
        mRecyclerView.setLayoutManager(new GridLayoutManager(artefactActivity, SPAN_COUNT));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ArtefactListAdapter.OnItemClickListener() {
            @Override
            public void onItemclick(int position) {

                ApplicationClass.position = position;
                Fragment artefactDetailFragment = new ArtefactDetailFragment();
                artefactActivity.fragmentSwitcher2(artefactDetailFragment, true, "artefactDetailFragment");
            }
        });

        mCategoryAdapter = new CategoryAdapter(artefactActivity, mCategoryList);
        spinnerFilterCategory.setAdapter(mCategoryAdapter);

        spinnerFilterCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCategory = (Category) parent.getItemAtPosition(position);
                Log.d(TAG, "onItemSelected: categoryFilter: " + mCategory.getCategoryName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnFilterApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.isCategoryFilter = true;
                if (mCategory != null) {
                    mAdapter.getFilter().filter(mCategory.getCategoryName());
                }
            }
        });

        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                Log.d(TAG, "performFiltering: onTextChanged: Text: " + charSequence);
                mAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu_artefact_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.list_filter:
                if (!isFilterActive) {
                    Log.d(TAG, "onOptionsItemSelected: filter mode on");
                    item.setIcon(R.drawable.ic_cancel_filter);
                    filterLayout.setVisibility(View.VISIBLE);
                } else {
                    Log.d(TAG, "onOptionsItemSelected: cancle filter mode");
                    item.setIcon(R.drawable.ic_filter);
                    filterLayout.setVisibility(View.GONE);
                    etFilter.setText("");
                    artefactsList = ApplicationClass.mArtefactList;
                    mAdapter.notifyDataSetChanged();
                }
                isFilterActive = !isFilterActive;
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Category> populateCategoryList() {
        ArrayList<Category> list = new ArrayList<>();

        list.add(new Category("SaltAndPepper", R.drawable.ic_salt_and_pepper));
        list.add(new Category(Util.CATEGORY_BASILIKA, R.drawable.ic_map_basilica));
        list.add(new Category(Util.CATEGORY_BOGEN, R.drawable.ic_map_bogen));
        list.add(new Category(Util.CATEGORY_CHRISTENTUM, R.drawable.ic_map_christentum));
        list.add(new Category(Util.CATEGORY_GRABSTAETTE, R.drawable.ic_map_grabstaette));
        list.add(new Category(Util.CATEGORY_GRUENDUNGSMYTHOS, R.drawable.ic_map_grundungsmythos));
        list.add(new Category(Util.CATEGORY_INFRASTRUKTUR, R.drawable.ic_map_infrastruktur));
        list.add(new Category(Util.CATEGORY_KULTSTAETTE, R.drawable.ic_map_kultstaette));
        list.add(new Category(Util.CATEGORY_PLATZANLAGE, R.drawable.ic_map_platzanlage));
        list.add(new Category(Util.CATEGORY_POLITISCHE_INSTITUTION, R.drawable.ic_map_politische_institution));
        list.add(new Category(Util.CATEGORY_SPIELSTAETTE, R.drawable.ic_map_spielstaette));
        list.add(new Category(Util.CATEGORY_THERME, R.drawable.ic_map_therme));
        list.add(new Category(Util.CATEGORY_WOHNKOMPLEX, R.drawable.ic_map_wohnkomplex));

        return list;
    }

    private void initArtefactList(View view) {

        mFormViewArtefactList = view.findViewById(R.id.login_form);
        mProgressViewArtefactList = view.findViewById(R.id.login_progress);
        tvLoadArtefactList = view.findViewById(R.id.tvLoad);

        mRecyclerView = view.findViewById(R.id.recyclerview_list_of_artefacts);
        mLayoutManager = new LinearLayoutManager(artefactActivity);

        filterLayout = view.findViewById(R.id.layout_filter);
        spinnerFilterCategory = filterLayout.findViewById(R.id.spinner_list_filter_category);
        btnFilterApply = filterLayout.findViewById(R.id.button_artefact_list_filter_submit);
        etFilter = filterLayout.findViewById(R.id.edit_list_filter);
        filteredList = new ArrayList<>();

        artefactsList = ApplicationClass.mArtefactList;
        mCategoryList = populateCategoryList();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        artefactActivity = (ArtefactActivity) getActivity();
        artefactActivity.setTitle("Civitas");
        artefactActivity.currentScreen = UserScreen.ARTEFACT_LIST;
        artefactActivity.isAtListFragment = true;

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mFormViewArtefactList.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormViewArtefactList.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormViewArtefactList.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressViewArtefactList.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressViewArtefactList.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressViewArtefactList.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoadArtefactList.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoadArtefactList.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoadArtefactList.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressViewArtefactList.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoadArtefactList.setVisibility(show ? View.VISIBLE : View.GONE);
            mFormViewArtefactList.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
