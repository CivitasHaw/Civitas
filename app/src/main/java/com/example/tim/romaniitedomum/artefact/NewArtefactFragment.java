package com.example.tim.romaniitedomum.artefact;


import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.geo.GeoPoint;
import com.example.tim.romaniitedomum.ApplicationClass;
import com.example.tim.romaniitedomum.R;
import com.example.tim.romaniitedomum.map.MapActivity;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by TimStaats 03.03.2019
 */

public class NewArtefactFragment extends Fragment {

    private static final String TAG = "NewArtefactFragment";

    public static final String ORIGIN_CAMERA = "camera";
    public static final String ORIGIN_MAP_LONG_CLICK = "onMapLongClick";
    public static final String ORIGIN_BTN_ADD_ARTEFACT = "btnAddArtefact";
    public static final String BACKENDLESS_IMAGE_FILE_PATH = "artefactImages";

    public static final int BITMAP_QUALITY = 100;
    public static final int REQUEST_PERMISSION_CODE = 1000;

    private ArtefactActivity artefactActivity;

    private View mProgressViewNewArtefact;
    private View mFormViewNewArtefact;
    private TextView tvLoadNewArtefact;

    private ImageView ivNewArtefact;
    private EditText etNewArtefactName, etNewArtefactDescription, etNewArtefactDate;
    private ImageButton btnTakeFotoFromCamera, btnTakeFotoFromGallery;

    private LinearLayout audioLayout;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private boolean isAudioRecording = false, audioExists = false, isAudioPlaying = false;
    private ImageButton btnAudioRecord, btnAudioPlay, btnAudioStop, btnAudioDelete;
    private String audioPathSave = "";
    private String imageIsTakenFromCamera = "";

    private Spinner spinnerCategories;
    private CategoryAdapter mAdapter;
    private ArrayList<Category> mCategoryList;

