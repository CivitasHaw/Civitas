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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.GeoPoint;
import com.example.tim.romaniitedomum.ApplicationClass;
import com.example.tim.romaniitedomum.Artefact;
import com.example.tim.romaniitedomum.R;
import com.example.tim.romaniitedomum.map.MapActivity;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by TimStaats 03.03.2019
 */

public class NewArtefactFragment extends Fragment {

    private static final String TAG = "NewArtefactFragment";

    public static final String ORIGIN_CAMERA = "camera";
    public static final String ORIGIN_MAP_LONG_CLICK = "onMapLongClick";
    public static final String ORIGIN_BTN_ADD_ARTEFACT = "btnAddArtefact";
    private ArtefactActivity artefactActivity;

    private View mProgressViewNewArtefact;
    private View mFormViewNewArtefact;
    private TextView tvLoadNewArtefact;

    private ImageView ivNewArtefact;
    private EditText etNewArtefactName, etNewArtefactDescription, etNewArtefactDate;
    private Button btnNewArtefactSave, btnTakeImage, btnAudioRecord;

    private String artefactName, artefactDescription, artefactDate;
    private Bitmap artefactBitmap;
    private Artefact mArtefact;
    private double mLat;
    private double mLng;
    private Bundle mArgs;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_artefact, container, false);

        initNewArtefact(view);

        ivNewArtefact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeAPictureWithCamera();
            }
        });

        btnTakeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeAPictureWithCamera();
            }
        });

        btnNewArtefactSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                artefactName = etNewArtefactName.getText().toString().trim();
                artefactDescription = etNewArtefactDescription.getText().toString().trim();
                artefactDate = etNewArtefactDate.getText().toString().trim();

                String imageIsTakenFromCamera = mArgs.getString("origin");

                if (artefactDate.isEmpty() || artefactDescription.isEmpty() || artefactName.isEmpty() || !imageIsTakenFromCamera.equals("camera")) {
                    Log.d(TAG, "onClick: mArgs: " + imageIsTakenFromCamera);
                    Toast.makeText(getContext(), getResources().getText(R.string.toast_empty_fields), Toast.LENGTH_SHORT).show();
                } else {
                    showProgress(true);
                    tvLoadNewArtefact.setText(getResources().getText(R.string.new_artefact_save_image));

                    Artefact artefact = new Artefact();
                    artefact.setArtefactName(artefactName);
                    artefact.setArtefactDescription(artefactDescription);
                    artefact.setUserEmail(ApplicationClass.user.getEmail());
                    artefact.setLatitude(mLat);
                    artefact.setLongitude(mLng);


                    saveDataWithGeoAsync(artefact);


                }
            }
        });


        return view;
    }

    private void saveDataWithGeoAsync(final Artefact artefact) {

        GeoPoint location = new GeoPoint(mLat, mLng);
        Log.d(TAG, "saveDataWithGeoAsync: mLat: " + mLat);
        location.addCategory("Basilika");
        location.addCategory("Roma");
        location.addMetadata("artefactName", artefact.getArtefactName());
        location.addMetadata("artefactDescription", artefact.getArtefactDescription());
        location.addMetadata("artefactCreator", artefact.getUserEmail());
        location.addMetadata("artefact", artefact);
        artefact.setLocation(location);

        Backendless.Geo.savePoint(location, new AsyncCallback<GeoPoint>() {
            @Override
            public void handleResponse(GeoPoint response) {
                Log.d(TAG, "handleResponse: GeoPoint has been saved: " + response.getObjectId());
                showProgress(false);
                ApplicationClass.mArtefact = artefact;

                etNewArtefactName.setText("");
                etNewArtefactDescription.setText("");
                etNewArtefactDate.setText("");
                startActivity(new Intent(getContext(), MapActivity.class));

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(getContext(), "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);

            }
        });
    }

    private void takeAPictureWithCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artefactActivity = (ArtefactActivity) getActivity();
        mArgs = getArguments();

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
        btnAudioRecord = view.findViewById(R.id.button_new_artefact_audio_record);
        btnTakeImage = view.findViewById(R.id.button_new_artefact_image);


        //mArgs = getArguments();
        if (mArgs != null) {
            String origin = mArgs.getString("origin");
            LatLng tempLatLng;

            switch (origin) {
                case ORIGIN_CAMERA: // artefact image is taken with camera
                    Log.d(TAG, "initNewArtefact: origin: " + origin);
                    byte[] byteArray = mArgs.getByteArray("image");
                    artefactBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    ivNewArtefact.setImageBitmap(artefactBitmap);
                    mLat = ApplicationClass.mTempArtefactLatLng.latitude;
                    mLng = ApplicationClass.mTempArtefactLatLng.longitude;
                    break;
                case ORIGIN_BTN_ADD_ARTEFACT: // artefact gets created at device position
                    Log.d(TAG, "initNewArtefact: origin: " + origin);
                    mLat = ApplicationClass.mDeviceLocation.getLatitude();
                    mLng = ApplicationClass.mDeviceLocation.getLongitude();
                    tempLatLng = new LatLng(mLat, mLng);
                    ApplicationClass.mTempArtefactLatLng = tempLatLng;
                    Log.d(TAG, "initNewArtefact: btnAddArtefact: mLat: " + mLat);
                    break;
                case ORIGIN_MAP_LONG_CLICK: // artefact gets created at marker position
                    Log.d(TAG, "initNewArtefact: origin: " + origin);
                    mLat = ApplicationClass.mArtefactLatLng.latitude;
                    mLng = ApplicationClass.mArtefactLatLng.longitude;
                    tempLatLng = new LatLng(mLat, mLng);
                    ApplicationClass.mTempArtefactLatLng = tempLatLng;

                    Log.d(TAG, "initNewArtefact: onMapLongClick: mLat: " + mLat);
                    break;
            }
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
