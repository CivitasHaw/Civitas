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
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.GeoPoint;
import com.example.tim.romaniitedomum.ApplicationClass;
import com.example.tim.romaniitedomum.R;
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
//    private static final String AUDIO_FILE_PATH = "audio";

    private ArtefactActivity artefactActivity;

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

        artefactActivity.isAtDetailFragment = true;
        ivArtefactDetail = view.findViewById(R.id.image_artefact_detail);
        tvArtefactDetailName = view.findViewById(R.id.text_artefact_detail_name);
        tvArtefactDetailCategory = view.findViewById(R.id.text_artefact_detail_category);
        tvArtefactDetailDescription = view.findViewById(R.id.text_artefact_detail_description);
        tvArtefactDetailAuthor = view.findViewById(R.id.text_artefact_detail_author);
        tvArtefactDetailCreated = view.findViewById(R.id.text_artefact_detail_created);
        btnArtefactDetailMarker = view.findViewById(R.id.button_artefact_detail_show_marker_on_map);
        mProgress = view.findViewById(R.id.progress_artefact_detail);
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
            String artefactName = args.getString("artefactName");
            double lat = args.getDouble("latitude");
            double lng = args.getDouble("longitude");
            for (int i = 0; i < ApplicationClass.mArtefactList.size(); i++) {
                if (ApplicationClass.mArtefactList.get(i).getArtefactName().equals(artefactName) &&
                        ApplicationClass.mArtefactList.get(i).getLatitude() == lat &&
                        ApplicationClass.mArtefactList.get(i).getLongitude() == lng) {

                    mArtefact = ApplicationClass.mArtefactList.get(i);
                    ApplicationClass.position = ApplicationClass.mArtefactList.indexOf(mArtefact);
                    mPosition = ApplicationClass.position;
/*
                    if (mArtefact != null) {
                        if (mArtefact.getArtefactAudioUrl() != null) {
                            new DownloadFileFromUrl().execute(mArtefact.getArtefactAudioUrl());
                        }
                    }
*/

                    if (mArtefact.getOwnerId().equals(ApplicationClass.user.getProperty(getResources().getString(R.string.backendless_property_ownerid)).toString())){
                        Log.d(TAG, "initArtefactDetail: artefact was created by current user");
                        //Toast.makeText(artefactActivity, "Happy B-Day", Toast.LENGTH_LONG).show();
                    }

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


                }
            }

        } else {
            // navigating to artefactDetail from ArtefactList item Click
            mPosition = ApplicationClass.position;
            mArtefact = ApplicationClass.mArtefactList.get(mPosition);

            if (mArtefact.getOwnerId().equals(ApplicationClass.user.getProperty(getResources().getString(R.string.backendless_property_ownerid)).toString())){
                Log.d(TAG, "initArtefactDetail: artefact was created by current user");
                //Toast.makeText(artefactActivity, "Happy B-Day", Toast.LENGTH_LONG).show();
            }

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
                artefactActivity.isAtDetailFragment = false;
                artefactActivity.isAtListFragment = false;
                startActivity(intent);
                break;
            // TODO: edit artefact
            case R.id.edit_artefact:
                Log.d(TAG, "onOptionsItemSelected: edit: clicked");
                //Toast.makeText(artefactActivity, "Clicki", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete_artefact:
                Log.d(TAG, "onOptionsItemSelected: delete: clicked");
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setMessage("Are you sure you want to delete the artefact?")
                        .setTitle("Delete Artefact")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "onClick: ok button clicked");
                                deleteArtefactFromBackendless(mArtefact);
                                //deleteGeoPointFromBackendless(mArtefact);
                                //deleteImageFileFromBackendless(mArtefact);
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

    private void deleteImageFileFromBackendless(String filePath) {
        Log.d(TAG, "deleteImageFileFromBackendless: file_path: " + filePath);
        Backendless.Files.remove(filePath, new AsyncCallback<Integer>() {
            @Override
            public void handleResponse(Integer response) {
                Toast.makeText(artefactActivity, "Image successfully deleted!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "handleResponse: Image successfully deleted!");
                startActivity(new Intent(getContext(), MapActivity.class));
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(artefactActivity, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //TODO: change saving algorithm for artefact -> image -> audio -> geoPoint
    private void deleteGeoPointFromBackendless(Artefact mArtefact) {

        GeoPoint geoPoint = new GeoPoint(mArtefact.getLocation().getLatitude(), mArtefact.getLocation().getLongitude());
        Log.d(TAG, "deleteGeoPointFromBackendless: geoPoint.objectId(): " + geoPoint.getObjectId());

 /*       Backendless.Geo.removePoint(geoPoint, new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void response) {
                Toast.makeText(artefactActivity, "GeoPoint successfully deleted!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "handleResponse: GeoPoint successfully deleted!");
                deleteArtefactFromBackendless(mArtefact);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(artefactActivity, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    private void deleteArtefactFromBackendless(final Artefact mArtefact) {
        Backendless.Persistence.of(Artefact.class).remove(mArtefact, new AsyncCallback<Long>() {
            @Override
            public void handleResponse(Long response) {
                imageFilePath = "artefactImages/" + mArtefact.getArtefactName() + "_" + mArtefact.getArtefactDescription() + ".png";
                ApplicationClass.mArtefactList.remove(mArtefact);
                Toast.makeText(artefactActivity, "Artefact successfully deleted", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "handleResponse: Artefact successfully deleted");
                deleteImageFileFromBackendless(imageFilePath);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(artefactActivity, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillArtefactTextViews(Artefact artefact) {
        String author = getResources().getString(R.string.artefact_detail_author) + " " + artefact.getAuthorName() + " ";
        Date date = mArtefact.getCreated();
        //String d = getResources().getString(R.string.artefact_detail_created) + " " + date.toLocaleString();
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
}
