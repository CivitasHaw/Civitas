package com.example.tim.romaniitedomum.artefact;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tim.romaniitedomum.ApplicationClass;
import com.example.tim.romaniitedomum.R;
import com.example.tim.romaniitedomum.Util.UserScreen;
import com.example.tim.romaniitedomum.Util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;


/**
 * Created by TimStaats 27.04.2019
 */

public class EditArtefactFragment extends Fragment {

    private static final String TAG = "EditArtefactFragment";

    public static final int REQUEST_PERMISSION_CODE_CAMERA = 2000;
    public static final String ORIGIN_CAMERA = "camera";
    private ArtefactActivity artefactActivity;

    private View mProgressViewEditArtefact;
    private View mFormViewEditArtefact;
    private TextView tvLoadEditArtefact;

    private ImageButton btnGallery, btnCamera;
    private ImageButton btnAudioPlay, btnAudioStop, btnAudioRecord, btnAudioDelete;
    private ImageView ivArtefact;
    private EditText etArtefactName, etArtefactDescription, etArtefactAge;
    private Button btnAudio, btnEditArtefact;
    private Spinner spinnerCategory;
    private CategoryAdapter mCategoryAdapter;
    private ArrayList<Category> mCategoryList;
    private Category mCategory;
    private int categoryPosition;
    private String clickedCategory;
    private Bitmap artefactBitmap;

    private Artefact mArtefact;
    private LinearLayout layoutAudio;

    private Bundle mArgs;
    private ImageLoader mLoader;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_artefact, container, false);
        Log.d(TAG, "onCreateView: is called");

        initEditArtefact(view);

        for (int i = 0; i < mCategoryList.size(); i++) {
            if (mCategoryList.get(i).getCategoryName().equals(mArtefact.getCategoryName())){
                categoryPosition = i;
            }
        }
        spinnerCategory.setSelection(categoryPosition);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCategory = (Category) parent.getItemAtPosition(position);
                clickedCategory = mCategory.getCategoryName();
                Log.d(TAG, "onItemSelected: category: " + clickedCategory);
                //Toast.makeText(getContext(), "category: " + clickedCategory, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: btnCamera");
                takePhotoWithCamera();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: btnGallery");
            }
        });

        btnEditArtefact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save new imageFile
                // save new audioFile
                // update artefact backendless
                // delete old imageFile
                // delete old audioFile
                // replace ApplicationClass.mArtefact
            }
        });

        return view;
    }

    private void takePhotoWithCamera() {
        artefactActivity.isEditMode = true;
        artefactActivity.isCamera = true;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_PERMISSION_CODE_CAMERA);
    }
    private void initEditArtefact(View view) {
        tvLoadEditArtefact = view.findViewById(R.id.tvLoad_edit_artefact);
        mProgressViewEditArtefact = view.findViewById(R.id.progress_edit_artefact);
        mFormViewEditArtefact = view.findViewById(R.id.form_edit_artefact);
        btnGallery = view.findViewById(R.id.button_edit_artefact_image_from_gallery);
        btnCamera = view.findViewById(R.id.button_edit_artefact_image_from_camera);
        layoutAudio = view.findViewById(R.id.layout_edit_audio_player);
        btnAudio = view.findViewById(R.id.button_edit_artefact_audio);
        btnAudioPlay = layoutAudio.findViewById(R.id.button_edit_artefact_audio_play);
        btnAudioStop = layoutAudio.findViewById(R.id.button_edit_artefact_audio_stop);
        btnAudioRecord = layoutAudio.findViewById(R.id.button_edit_artefact_audio_record);
        btnAudioDelete = layoutAudio.findViewById(R.id.button_edit_artefact_audio_delete);
        ivArtefact = view.findViewById(R.id.image_edit_artefact);
        spinnerCategory = view.findViewById(R.id.spinner_edit_artefact_category);
        etArtefactName = view.findViewById(R.id.edit_edit_artefact_name);
        etArtefactDescription = view.findViewById(R.id.edit_edit_artefact_description);
        etArtefactAge = view.findViewById(R.id.edit_edit_artefact_age);
        btnEditArtefact = view.findViewById(R.id.button_edit_artefact_save);

        mCategoryList = populateCategoryList();
        mCategoryAdapter = new CategoryAdapter(artefactActivity, mCategoryList);
        spinnerCategory.setAdapter(mCategoryAdapter);
        mLoader = ApplicationClass.loader;

        etArtefactName.setText(mArtefact.getArtefactName());
        etArtefactDescription.setText(mArtefact.getArtefactDescription());
        etArtefactAge.setText(mArtefact.getArtefactAge());
        mLoader.displayImage(mArtefact.getArtefactImageUrl(), ivArtefact, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                showProgress(true);
                tvLoadEditArtefact.setText("Loading artefact... please wait...");
            }

        if (mArgs != null) {
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                showProgress(false);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                showProgress(false);
            switch (origin) {
                case ORIGIN_CAMERA:
                    Log.d(TAG, "initEditArtefact: origin: camera");
                    artefactBitmap = BitmapFactory.decodeByteArray(artefactImageBitmap.getByteArray(), 0, artefactImageBitmap.getByteArray().length);
                    ivArtefact.setImageBitmap(artefactBitmap);
                    break;
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                showProgress(false);
            }
        });
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artefactActivity = (ArtefactActivity) getActivity();
        artefactActivity.currentScreen = UserScreen.EDIT_ARTEFACT;
        mArgs = getArguments();
        mArtefact = ApplicationClass.mArtefact;
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

            mFormViewEditArtefact.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormViewEditArtefact.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormViewEditArtefact.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressViewEditArtefact.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressViewEditArtefact.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressViewEditArtefact.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoadEditArtefact.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoadEditArtefact.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoadEditArtefact.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressViewEditArtefact.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoadEditArtefact.setVisibility(show ? View.VISIBLE : View.GONE);
            mFormViewEditArtefact.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
