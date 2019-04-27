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
import com.example.tim.romaniitedomum.Util.ArtefactImageBitmap;
import com.example.tim.romaniitedomum.Util.UserScreen;
import com.example.tim.romaniitedomum.Util.Util;
import com.example.tim.romaniitedomum.map.MapActivity;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by TimStaats 03.03.2019
 */

public class NewArtefactFragment extends Fragment {

    private static final String TAG = "NewArtefactFragment";

    public static final String ORIGIN_CAMERA = "camera";
    public static final String ORIGIN_GALLERY = "gallery";
    public static final String ORIGIN_MAP_LONG_CLICK = "onMapLongClick";
    public static final String ORIGIN_BTN_ADD_ARTEFACT = "btnAddArtefact";
    public static final String BACKENDLESS_IMAGE_FILE_PATH = "artefactImages";
    public static final String BACKENDLESS_AUDIO_FILE_PATH = "/artefactAudios";

    public static final int BITMAP_QUALITY = 100;
    public static final int REQUEST_PERMISSION_CODE_AUDIO = 1000;
    public static final int REQUEST_PERMISSION_CODE_CAMERA = 2000;
    public static final int REQUEST_PERMISSION_CODE_GALLERY = 3000;
    public static final int PICK_IMAGE = 1;

    private ArtefactActivity artefactActivity;

    private View mProgressViewNewArtefact;
    private View mFormViewNewArtefact;
    private TextView tvLoadNewArtefact;

    private ImageView ivNewArtefact;
    private EditText etNewArtefactName, etNewArtefactDescription, etNewArtefactDate;
    private Button btnNewArtefactSave, btnAddCategory, btnAudio;
    private ImageButton btnTakeFotoFromCamera, btnTakeFotoFromGallery;

    private LinearLayout audioLayout;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private boolean isAudioRecording = false, audioExists = false, isAudioPlaying = false;
    private ImageButton btnAudioRecord, btnAudioPlay, btnAudioStop, btnAudioDelete;
    private String audioPathSave = "";
    private File mAudioFile;
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

    String imageFileName = "";
    String audioFileName = "";

    private boolean isImageSelected = false;

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
                Log.d(TAG, "onItemSelected: category: " + clickedCategory);
                //Toast.makeText(getContext(), "category: " + clickedCategory, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: audio menu");

