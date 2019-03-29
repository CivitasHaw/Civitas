package com.example.tim.romaniitedomum.artefact;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

/**
 * Created by TimStaats 12.03.2019
 */

public class ArtefactDetailFragment extends Fragment {

    private static final String TAG = "ArtefactDetailFragment";

    private ArtefactActivity artefactActivity;

    private ImageView ivArtefactDetail;
    private TextView tvArtefactDetailName, tvArtefactDetailCategory, tvArtefactDetailDescription;
    private Button btnArtefactDetailMarker, btnArtefactDetailSaveRating;
    private ProgressBar mProgress;
    private RatingBar mRating;
    private ImageLoader mLoader;
    private int mPosition;

    private Bundle args;

    private LinearLayout audioLayout;
    private ImageButton btnAudioPlay, btnAudioPause, btnAudioStop;
    private Artefact mArtefact = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_artefact_detail, container, false);

        initArtefactDetail(view);

        btnAudioPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        return view;

    }

    private void initArtefactDetail(View view) {
        ivArtefactDetail = view.findViewById(R.id.image_artefact_detail);
        tvArtefactDetailName = view.findViewById(R.id.text_artefact_detail_name);
        tvArtefactDetailCategory = view.findViewById(R.id.text_artefact_detail_category);
        tvArtefactDetailDescription = view.findViewById(R.id.text_artefact_detail_description);
        btnArtefactDetailMarker = view.findViewById(R.id.button_artefact_detail_show_marker_on_map);
        mProgress = view.findViewById(R.id.progress_artefact_detail);
        mRating = view.findViewById(R.id.ratingbar_artefact_detail);
        btnArtefactDetailSaveRating = view.findViewById(R.id.button_artefact_detail_save_rating);
        audioLayout = view.findViewById(R.id.layout_artefact_detail_audio_player);
        btnAudioPlay = audioLayout.findViewById(R.id.button_artefact_detail_audio_play);
        btnAudioPause = audioLayout.findViewById(R.id.button_artefact_detail_audio_pause);
        btnAudioStop = audioLayout.findViewById(R.id.button_artefact_detail_audio_stop);

        mLoader = ApplicationClass.loader;


        mRating.setNumStars(5);
        mRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

            }
        });

        btnArtefactDetailSaveRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rate = mRating.getRating();
                Toast.makeText(getContext(), "Rate: " + rate, Toast.LENGTH_SHORT).show();
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

                    if (mArtefact.getOwnerId().equals(ApplicationClass.user.getProperty("ownerId"))){
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

                    tvArtefactDetailName.setText(mArtefact.getArtefactName());
                    //tvArtefactDetailCategory.setText("#" + ApplicationClass.mArtefactList.get(i).getCategory().getCategoryName());
                    tvArtefactDetailCategory.setText("#" + mArtefact.getCategoryName());
                    tvArtefactDetailDescription.setText(mArtefact.getArtefactDescription());
                    artefactAudioFileExists(mArtefact);
//                    if (mArtefact.getArtefactAudioUrl() != null){
//                        audioLayout.setVisibility(View.VISIBLE);
//                    }

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

            tvArtefactDetailName.setText(mArtefact.getArtefactName());
            tvArtefactDetailCategory.setText("#" + mArtefact.getCategoryName());
            tvArtefactDetailDescription.setText(mArtefact.getArtefactDescription());
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

        artefactActivity = (ArtefactActivity)getActivity();
        args = getArguments();
    }
}
