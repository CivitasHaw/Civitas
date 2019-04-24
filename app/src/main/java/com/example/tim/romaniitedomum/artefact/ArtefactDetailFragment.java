package com.example.tim.romaniitedomum.artefact;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.Geo;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.GeoPoint;
import com.example.tim.romaniitedomum.ApplicationClass;
import com.example.tim.romaniitedomum.R;
import com.example.tim.romaniitedomum.Util.Util;
import com.example.tim.romaniitedomum.map.MapActivity;
import com.google.android.gms.maps.model.LatLng;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Date;

/**
 * Created by TimStaats 12.03.2019
 */

public class ArtefactDetailFragment extends Fragment {

    private static final String TAG = "ArtefactDetailFragment";

    private ArtefactActivity artefactActivity;

    private View mProgressViewArtefactDetail;
    private View mFormViewArtefactDetail;
    private TextView tvLoadArtefactDetail;

    private ImageView ivArtefactDetail;
    private TextView tvArtefactDetailName, tvArtefactDetailCategory, tvArtefactDetailDescription;
    private TextView tvArtefactDetailAuthor, tvArtefactDetailCreated;
    private Button btnArtefactDetailMarker, btnArtefactDetailSaveRating;
    private ProgressBar mProgress;
    private RatingBar mRating;
    private ImageLoader mLoader;
    private int mPosition;

    private Bundle args;

    private LinearLayout audioLayout;
    private ImageButton btnAudioPlay, btnAudioPause, btnAudioStop;
    private Artefact mArtefact = null;
    private String imageFilePath = "";
    private String audioFilePath = "";
//    private File mAudioFile = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_artefact_detail, container, false);

        initArtefactDetail(view);

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        artefactActivity.setTitle(mArtefact.getArtefactName());
    }