                if (checkPermissionFromDevice()) {
                    btnAudio.setVisibility(View.GONE);
                    audioLayout.setVisibility(View.VISIBLE);
                    if (!isAudioRecording && !audioExists) {
                        btnAudioDelete.setEnabled(false);
                        btnAudioDelete.setBackground(getResources().getDrawable(R.drawable.buttons_pressed));
                    }
                } else {
                    requestPermissionAudio();
                }


            }
        });

        btnAudioRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: recording");
                if (!isAudioRecording) {
                    isAudioRecording = true;
                    btnAudioPlay.setBackground(getResources().getDrawable(R.drawable.buttons_pressed));
                    btnAudioRecord.setImageDrawable(getResources().getDrawable(R.drawable.ic_audio_record_red));
                    btnAudioRecord.setBackground(getResources().getDrawable(R.drawable.buttons_pressed));
//                    audioPathSave = Environment.getExternalStorageDirectory()
//                            .getAbsolutePath() + "/" + UUID.randomUUID().toString() + "_audio_record.3gp";
                    mediaRecorder = new MediaRecorder();
                    setupMediaRecorder();
                    mediaRecorder.start();
                }
            }
        });
        btnAudioStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: stop recording");
                if (isAudioRecording) {
                    mediaRecorder.stop();
                    isAudioRecording = false;
                    audioExists = true;
                    btnAudioDelete.setEnabled(true);
                    btnAudioPlay.setBackground(getResources().getDrawable(R.drawable.buttons));
                    btnAudioDelete.setBackground(getResources().getDrawable(R.drawable.buttons));
                    btnAudioRecord.setImageDrawable(getResources().getDrawable(R.drawable.ic_audio_record));
                    btnAudioRecord.setBackground(getResources().getDrawable(R.drawable.buttons));
                } else if (isAudioPlaying) {
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
                if (audioExists) {
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
                if (audioExists) {
                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(mAudioFile.getAbsolutePath());
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


        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerCategories.setVisibility(View.VISIBLE);
                btnAddCategory.setVisibility(View.GONE);
            }
        });

        // TODO: delete ivNewArtefact clickable
        ivNewArtefact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //takePhotoFromCamera();
            }
        });

        btnTakeFotoFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoFromCamera();
            }
        });

        btnTakeFotoFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery();
            }
        });


        btnNewArtefactSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                artefactName = etNewArtefactName.getText().toString().trim();
                artefactDescription = etNewArtefactDescription.getText().toString().trim();
                artefactDate = etNewArtefactDate.getText().toString().trim();

                imageIsTakenFromCamera = mArgs.getString(getResources().getString(R.string.origin));
                if (!imageIsTakenFromCamera.isEmpty()) {
                    isImageSelected = true;
                }

                if (checkFields()) {
                    //Log.d(TAG, "onClick: mArgs: " + imageIsTakenFromCamera);
                    Toast.makeText(getContext(), getResources().getText(R.string.toast_empty_fields), Toast.LENGTH_SHORT).show();
                } else {

                    // setup artefact content for later usage
                    mArtefact = new Artefact();
                    mArtefact.setArtefactName(artefactName);
                    mArtefact.setArtefactDescription(artefactDescription);
                    mArtefact.setUserEmail(ApplicationClass.user.getEmail());
                    mArtefact.setAuthorName(ApplicationClass.user.getProperty(getResources().getString(R.string.backendless_property_name)).toString());
                    mArtefact.setOwnerId(ApplicationClass.user.getProperty(getResources().getString(R.string.backendless_property_ownerid)).toString());
                    mArtefact.setLatitude(mLat);
                    mArtefact.setLongitude(mLng);
                    mArtefact.setLocation(new GeoPoint(mLat, mLng));
                    //mArtefact.setCategory(new Category(mCategory.getCategoryName(), mCategory.getCategoryMarkerImage()));
                    mArtefact.setCategoryName(mCategory.getCategoryName());
                    mArtefact.setCategoryMarkerImage(mCategory.getCategoryMarkerImage());

                    Date date = new Date();
                    Timestamp timestamp = new Timestamp(date.getTime());
                    //String imageFileName = artefactName + "_" + artefactDescription + ".png";
                    imageFileName = "artefactImage_" + artefactName + "_" + timestamp + ".png";
                    audioFileName = "artefactAudio_" + artefactName + "_" + timestamp + ".3gp";

                    mArtefact.setArtefactImageFileName(imageFileName);
                    mArtefact.setArtefactAudioFileName(audioFileName);

                    uploadImageToBackendless(artefactBitmap, imageFileName);
                }


            }
        });

        return view;
    }

            mArtefact.setArtefactAge(artefactDate);
    private void uploadImageToBackendless(Bitmap artefactBitmap, String fileName) {
        showProgress(true);
        tvLoadNewArtefact.setText(getResources().getText(R.string.new_artefact_save_image));

        Backendless.Files.Android.upload(artefactBitmap, Bitmap.CompressFormat.PNG, BITMAP_QUALITY,
                fileName, BACKENDLESS_IMAGE_FILE_PATH, new AsyncCallback<BackendlessFile>() {
            @Override
            public void handleResponse(BackendlessFile response) {
                mArtefact.setArtefactImageUrl(response.getFileURL());
                Toast.makeText(getContext(), getResources().getText(R.string.toast_backendless_image_upload), Toast.LENGTH_SHORT).show();
                //showProgress(false);

                if (!audioExists) {
                    saveGeoPointToBackendless(mLat, mLng);
                } else {
                    uploadAudioToBackendless(mAudioFile);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                mArtefact.setArtefactAudioUrl("");
                showProgress(false);
                Toast.makeText(getContext(), getResources().getText(R.string.toast_backendless_error) + fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadAudioToBackendless(File audioFile) {
        showProgress(true);
        tvLoadNewArtefact.setText(getResources().getString(R.string.new_artefact_save_audio));
        // ---------------------- Audio Upload ----------------------
        // https://backendless.com/feature-31-uploading-files-to-server-with-the-file-upload-api/
        Backendless.Files.upload(audioFile, BACKENDLESS_AUDIO_FILE_PATH, new AsyncCallback<BackendlessFile>() {
            @Override
            public void handleResponse(BackendlessFile response) {

                //Toast.makeText(getContext(), "Audio upload successful", Toast.LENGTH_SHORT).show();
                mArtefact.setArtefactAudioUrl(response.getFileURL());
                //showProgress(false);
                saveGeoPointToBackendless(mLat, mLng);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                showProgress(false);
                Toast.makeText(getContext(), getResources().getText(R.string.toast_backendless_error) + fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveArtefactToBackendless() {
        showProgress(true);
        tvLoadNewArtefact.setText(getResources().getString(R.string.new_artefact_create_artefact));
        Backendless.Persistence.save(mArtefact, new AsyncCallback<Artefact>() {
            @Override
            public void handleResponse(Artefact response) {
                showProgress(false);
                ApplicationClass.mArtefact = response;
                ApplicationClass.mArtefactList.add(response);

                etNewArtefactName.setText("");
                etNewArtefactDescription.setText("");
                etNewArtefactDate.setText("");
                Intent intent = new Intent(artefactActivity, MapActivity.class);
                intent.putExtra(getResources().getString(R.string.origin), TAG);
                startActivity(intent);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                showProgress(false);
                Toast.makeText(getContext(), getResources().getText(R.string.toast_backendless_error) + fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveGeoPointToBackendless(double mLat, double mLng) {
        GeoPoint artefactLocation = new GeoPoint(mLat, mLng);
        artefactLocation.addCategory(mArtefact.getCategoryName());
        artefactLocation.addMetadata("artefactName", mArtefact.getArtefactName());
        artefactLocation.addMetadata("artefactDescription", mArtefact.getArtefactName());
        artefactLocation.addMetadata("artefactAuthor", mArtefact.getAuthorName());
        artefactLocation.addMetadata("artefactUserEmail", mArtefact.getUserEmail());
        //artefactLocation.addMetadata("artefact", mArtefact);

        showProgress(true);
        tvLoadNewArtefact.setText(getResources().getString(R.string.new_artefact_add_marker));
        Backendless.Geo.savePoint(artefactLocation, new AsyncCallback<GeoPoint>() {
            @Override
            public void handleResponse(GeoPoint response) {
                //showProgress(false);
                //mArtefact.setLocation(response);
                mArtefact.setArtefactLocationObjectId(response.getObjectId());
                Log.d(TAG, "handleResponse: onClick: geoPoint.objectId: " + response.getObjectId());
                saveArtefactToBackendless();

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                showProgress(false);
                Toast.makeText(getContext(), getResources().getText(R.string.toast_backendless_error) + fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean checkFields() {
        if (artefactDate.isEmpty() || artefactDescription.isEmpty() || artefactName.isEmpty() || !isImageSelected
                /*!imageIsTakenFromCamera.equals("camera")*/ || clickedCategory.equals("")) {
            return true;
        }
        return false;
    }

    // https://stackoverflow.com/questions/37338606/mediarecorder-not-saving-audio-to-file
    private void setupMediaRecorder() {
        /*
        Random r = new Random();
        int x = r.nextInt(10000);

        mAudioFile = new File(Environment.getExternalStorageDirectory(), artefactName + "_" + artefactDescription + "_" + x + "_audio.3gp");
        */
        mAudioFile = new File(Environment.getExternalStorageDirectory(), audioFileName);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setAudioEncodingBitRate(16);
        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setOutputFile(mAudioFile.getAbsolutePath());

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Toast.makeText(artefactActivity, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            Toast.makeText(artefactActivity, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void requestPermissionAudio() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                REQUEST_PERMISSION_CODE_AUDIO);
    }

    private void requestPermissionCamera() {
        requestPermissions(new String[]{
                        Manifest.permission.CAMERA},
                REQUEST_PERMISSION_CODE_CAMERA);
    }

    private void requestPermissionGallery() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_PERMISSION_CODE_GALLERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE_AUDIO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: permission denied");
                }
                break;
            }
            case REQUEST_PERMISSION_CODE_GALLERY: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                } else {
                    checkAndroidVersionAndPermissionForGallery();
                }
                break;
            }
            case REQUEST_PERMISSION_CODE_CAMERA: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    requestPermissionCamera();
                }
                break;
            }
        }
    }

    private boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO);

        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }


    public void checkAndroidVersionAndPermissionForGallery() {
        //REQUEST PERMISSION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE_GALLERY);
            } catch (Exception e) {
                e.getMessage();
            }
        } else {
            pickImage();
        }
    }

    //TODO: implement Gallery picker
    private void pickImageFromGallery() {
        checkAndroidVersionAndPermissionForGallery();
    }

    private void pickImage() {
        artefactActivity.isGallery = true;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private void takePhotoFromCamera() {
        requestPermissionCamera();
    }

    private void takePhoto() {
        artefactActivity.isCamera = true;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_PERMISSION_CODE_CAMERA);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artefactActivity = (ArtefactActivity) getActivity();
        artefactActivity.setTitle("New Artefact");
        mArgs = getArguments();
        artefactActivity.currentScreen = UserScreen.NEW_ARTEFACT;

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

    private void initNewArtefact(View view) {
        artefactActivity.hideSoftKeyboard();
        mProgressViewNewArtefact = view.findViewById(R.id.progress_new_artefact);
        mFormViewNewArtefact = view.findViewById(R.id.form_new_artefact);
        tvLoadNewArtefact = view.findViewById(R.id.tvLoad_new_artefact);

        ivNewArtefact = view.findViewById(R.id.image_new_artefact);
        etNewArtefactDate = view.findViewById(R.id.et_new_artefact_date);
        etNewArtefactName = view.findViewById(R.id.et_new_artefact_name);
        etNewArtefactDescription = view.findViewById(R.id.et_new_artefact_description);
        btnNewArtefactSave = view.findViewById(R.id.button_new_artefact_save);
        btnAudio = view.findViewById(R.id.button_new_artefact_audio);
        btnTakeFotoFromCamera = view.findViewById(R.id.button_new_artefact_image_from_camera);
        btnTakeFotoFromGallery = view.findViewById(R.id.button_new_artefact_image_from_gallery);
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
            ArtefactImageBitmap artefactImageBitmap = ArtefactImageBitmap.getInstance();
            switch (origin) {
                case ORIGIN_CAMERA: // artefact image is taken with camera
                    Log.d(TAG, "initNewArtefact: origin: " + origin);
                    artefactBitmap = BitmapFactory.decodeByteArray(artefactImageBitmap.getByteArray(), 0, artefactImageBitmap.getByteArray().length);
                    ivNewArtefact.setImageBitmap(artefactBitmap);
                    mLat = ApplicationClass.mTempArtefactLatLng.latitude;
                    mLng = ApplicationClass.mTempArtefactLatLng.longitude;
                    break;
                case ORIGIN_GALLERY:
                    Log.d(TAG, "initNewArtefact: origin: " + origin);
                    artefactBitmap = BitmapFactory.decodeByteArray(artefactImageBitmap.getByteArray(), 0, artefactImageBitmap.getByteArray().length);
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
                default:
                    Log.d(TAG, "initNewArtefact: backpressed in camera mode");
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
