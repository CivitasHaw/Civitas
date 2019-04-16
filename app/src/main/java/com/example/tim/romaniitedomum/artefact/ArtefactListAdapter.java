package com.example.tim.romaniitedomum.artefact;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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

    private List<Artefact> mArtefactList;
    private List<Artefact> mFilteredArtefactList;
    private ImageLoader mImageLoader;
    private OnItemClickListener mListener;
    private ItemFilter mFilter = new ItemFilter();


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
        mArtefactList = artefacts;
        mFilteredArtefactList = artefacts;
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

        //Artefact currentArtefact = mArtefactList.get(position);
        Artefact currentArtefact = mFilteredArtefactList.get(position);

        //mImageLoader.displayImage(mArtefactList.get(position).getArtefactImageUrl(), holder.mIvArtefact, new ImageLoadingListener() {
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
        //return mArtefactList.size();
        return mFilteredArtefactList == null ? 0 : mFilteredArtefactList.size();
    }

    /**
     * Source
     * https://gist.github.com/fjfish/3024308
     * https://stackoverflow.com/questions/24769257/custom-listview-adapter-with-filter-android
     */
    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {

        Artefact filterableArtefact = new Artefact();

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String filterString = charSequence.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Artefact> list = mFilteredArtefactList;
            //Log.d(TAG, "performFiltering: list.size(): " + list.size());
            for (int i = 0; i < list.size(); i++) {
                //Log.d(TAG, "performFiltering: list: " + list.get(i).getArtefactName());
            }

            int count = list.size();

            final ArrayList<Artefact> nlist = new ArrayList<>(count);
            //Artefact filterableArtefact = new Artefact();

            for (int i = 0; i < count; i++) {
                filterableArtefact = list.get(i);
                if (filterableArtefact.getArtefactName().toLowerCase().contains(filterString)) {
                    //Log.d(TAG, "performFiltering: " + filterableArtefact.getArtefactName());
                    nlist.add(filterableArtefact);
                }
            }

    /*        final ArrayList<String> nlist = new ArrayList<>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getArtefactName();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }*/

            if (filterString.isEmpty()) {
                results.values = list;
                results.count = list.size();
            } else {
                results.values = nlist;
                results.count = nlist.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mFilteredArtefactList = (ArrayList<Artefact>) filterResults.values;
            notifyDataSetChanged();
        }
    }


}
