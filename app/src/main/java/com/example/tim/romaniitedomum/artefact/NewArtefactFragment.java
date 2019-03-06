package com.example.tim.romaniitedomum.artefact;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tim.romaniitedomum.R;

/**
 * Created by TimStaats 03.03.2019
 */

public class NewArtefactFragment extends Fragment {

    private static final String TAG = "NewArtefactFragment";

    private View mProgressViewNewArtefact;
    private View mFormViewNewArtefact;
    private TextView tvLoadNewArtefact;

    private ImageView ivNewArtefact;
    private EditText etNewArtefactName, etNewArtefactDescription, etNewArtefactDate;
    private Button btnNewArtefactSave;

    private String artefactName, artefactDescription, artefactDate;
    private Bitmap artefactBitmap;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_artefact, container, false);

        initNewArtefact(view);

        ivNewArtefact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        btnNewArtefactSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                artefactName = etNewArtefactName.getText().toString().trim();
                artefactDescription = etNewArtefactDescription.getText().toString().trim();
                artefactDate = etNewArtefactDate.getText().toString().trim();

                if (artefactDate.isEmpty() || artefactDescription.isEmpty() || artefactName.isEmpty()){
                    Toast.makeText(getContext(), getResources().getText(R.string.toast_empty_fields), Toast.LENGTH_SHORT).show();
                } else {
                    showProgress(true);
                    tvLoadNewArtefact.setText("Saving image... please wait...");

                }
            }
        });


        return view;
    }


    private void initNewArtefact(View view) {
        mProgressViewNewArtefact = view.findViewById(R.id.progress_new_artefact);
        mFormViewNewArtefact = view.findViewById(R.id.form_new_artefact);
        tvLoadNewArtefact = view.findViewById(R.id.tvLoad_new_artefact);

        ivNewArtefact = view.findViewById(R.id.image_new_artefact);
        etNewArtefactDate = view.findViewById(R.id.edit_new_artefact_date);
        etNewArtefactName = view.findViewById(R.id.edit_new_artefact_name);
        etNewArtefactDescription = view.findViewById(R.id.edit_new_artefact_description);
        btnNewArtefactSave = view.findViewById(R.id.button_new_artefact_save);

        if (getArguments() != null){
            byte[] byteArray = getArguments().getByteArray("image");
            artefactBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            ivNewArtefact.setImageBitmap(artefactBitmap);
        }
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

            mFormViewNewArtefact.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormViewNewArtefact.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormViewNewArtefact.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressViewNewArtefact.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressViewNewArtefact.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressViewNewArtefact.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoadNewArtefact.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoadNewArtefact.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoadNewArtefact.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressViewNewArtefact.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoadNewArtefact.setVisibility(show ? View.VISIBLE : View.GONE);
            mFormViewNewArtefact.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}
