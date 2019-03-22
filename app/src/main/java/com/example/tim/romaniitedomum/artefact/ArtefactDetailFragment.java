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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_artefact_detail, container, false);

        initArtefactDetail(view);

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

                    tvArtefactDetailName.setText(ApplicationClass.mArtefactList.get(i).getArtefactName());
                    //tvArtefactDetailCategory.setText("#" + ApplicationClass.mArtefactList.get(i).getCategory().getCategoryName());
                    tvArtefactDetailCategory.setText("#" + ApplicationClass.mArtefactList.get(i).getCategoryName());
                    tvArtefactDetailDescription.setText(ApplicationClass.mArtefactList.get(i).getArtefactDescription());
                    mLoader.displayImage(ApplicationClass.mArtefactList.get(i).getArtefactImageUrl(), ivArtefactDetail, new ImageLoadingListener() {
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
                }
            }

        } else {
            // navigating to artefactDetail from ArtefactList item Click
            mPosition = ApplicationClass.position;

            mLoader.displayImage(ApplicationClass.mArtefactList.get(mPosition).getArtefactImageUrl(), ivArtefactDetail, new ImageLoadingListener() {
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

            tvArtefactDetailName.setText(ApplicationClass.mArtefactList.get(mPosition).getArtefactName());
            tvArtefactDetailCategory.setText("#" + ApplicationClass.mArtefactList.get(mPosition).getCategoryName());
            tvArtefactDetailDescription.setText(ApplicationClass.mArtefactList.get(mPosition).getArtefactDescription());
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artefactActivity = (ArtefactActivity)getActivity();
        args = getArguments();
    }
}
