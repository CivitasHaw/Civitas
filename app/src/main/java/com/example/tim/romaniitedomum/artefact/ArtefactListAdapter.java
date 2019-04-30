package com.example.tim.romaniitedomum.artefact;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.tim.romaniitedomum.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TimStaats 11.03.2019
 */

public class ArtefactListAdapter extends RecyclerView.Adapter<ArtefactListAdapter.ArtefactListViewHolder> implements Filterable {

    private static final String TAG = "ArtefactListAdapter";

    private List<Artefact> mArtefactListFull;
    public boolean isCategoryFilter = false;
    private List<Artefact> mArtefactList;
    private ImageLoader mImageLoader;
    private OnItemClickListener mListener;

    /**
     * credits codingInFlow
     * https://www.youtube.com/watch?v=sJ-Z9G0SDhc
     */
    @Override
    public Filter getFilter() {
        return artefactFilter;
    }

    private Filter artefactFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Artefact> filteredList = new ArrayList<>();
            
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mArtefactListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Artefact item: mArtefactListFull) {
                    if (isCategoryFilter) {
                        if (item.getCategoryName().toLowerCase().equals(filterPattern)) {
                            filteredList.add(item);
                        }
                    } else {
                        if (item.getArtefactName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
                isCategoryFilter = false;
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mArtefactList.clear();
            mArtefactList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public interface OnItemClickListener {
        void onItemclick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public static class ArtefactListViewHolder extends RecyclerView.ViewHolder {

        public ImageView mIvArtefact;
        public TextView mTvArtefactName, mTvArtefactCategory, mTvArtefactDescription, mTvAverageRating;
        public ProgressBar mProgress;
        public RatingBar mRatingBar;

        public ArtefactListViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            mIvArtefact = itemView.findViewById(R.id.image_cardview_artefact);
            mTvArtefactName = itemView.findViewById(R.id.text_cardview_artefact_name);
            mTvArtefactCategory = itemView.findViewById(R.id.text_cardview_artefact_category);
            //mTvArtefactDescription = itemView.findViewById(R.id.text_cardview_artefact_description);
            mProgress = itemView.findViewById(R.id.progress_cardview_artefact);
            mTvAverageRating = itemView.findViewById(R.id.text_rating_average);
            mRatingBar = itemView.findViewById(R.id.ratingbar_artefact_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: click");
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemclick(position);
                        }
                    }
                }
            });
        }
    }

    public ArtefactListAdapter(List<Artefact> artefacts, ImageLoader loader) {
        mArtefactListFull = new ArrayList<>(artefacts);
        this.mArtefactList = artefacts;
        mImageLoader = loader;
    }

    @NonNull
    @Override
    public ArtefactListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_artefact, parent, false);
        ArtefactListViewHolder holder = new ArtefactListViewHolder(v, mListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ArtefactListViewHolder holder, int position) {

        //Artefact currentArtefact = mArtefactListFull.get(position);
        Artefact currentArtefact = mArtefactList.get(position);

        //mImageLoader.displayImage(mArtefactListFull.get(position).getArtefactImageUrl(), holder.mIvArtefact, new ImageLoadingListener() {
        mImageLoader.displayImage(currentArtefact.getArtefactImageUrl(), holder.mIvArtefact, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                holder.mProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holder.mIvArtefact.setImageResource(R.drawable.civitas_main_logo);
                holder.mProgress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.mProgress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        holder.mTvArtefactName.setText(currentArtefact.getArtefactName());
        holder.mTvArtefactCategory.setText("#" + currentArtefact.getCategoryName());
        //holder.mTvArtefactDescription.setText(currentArtefact.getArtefactDescription());

    }

    @Override
    public int getItemCount() {
        //return mArtefactListFull.size();
        return mArtefactList == null ? 0 : mArtefactList.size();
    }
}