/*
    // https://stackoverflow.com/questions/15758856/android-how-to-download-file-from-webserver
    class DownloadFileFromUrl extends AsyncTask<String, String, File> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected File doInBackground(String... urls) {
            int count;

            try {
                URL url = new URL(urls[0]);
                URLConnection connection = url.openConnection();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output Stream
                File file = new File(Environment.getExternalStorageDirectory() + "/myAudio.3gp");
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];
                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress...
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // write data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            btnAudioPlay.setEnabled(true);
            mAudioFile = file;
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(mAudioFile.getAbsolutePath());
                //mediaPlayer.setDataSource(file.getAbsolutePath());
                //mediaPlayer.prepare();
            } catch (IOException e) {
                Toast.makeText(artefactActivity, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (IllegalStateException e) {
                Toast.makeText(artefactActivity, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(artefactActivity, "Fertig", Toast.LENGTH_SHORT).show();
            //mediaPlayer.start();
            //btnAudioPlay.setBackground(getResources().getDrawable(R.drawable.buttons_pressed));
        }


    }
*/

    private void initArtefactDetail(View view) {

        mProgressViewArtefactDetail = view.findViewById(R.id.progress_artefact_detail);
        mFormViewArtefactDetail = view.findViewById(R.id.form_artefact_detail);
        tvLoadArtefactDetail = view.findViewById(R.id.tvLoad_artefact_detail);

        artefactActivity.isAtDetailFragment = true;
        ivArtefactDetail = view.findViewById(R.id.image_artefact_detail);
        tvArtefactDetailName = view.findViewById(R.id.text_artefact_detail_name);
        tvArtefactDetailCategory = view.findViewById(R.id.text_artefact_detail_category);
        tvArtefactDetailDescription = view.findViewById(R.id.text_artefact_detail_description);
        tvArtefactDetailAuthor = view.findViewById(R.id.text_artefact_detail_author);
        tvArtefactDetailCreated = view.findViewById(R.id.text_artefact_detail_created);
        btnArtefactDetailMarker = view.findViewById(R.id.button_artefact_detail_show_marker_on_map);
        mProgress = view.findViewById(R.id.progress_image_artefact_detail);
        mRating = view.findViewById(R.id.ratingbar_artefact_detail);
        btnArtefactDetailSaveRating = view.findViewById(R.id.button_artefact_detail_save_rating);
        audioLayout = view.findViewById(R.id.layout_artefact_detail_audio_player);
        btnAudioPlay = audioLayout.findViewById(R.id.button_artefact_detail_audio_play);
        btnAudioPause = audioLayout.findViewById(R.id.button_artefact_detail_audio_pause);
        btnAudioStop = audioLayout.findViewById(R.id.button_artefact_detail_audio_stop);

        mLoader = ApplicationClass.loader;

//        mAudioFile = new File(AUDIO_FILE_PATH);

        mRating.setNumStars(5);
        mRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

            }
        });

        //TODO: implement Rating
        btnArtefactDetailSaveRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rate = mRating.getRating();
                Toast.makeText(getContext(), "Rate: " + rate, Toast.LENGTH_SHORT).show();
            }
        });

        //TODO: implement Mediaplayer
        btnAudioPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(artefactActivity, "Clicki", Toast.LENGTH_SHORT).show();
            }
        });

        if (args != null){
            // navigating to artefactDetail from Map Markerclick
            String artefactObjectId = args.getString(Util.ARTEFACT_OBJECT_ID);

            for (int i = 0; i < ApplicationClass.mArtefactList.size(); i++) {
                if (ApplicationClass.mArtefactList.get(i).getObjectId().equals(artefactObjectId)){

                    mArtefact = ApplicationClass.mArtefactList.get(i);
                    ApplicationClass.position = ApplicationClass.mArtefactList.indexOf(mArtefact);
                    mPosition = ApplicationClass.position;
                    ApplicationClass.mTempArtefactLatLng = new LatLng(mArtefact.getLatitude(), mArtefact.getLongitude());
/*
                    if (mArtefact != null) {
                        if (mArtefact.getArtefactAudioUrl() != null) {
                            new DownloadFileFromUrl().execute(mArtefact.getArtefactAudioUrl());
                        }
                    }
*/

                    mLoader.displayImage(mArtefact.getArtefactImageUrl(), ivArtefactDetail, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            mProgress.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            ivArtefactDetail.setImageResource(R.drawable.civitas_main_logo);
                            mProgress.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            mProgress.setVisibility(View.INVISIBLE);

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });

                    fillArtefactTextViews(mArtefact);
                    artefactAudioFileExists(mArtefact);
                    provideFilePath();


                }
            }

        } else {
            // navigating to artefactDetail from ArtefactList item Click
            mPosition = ApplicationClass.position;
            mArtefact = ApplicationClass.mArtefactList.get(mPosition);
            ApplicationClass.mArtefact = mArtefact;
            ApplicationClass.mTempArtefactLatLng = new LatLng(mArtefact.getLatitude(), mArtefact.getLongitude());

            mLoader.displayImage(mArtefact.getArtefactImageUrl(), ivArtefactDetail, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    mProgress.setVisibility(View.VISIBLE);

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    ivArtefactDetail.setImageResource(R.drawable.civitas_main_logo);
                    mProgress.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    mProgress.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });

            fillArtefactTextViews(mArtefact);
            artefactAudioFileExists(mArtefact);
            provideFilePath();
        }

        btnArtefactDetailMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(artefactActivity, MapActivity.class);
                intent.putExtra("origin", "artefactDetail");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu_map, menu);
        if (mArtefact.getOwnerId().equals(ApplicationClass.user.getProperty(getResources().getString(R.string.backendless_property_ownerid)).toString())) {
            inflater.inflate(R.menu.toolbar_menu_artefact, menu);
            //TODO: implement editing current artefact
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.button_my_location:
                Intent intent = new Intent(artefactActivity, MapActivity.class);
                intent.putExtra("objectId", mArtefact.getObjectId());
                intent.putExtra(getResources().getString(R.string.origin), TAG);
                ApplicationClass.mArtefact = mArtefact;
                artefactActivity.isAtDetailFragment = false;
                artefactActivity.isAtListFragment = false;
                startActivity(intent);
                break;
            // TODO: edit artefact
            case R.id.edit_artefact:
                Log.d(TAG, "onOptionsItemSelected: edit: clicked");
                break;
            case R.id.delete_artefact:
                Log.d(TAG, "onOptionsItemSelected: delete: clicked");
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setMessage("Are you sure you want to delete the artefact?")
                        .setTitle("Delete Artefact")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "onClick: yes button clicked");
                                deleteImageFileFromBackendless(imageFilePath);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "onClick: no button clicked");
                            }
                        })
                        .show();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteImageFileFromBackendless(String imageFilePath) {
        tvLoadArtefactDetail.setText(getResources().getString(R.string.artefact_detail_delete_image));
        showProgress(true);
        Log.d(TAG, "deleteImageFileFromBackendless: is called");
        Backendless.Files.remove(imageFilePath, new AsyncCallback<Integer>() {
            @Override
            public void handleResponse(Integer response) {
                Log.d(TAG, "handleResponse: Image successfully deleted!");

                if (mArtefact.getArtefactAudioUrl()!= null) {
                    deleteAudioFileFromBackendless(audioFilePath);
                } else {
                    GeoPoint artefactLocation = new GeoPoint();
                    artefactLocation.setObjectId(mArtefact.getArtefactLocationObjectId());
                    Log.d(TAG, "handleResponse: objectId: " + artefactLocation.getObjectId());
                    deleteGeoPointFromBackendless(artefactLocation);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                showProgress(false);
                Toast.makeText(artefactActivity, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "handleFault: Error: " + fault.getMessage());
            }
        });
    }

    private void deleteAudioFileFromBackendless(String audioFilePath) {
        Log.d(TAG, "deleteAudioFileFromBackendless: is called");
        tvLoadArtefactDetail.setText(getResources().getString(R.string.artefact_detail_delete_audio));
        showProgress(true);
        Backendless.Files.remove(audioFilePath, new AsyncCallback<Integer>() {
            @Override
            public void handleResponse(Integer response) {
                GeoPoint artefactLocation = new GeoPoint();
                artefactLocation.setObjectId(mArtefact.getArtefactLocationObjectId());
                deleteGeoPointFromBackendless(artefactLocation);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                showProgress(false);
                Toast.makeText(artefactActivity, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //TODO: change saving algorithm for artefact -> image -> audio -> geoPoint
    private void deleteGeoPointFromBackendless(GeoPoint location) {
        showProgress(true);
        tvLoadArtefactDetail.setText(getResources().getString(R.string.artefact_detail_remove_marker));
        Log.d(TAG, "deleteGeoPointFromBackendless: is called");
        Backendless.Geo.removePoint(location, new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void response) {
                Log.d(TAG, "handleResponse: GeoPoint successfully deleted!");
                deleteArtefactFromBackendless(mArtefact);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                showProgress(false);
                Toast.makeText(artefactActivity, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteArtefactFromBackendless(final Artefact artefact) {
        tvLoadArtefactDetail.setText(getResources().getString(R.string.artefact_detail_delete_artefact));
        showProgress(true);
        Backendless.Persistence.of(Artefact.class).remove(artefact, new AsyncCallback<Long>() {
            @Override
            public void handleResponse(Long response) {
                Log.d(TAG, "handleResponse: Artefact successfully deleted");
                ApplicationClass.mArtefactList.remove(artefact);
                ApplicationClass.mArtefact = null;

                Intent intent = new Intent(artefactActivity, MapActivity.class);
                intent.putExtra(getResources().getString(R.string.origin), Util.ORIGIN_DELETE_ARTEFACT);
                startActivity(intent);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                showProgress(false);
                Toast.makeText(artefactActivity, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillArtefactTextViews(Artefact artefact) {
        String author = getResources().getString(R.string.artefact_detail_author) + " " + artefact.getAuthorName() + " ";
        Date date = artefact.getCreated();
        String d = "";
        try {
            d =  "Created: " + date.toLocaleString();
        } catch (NullPointerException e) {
            Log.e(TAG, "fillArtefactTextViews: NullpointerException: " + e.getMessage());
        }
        String category = "#" + artefact.getCategoryName();
        tvArtefactDetailName.setText(artefact.getArtefactName());
        tvArtefactDetailAuthor.setText(author);
        tvArtefactDetailCreated.setText(d);
        tvArtefactDetailCategory.setText(category);
        tvArtefactDetailDescription.setText(artefact.getArtefactDescription());
    }

    private void provideFilePath() {
        audioFilePath = "artefactAudios/" + mArtefact.getArtefactAudioFileName();
        imageFilePath = "artefactImages/" + mArtefact.getArtefactImageFileName();
    }

    private void artefactAudioFileExists(Artefact mArtefact) {
        if (mArtefact.getArtefactAudioUrl() != null){
            audioLayout.setVisibility(View.VISIBLE);
        } else {
            audioLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        artefactActivity = (ArtefactActivity)getActivity();
        args = getArguments();
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

            mFormViewArtefactDetail.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormViewArtefactDetail.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormViewArtefactDetail.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressViewArtefactDetail.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressViewArtefactDetail.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressViewArtefactDetail.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoadArtefactDetail.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoadArtefactDetail.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoadArtefactDetail.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressViewArtefactDetail.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoadArtefactDetail.setVisibility(show ? View.VISIBLE : View.GONE);
            mFormViewArtefactDetail.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
