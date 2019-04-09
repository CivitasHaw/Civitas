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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tim.romaniitedomum.ApplicationClass;
import com.example.tim.romaniitedomum.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

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
    private Switch filterSwitch;
    private RelativeLayout filterLayout;
    private EditText etFilter;
    private Button btnFilterApply;
    private String filterString = "";
    private ArrayList<Artefact> filteredList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artefact_list, container, false);

        initArtefactList(view);

        mAdapter = new ArtefactListAdapter(ApplicationClass.mArtefactList, ApplicationClass.loader);
        mRecyclerView.setLayoutManager(new GridLayoutManager(artefactActivity, SPAN_COUNT));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ArtefactListAdapter.OnItemClickListener() {
            @Override
            public void onItemclick(int position) {
                Toast.makeText(artefactActivity, "Click: " + ApplicationClass.mArtefactList.get(position).getArtefactName(),
                        Toast.LENGTH_SHORT).show();

                ApplicationClass.position = position;

                Fragment artefactDetailFragment = new ArtefactDetailFragment();
                artefactActivity.fragmentSwitcher2(artefactDetailFragment, true, "artefactDetailFragment");
            }
        });

        filterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    filterLayout.setVisibility(View.VISIBLE);
                } else {
                    filterLayout.setVisibility(View.GONE);
                }
            }
        });

/*        btnFilterApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterString = etFilter.getText().toString().trim();
                mAdapter.getFilter().filter(filterString);
                mAdapter.notifyDataSetChanged();
            }
        });*/
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


    private void initArtefactList(View view) {

        mFormViewArtefactList = view.findViewById(R.id.login_form);
        mProgressViewArtefactList = view.findViewById(R.id.login_progress);
        tvLoadArtefactList = view.findViewById(R.id.tvLoad);

        mRecyclerView = view.findViewById(R.id.recyclerview_list_of_artefacts);
        mLayoutManager = new LinearLayoutManager(artefactActivity);

        filterSwitch = view.findViewById(R.id.switch_filter);
        filterLayout = view.findViewById(R.id.layout_filter);
        btnFilterApply = filterLayout.findViewById(R.id.button_artefact_list_filter_submit);
        etFilter = filterLayout.findViewById(R.id.edit_list_filter);
        filteredList = new ArrayList<>();

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artefactActivity = (ArtefactActivity) getActivity();
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
