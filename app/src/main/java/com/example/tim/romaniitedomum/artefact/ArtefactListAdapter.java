package com.example.tim.romaniitedomum.artefact;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tim.romaniitedomum.R;

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

    public ArtefactListAdapter(List<Artefact> artefacts) {
        mArtefactListFull = new ArrayList<>(artefacts);
        this.mArtefactList = artefacts;
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

        Artefact currentArtefact = mArtefactList.get(position);
        holder.mProgress.setVisibility(View.VISIBLE);
        Glide.with(holder.mIvArtefact)
                .load(currentArtefact.getArtefactImageUrl())
                .centerCrop()
                .placeholder(R.drawable.civitas_main_logo)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.mProgress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.mProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.mIvArtefact);

        holder.mTvArtefactName.setText(currentArtefact.getArtefactName());
        holder.mTvArtefactCategory.setText("#" + currentArtefact.getCategoryName());

    }

    @Override
    public int getItemCount() {
        return mArtefactList == null ? 0 : mArtefactList.size();
    }
}