    private String artefactName, artefactDescription, artefactDate, clickedCategory;
    private Bitmap artefactBitmap;
    private Artefact mArtefact;
    private Category mCategory;
    private double mLat;
    private double mLng;
    private Bundle mArgs;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_artefact, container, false);

        initNewArtefact(view);

        mAdapter = new CategoryAdapter(artefactActivity, mCategoryList);
        spinnerCategories.setAdapter(mAdapter);
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCategory = (Category) parent.getItemAtPosition(position);
                clickedCategory = mCategory.getCategoryName();
                Toast.makeText(getContext(), "category: " + clickedCategory, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerCategories.setVisibility(View.VISIBLE);
                btnAddCategory.setVisibility(View.GONE);
            }
        });

        ivNewArtefact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeFotoFromCamera();
            }
        });

        btnTakeFotoFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeFotoFromCamera();
            }
        });

        btnTakeFotoFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeFotoFromGallery();
            }
        });

        btnNewArtefactSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                artefactName = etNewArtefactName.getText().toString().trim();
                artefactDescription = etNewArtefactDescription.getText().toString().trim();
                artefactDate = etNewArtefactDate.getText().toString().trim();

                imageIsTakenFromCamera = mArgs.getString(getResources().getString(R.string.origin));

                if (artefactDate.isEmpty() || artefactDescription.isEmpty() || artefactName.isEmpty() ||
                        !imageIsTakenFromCamera.equals("camera") || clickedCategory.equals(""))  {
                    Log.d(TAG, "onClick: mArgs: " + imageIsTakenFromCamera);
                    Toast.makeText(getContext(), getResources().getText(R.string.toast_empty_fields), Toast.LENGTH_SHORT).show();
                } else {
                    showProgress(true);
                    tvLoadNewArtefact.setText(getResources().getText(R.string.new_artefact_save_image));

                    mArtefact = new Artefact();
                    mArtefact.setArtefactName(artefactName);
                    mArtefact.setArtefactDescription(artefactDescription);
                    mArtefact.setUserEmail(ApplicationClass.user.getEmail());
                    mArtefact.setOwnerId(ApplicationClass.user.getProperty("ownerId").toString());
                    mArtefact.setLatitude(mLat);
                    mArtefact.setLongitude(mLng);
                    //mArtefact.setCategory(new Category(mCategory.getCategoryName(), mCategory.getCategoryMarkerImage()));
                    mArtefact.setCategoryName(mCategory.getCategoryName());
                    mArtefact.setCategoryMarkerImage(mCategory.getCategoryMarkerImage());

                    String fileName = artefactName + ".png";

                    Backendless.Files.Android.upload(artefactBitmap, Bitmap.CompressFormat.PNG, BITMAP_QUALITY,
                            fileName, BACKENDLESS_FILE_PATH, new AsyncCallback<BackendlessFile>() {
                        @Override
                        public void handleResponse(BackendlessFile response) {

                            mArtefact.setArtefactImageUrl(response.getFileURL());
                            Toast.makeText(getContext(), getResources().getText(R.string.toast_backendless_image_upload), Toast.LENGTH_SHORT).show();
                            showProgress(false);

                            showProgress(true);
                            tvLoadNewArtefact.setText(getResources().getText(R.string.toast_backendless_create_new_artefact));
                            saveDataWithGeoAsync(mArtefact);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            showProgress(false);
                            Toast.makeText(getContext(), getResources().getText(R.string.toast_backendless_error) + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });



        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: audio menu");

                if (checkPermissionFromDevice()){
                    btnAudio.setVisibility(View.GONE);
                    audioLayout.setVisibility(View.VISIBLE);
                    if (!isAudioRecording && !audioExists){
                        btnAudioDelete.setEnabled(false);
                        btnAudioDelete.setBackground(getResources().getDrawable(R.drawable.buttons_pressed));
                    }
                } else {
                    requestPermission();
                }


            }
        });

        btnAudioRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: recording");
                if (!isAudioRecording){
                    isAudioRecording = true;
                    btnAudioPlay.setBackground(getResources().getDrawable(R.drawable.buttons_pressed));
                    btnAudioRecord.setImageDrawable(getResources().getDrawable(R.drawable.ic_audio_record_red));
                    btnAudioRecord.setBackground(getResources().getDrawable(R.drawable.buttons_pressed));
                    audioPathSave = Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/" + UUID.randomUUID().toString() + "_audio_record.3gp";
                    setupMediaRecorder();
                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        btnAudioStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: stop recording");
                if (isAudioRecording){
                    mediaRecorder.stop();
                    isAudioRecording = false;
                    audioExists = true;
                    btnAudioDelete.setEnabled(true);
                    btnAudioPlay.setBackground(getResources().getDrawable(R.drawable.buttons));
                    btnAudioDelete.setBackground(getResources().getDrawable(R.drawable.buttons));
                    btnAudioRecord.setImageDrawable(getResources().getDrawable(R.drawable.ic_audio_record));
                    btnAudioRecord.setBackground(getResources().getDrawable(R.drawable.buttons));
                } else if (isAudioPlaying){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    setupMediaRecorder();
                    isAudioPlaying = false;
                    btnAudioPlay.setBackground(getResources().getDrawable(R.drawable.buttons));
                }
            }
        });

        btnAudioDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (audioExists){
                    Log.d(TAG, "onClick: delete audio");
                    audioExists = false;
                    isAudioPlaying = false;
                    isAudioRecording = false;
                    audioLayout.setVisibility(View.GONE);
                    btnAudio.setVisibility(View.VISIBLE);
                }
            }
        });

        btnAudioPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (audioExists){
                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(audioPathSave);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mediaPlayer.start();
                    Toast.makeText(getContext(), "Playing...", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick: play");
                    isAudioPlaying = true;
                    btnAudioPlay.setBackground(getResources().getDrawable(R.drawable.buttons_pressed));
                }
            }
        });


        return view;
    }

    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(audioPathSave);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_PERMISSION_CODE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: permission denied");
                }
            }
        }
    }

    private boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO);

        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }

    private void saveDataWithGeoAsync(final Artefact artefact) {

        GeoPoint location = new GeoPoint(mLat, mLng);
        Log.d(TAG, "saveDataWithGeoAsync: mLat: " + mLat);
        //location.addCategory(artefact.getCategory().getCategoryName());
        location.addCategory(artefact.getCategoryName());
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
                ApplicationClass.mArtefactList.add(artefact);

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

    private void takeFotoFromGallery(){
        Toast.makeText(artefactActivity, "TODO: implement Gallery picker", Toast.LENGTH_SHORT).show();
    }

    private void takeFotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artefactActivity = (ArtefactActivity) getActivity();
        mArgs = getArguments();

    }

    private ArrayList<Category> populateCategoryList(){
        ArrayList<Category> list = new ArrayList<>();

        list.add(new Category("Akropolis", R.drawable.ic_map_akropolis));
        list.add(new Category("Blur", R.drawable.ic_blur));
        list.add(new Category("Basilika", R.drawable.ic_map_basilica));
        list.add(new Category("Bogen", R.drawable.ic_map_bogen));
        list.add(new Category("Christentum", R.drawable.ic_map_christentum));
        list.add(new Category("Grabstaette", R.drawable.ic_map_grabstaette));
        list.add(new Category("Grundungsmythos", R.drawable.ic_map_grundungsmythos));

        return list;
    }

    private void initNewArtefact(View view) {
        artefactActivity.hideSoftKeyboard();
        mProgressViewNewArtefact = view.findViewById(R.id.progress_new_artefact);
        mFormViewNewArtefact = view.findViewById(R.id.form_new_artefact);
        tvLoadNewArtefact = view.findViewById(R.id.tvLoad_new_artefact);

        ivNewArtefact = view.findViewById(R.id.image_new_artefact);
        etNewArtefactDate = view.findViewById(R.id.edit_new_artefact_date);
        etNewArtefactName = view.findViewById(R.id.edit_new_artefact_name);
        etNewArtefactDescription = view.findViewById(R.id.edit_new_artefact_description);
        btnNewArtefactSave = view.findViewById(R.id.button_new_artefact_save);
        btnAudio = view.findViewById(R.id.button_new_artefact_audio);
        btnTakeImage = view.findViewById(R.id.button_new_artefact_image);
        btnAddCategory = view.findViewById(R.id.button_new_artefact_add_category);
        spinnerCategories = view.findViewById(R.id.spinner_new_artefact_category);

        mCategoryList = populateCategoryList();

        audioLayout = view.findViewById(R.id.layout_audio_player);
        btnAudioRecord = audioLayout.findViewById(R.id.button_new_artefact_audio_record);
        btnAudioPlay = audioLayout.findViewById(R.id.button_new_artefact_audio_play);
        btnAudioStop = audioLayout.findViewById(R.id.button_new_artefact_audio_stop);
        btnAudioDelete = audioLayout.findViewById(R.id.button_new_artefact_audio_delete);
        audioLayout.setVisibility(View.GONE);

        //mArgs = getArguments();
        if (mArgs != null) {
            String origin = mArgs.getString(getResources().getString(R.string.origin));
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
