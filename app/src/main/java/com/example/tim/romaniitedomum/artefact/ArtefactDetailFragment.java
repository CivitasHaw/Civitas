package com.example.tim.romaniitedomum.artefact;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.example.tim.romaniitedomum.ApplicationClass;
import com.example.tim.romaniitedomum.R;
import com.example.tim.romaniitedomum.map.MapActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
/*
                    if (mArtefact != null) {
                        if (mArtefact.getArtefactAudioUrl() != null) {
                            new DownloadFileFromUrl().execute(mArtefact.getArtefactAudioUrl());
                        }
                    }
*/

                    if (mArtefact.getOwnerId().equals(ApplicationClass.user.getProperty(getResources().getString(R.string.backendless_property_ownerid)).toString())){
                        Toast.makeText(artefactActivity, "Happy B-Day", Toast.LENGTH_LONG).show();
                    }

                    mLoader.displayImage(mArtefact.getArtefactImageUrl(), ivArtefactDetail, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            mProgress.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

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

            mLoader.displayImage(mArtefact.getArtefactImageUrl(), ivArtefactDetail, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    mProgress.setVisibility(View.VISIBLE);

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

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
            case R.id.edit_artefact:
                Toast.makeText(artefactActivity, "Clicki", Toast.LENGTH_SHORT).show();
                break;
        }


        return super.onOptionsItemSelected(item);
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
