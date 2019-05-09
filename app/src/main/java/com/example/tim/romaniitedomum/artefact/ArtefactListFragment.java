package com.example.tim.romaniitedomum.artefact;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tim.romaniitedomum.ApplicationClass;
import com.example.tim.romaniitedomum.R;
import com.example.tim.romaniitedomum.Util.BcAc;
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
    private ConstraintLayout filterLayout;
    private EditText etFilterName, etFilterAge, etFilterAgeFrom;
    private Spinner spinnerFilterCategory;
    private Button btnFilterBeforeAfter, btnFilterBeforeAfterFrom;
    private CategoryAdapter mCategoryAdapter;
    private ArrayList<Category> mCategoryList;
    private Category mCategory;
    private boolean isFilterActive = false;

    private RadioGroup radioGroupFilter;
    private RadioButton radioButtonFilter;
    private Button btnFilterCategoryApply, btnFilterAgeApply, btnShowFilterResultOnMap;
    private String filterString = "";
    private List<Artefact> artefactsList;
    private List<Artefact> filteredList;

    private BcAc annoDomini;
    private BcAc annoDominiFrom;


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artefact_list, container, false);

        initArtefactList(view);

        radioGroupFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_artefact_name:
                        // Toast.makeText(artefactActivity, "checked button: " + checkedId, Toast.LENGTH_SHORT).show();
                        // disable unchecked lines
                        spinnerFilterCategory.setEnabled(false);
                        btnFilterCategoryApply.setEnabled(false);
                        etFilterAge.setEnabled(false);
                        btnFilterBeforeAfter.setEnabled(false);
                        etFilterAgeFrom.setEnabled(false);
                        btnFilterBeforeAfterFrom.setEnabled(false);
                        btnFilterAgeApply.setEnabled(false);

                        // enable checked line
                        etFilterName.setEnabled(true);
                        break;
                    case R.id.radio_artefact_category:
                        // Toast.makeText(artefactActivity, "checked button: " + checkedId, Toast.LENGTH_SHORT).show();
                        // disable unchecked lines
                        etFilterName.setEnabled(false);
                        etFilterAge.setEnabled(false);
                        btnFilterBeforeAfter.setEnabled(false);
                        etFilterAgeFrom.setEnabled(false);
                        btnFilterBeforeAfterFrom.setEnabled(false);
                        btnFilterAgeApply.setEnabled(false);

                        // enable checked line
                        spinnerFilterCategory.setEnabled(true);
                        btnFilterCategoryApply.setEnabled(true);
                        break;
                    case R.id.radio_artefact_age:
                        // Toast.makeText(artefactActivity, "checked button: " + checkedId, Toast.LENGTH_SHORT).show();
                  
                        // disable unchecked lines
                        etFilterName.setEnabled(false);
                        spinnerFilterCategory.setEnabled(false);
                        btnFilterCategoryApply.setEnabled(false);

                        // enable checked line
                        etFilterAge.setEnabled(true);
                        btnFilterBeforeAfter.setEnabled(true);
                        etFilterAgeFrom.setEnabled(true);
                        btnFilterBeforeAfterFrom.setEnabled(true);
                        btnFilterAgeApply.setEnabled(true);
                        break;
                }
            }
        });

        btnFilterBeforeAfterFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (annoDominiFrom == BcAc.BEFORE_CHRIST) {
                    annoDominiFrom = BcAc.AFTER_CHRIST;
                    btnFilterBeforeAfterFrom.setText("A.C.");
                } else {
                    annoDominiFrom = BcAc.BEFORE_CHRIST;
                    btnFilterBeforeAfterFrom.setText("B.C.");
                }
                Log.d(TAG, "onClick: btnFilterBeforeAfterFrom click: " + annoDominiFrom);
            }
        });

        btnFilterBeforeAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (annoDomini == BcAc.BEFORE_CHRIST) {
                    annoDomini = BcAc.AFTER_CHRIST;
                    btnFilterBeforeAfter.setText("A.C.");
                } else {
                    annoDomini = BcAc.BEFORE_CHRIST;
                    btnFilterBeforeAfter.setText("B.C.");
                }
                Log.d(TAG, "onClick: btnFilterBeforeAfter click: " + annoDomini);
            }
        });


        btnFilterAgeApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etFilterAge.getText().toString().isEmpty() || etFilterAgeFrom.getText().toString().isEmpty()) {
                    Toast.makeText(artefactActivity, "Fill empty field", Toast.LENGTH_SHORT).show();
                } else {
                    if (annoDominiFrom.toString().equals(BcAc.AFTER_CHRIST) && annoDomini.toString().equals(BcAc.BEFORE_CHRIST)) {
                        Toast.makeText(artefactActivity, "wrong annoDomini setup", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onClick: wrong annoDomini constellation");
                    } else {
                        Log.d(TAG, "onClick: annoDomini is correct");
                        int ageFrom = Integer.parseInt(etFilterAgeFrom.getText().toString());
                        int age = Integer.parseInt(etFilterAge.getText().toString());
                        if (age >= 9999 || ageFrom >= 9999) {
                            Toast.makeText(artefactActivity, "wrong age", Toast.LENGTH_SHORT).show();
                            resetAgeFilterViews();
                        } else {
                            Log.d(TAG, "onClick: age is fine");
                            if (annoDominiFrom == BcAc.BEFORE_CHRIST && annoDomini == BcAc.AFTER_CHRIST) {
                                Log.d(TAG, "onClick: B.C. to A.C. years filter is correct");
                                Log.d(TAG, "onClick: from: " + ageFrom + " " + annoDominiFrom + " to: " + age + " " + annoDomini + "\n");
                                List<Artefact> tempList;
                                tempList = getFilteredList(ApplicationClass.mArtefactList, annoDominiFrom, ageFrom, false);
                                filteredList = getFilteredList(ApplicationClass.mArtefactList, annoDomini, age, false);
                                filteredList.addAll(tempList);
                                mAdapter = null;
                                mAdapter = new ArtefactListAdapter(filteredList);
                                mRecyclerView.setAdapter(mAdapter);
                                mAdapter.setOnItemClickListener(new ArtefactListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemclick(int position) {
                                    }
                                });
                                Log.d(TAG, "onClick: filteredList.size(): " + filteredList.size());

//                                for (int i = 0; i < filteredList.size(); i++) {
//                                    Log.d(TAG, "onClick: artefactname: " + filteredList.get(i).getArtefactName() + " age: " + filteredList.get(i).getArtefactAge() + " " + filteredList.get(i).getAnnoDomini() + "\n");
//                                }
                            } else if (annoDominiFrom == BcAc.AFTER_CHRIST && annoDomini == BcAc.AFTER_CHRIST) {
                                if (ageFrom > age) {
                                    Toast.makeText(artefactActivity, "incorrect filter", Toast.LENGTH_SHORT).show();
                                    resetAgeFilterViews();
                                } else {
                                    Log.d(TAG, "onClick: A.C. to A.C. years filter is correct");
                                    Log.d(TAG, "onClick: from: " + ageFrom + " " + annoDominiFrom + " to: " + age + " " + annoDomini + "\n");
                                    List<Artefact> tempList;
                                    tempList = getFilteredList(ApplicationClass.mArtefactList, annoDominiFrom, ageFrom, true);
                                    filteredList = getFilteredList(tempList, annoDomini, age, false);
                                    mAdapter = null;
                                    mAdapter = new ArtefactListAdapter(filteredList);
                                    mRecyclerView.setAdapter(mAdapter);
                                    mAdapter.setOnItemClickListener(new ArtefactListAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemclick(int position) {
                                        }
                                    });
                                    Log.d(TAG, "onClick: filteredList.size(): " + filteredList.size());

//                                    for (int i = 0; i < filteredList.size(); i++) {
//                                        Log.d(TAG, "onClick: artefactname: " + filteredList.get(i).getArtefactName() + " age: " + filteredList.get(i).getArtefactAge() + " " + filteredList.get(i).getAnnoDomini() + "\n");
//                                    }
                                }
                            } else if (annoDominiFrom == BcAc.BEFORE_CHRIST && annoDomini == BcAc.BEFORE_CHRIST) {
                                if (ageFrom < age) {
                                    Toast.makeText(artefactActivity, "B.C. to B.C. incorrect filter", Toast.LENGTH_SHORT).show();
                                    resetAgeFilterViews();

                                } else {
                                    Log.d(TAG, "onClick: B.C. to B.C. years filter is correct");
                                    Log.d(TAG, "onClick: from: " + ageFrom + " " + annoDominiFrom + " to: " + age + " " + annoDomini + "\n");
                                    List<Artefact> tempList;
                                    tempList = getFilteredList(ApplicationClass.mArtefactList, annoDominiFrom, ageFrom, false);
                                    filteredList = getFilteredList(tempList, annoDomini, age, true);
                                    mAdapter = null;
                                    mAdapter = new ArtefactListAdapter(filteredList);
                                    mRecyclerView.setAdapter(mAdapter);
                                    mAdapter.setOnItemClickListener(new ArtefactListAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemclick(int position) {
                                        }
                                    });
                                    Log.d(TAG, "onClick: filteredList.size(): " + filteredList.size());
//                                    for (int i = 0; i < filteredList.size(); i++) {
//                                        Log.d(TAG, "onClick: artefactname: " + filteredList.get(i).getArtefactName() + " age: " + filteredList.get(i).getArtefactAge() + " " + filteredList.get(i).getAnnoDomini() + "\n");
//                                    }
                                }
                            } else if (annoDominiFrom == BcAc.AFTER_CHRIST && annoDomini == BcAc.BEFORE_CHRIST) {
                                Log.d(TAG, "onClick: A.C. to B.C. geht nicht");
                                Log.d(TAG, "onClick: from: " + annoDominiFrom + " to: " + annoDomini);
                            }
                        }
                    }
                }
            }
        });

        btnShowFilterResultOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!filteredList.isEmpty()) {
                    ApplicationClass.mFilteredArtefactList = filteredList;
                    Intent intent = new Intent(artefactActivity, MapActivity.class);
                    intent.putExtra(Util.ORIGIN, Util.FILTER);
                    startActivity(intent);
                }
            }
        });

        mAdapter = new ArtefactListAdapter(artefactsList);
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

        btnFilterCategoryApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.isCategoryFilter = true;
                if (mCategory != null) {
                    mAdapter.getFilter().filter(mCategory.getCategoryName());
                }
            }
        });

        etFilterName.addTextChangedListener(new TextWatcher() {
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


    private void resetAgeFilterViews() {
        etFilterAge.setText("");
        etFilterAgeFrom.setText("");
        etFilterAge.setHint("Age");
        etFilterAgeFrom.setHint("Age");
    }

    private List<Artefact> getFilteredList (List<Artefact> fullList, BcAc filterAnnoDominiFrom, int age, boolean isGreaterThan) {

        filteredList.clear();
        List<Artefact> tempList = new ArrayList<>();
        if (isGreaterThan) {
            for (Artefact item : fullList) {
                if (item.getAnnoDomini().equals(filterAnnoDominiFrom.toString()) && Integer.parseInt(item.getArtefactAge()) >= age) {
                    tempList.add(item);
                }
            }
        } else {
            for (Artefact item : fullList) {
                if (item.getAnnoDomini().equals(filterAnnoDominiFrom.toString()) && Integer.parseInt(item.getArtefactAge()) <= age) {
                    tempList.add(item);
                }
            }
        }
        return tempList;
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
                    etFilterName.setText("");
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

    public void checkButton(View v){
        int radioId = radioGroupFilter.getCheckedRadioButtonId();

        radioButtonFilter = radioGroupFilter.findViewById(radioId);
        Toast.makeText(artefactActivity, "Radiobutton selected: " + radioButtonFilter.getText(), Toast.LENGTH_SHORT).show();
    }

    private void initArtefactList(View view) {

        mFormViewArtefactList = view.findViewById(R.id.login_form);
        mProgressViewArtefactList = view.findViewById(R.id.login_progress);
        tvLoadArtefactList = view.findViewById(R.id.tvLoad);

        mRecyclerView = view.findViewById(R.id.recyclerview_list_of_artefacts);
        mLayoutManager = new LinearLayoutManager(artefactActivity);

        filterLayout = view.findViewById(R.id.layout_filter);
        spinnerFilterCategory = filterLayout.findViewById(R.id.spinner_list_filter_category);
        btnFilterCategoryApply = filterLayout.findViewById(R.id.button_artefact_list_apply_category_filter);
        etFilterAge = filterLayout.findViewById(R.id.edit_artefact_age);
        etFilterAgeFrom = filterLayout.findViewById(R.id.edit_artefact_age_from);
        btnFilterBeforeAfter = filterLayout.findViewById(R.id.button_before_after);
        btnFilterBeforeAfterFrom = filterLayout.findViewById(R.id.button_before_after_from);
        btnFilterAgeApply = filterLayout.findViewById(R.id.button_artefact_list_apply_age_filter);
        btnShowFilterResultOnMap = filterLayout.findViewById(R.id.button_show_filter_on_map);
        radioGroupFilter = filterLayout.findViewById(R.id.radioGroup);
        etFilterName = filterLayout.findViewById(R.id.edit_list_filter);
        annoDomini = BcAc.AFTER_CHRIST;
        annoDominiFrom = BcAc.BEFORE_CHRIST;

        // disable unchecked lines
        spinnerFilterCategory.setEnabled(false);
        btnFilterCategoryApply.setEnabled(false);
        etFilterAge.setEnabled(false);
        btnFilterBeforeAfter.setEnabled(false);
        btnFilterBeforeAfter.setText("A.C.");
        etFilterAgeFrom.setEnabled(false);
        btnFilterBeforeAfterFrom.setEnabled(false);
        btnFilterBeforeAfterFrom.setText("B.C.");
        btnFilterAgeApply.setEnabled(false);
        // enable checked line
        etFilterName.setEnabled(true);

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
