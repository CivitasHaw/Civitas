package com.example.tim.romaniitedomum.artefact;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tim.romaniitedomum.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by TimStaats 11.03.2019
 */

public class ArtefactListAdapter extends RecyclerView.Adapter<ArtefactListAdapter.ArtefactListViewHolder> {

    private static final String TAG = "ArtefactListAdapter";

    private List<Artefact> mArtefactList;
    private ImageLoader mImageLoader;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemclick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


    public static class ArtefactListViewHolder extends RecyclerView.ViewHolder{

        public ImageView mIvArtefact;
        public TextView mTvArtefactName, mTvArtefactCategory, mTvArtefactDescription;

        public ArtefactListViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            mIvArtefact = itemView.findViewById(R.id.image_cardview_artefact);
            mTvArtefactName = itemView.findViewById(R.id.text_cardview_artefact_name);
            mTvArtefactCategory = itemView.findViewById(R.id.text_cardview_artefact_category);
            mTvArtefactDescription = itemView.findViewById(R.id.text_cardview_artefact_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: click");
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemclick(position);
                        }
                    }
                }
            });
        }
    }

    public ArtefactListAdapter(List<Artefact> artefacts, ImageLoader loader){
        mArtefactList = artefacts;
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
    public void onBindViewHolder(@NonNull ArtefactListViewHolder holder, int position) {

        Artefact currentArtefact = mArtefactList.get(position);

        mImageLoader.displayImage(mArtefactList.get(position).getArtefactImageUrl(), holder.mIvArtefact);
        holder.mTvArtefactName.setText(currentArtefact.getArtefactName());
        holder.mTvArtefactDescription.setText(currentArtefact.getArtefactDescription());

    }

    @Override
    public int getItemCount() {
        return mArtefactList.size();
    }


}
