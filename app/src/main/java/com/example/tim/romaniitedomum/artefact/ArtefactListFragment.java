package com.example.tim.romaniitedomum.artefact;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tim.romaniitedomum.R;

/**
 * Created by TimStaats 03.03.2019
 */

public class ArtefactListFragment extends Fragment {

    private static final String TAG = "ArtefactListFragment";

    private ArtefactActivity artefactActivity;
    private View mProgressViewArtefactList;
    private View mFormViewArtefactList;
    private TextView tvLoadArtefactList;

    private TextView tvListOfArtefacts;

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artefact_list, container, false);

        initArtefactList(view);

        return view;
    }


    private void initArtefactList(View view) {

        mFormViewArtefactList = view.findViewById(R.id.login_form);
        mProgressViewArtefactList = view.findViewById(R.id.login_progress);
        tvLoadArtefactList = view.findViewById(R.id.tvLoad);

        tvListOfArtefacts = view.findViewById(R.id.text_list_of_artefacts);

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
