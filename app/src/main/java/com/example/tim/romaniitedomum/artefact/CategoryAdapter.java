package com.example.tim.romaniitedomum.artefact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.tim.romaniitedomum.R;

import java.util.ArrayList;

/**
 * Created by TimStaats 19.03.2019
 */

public class CategoryAdapter extends ArrayAdapter<CategoryItem> {

    public CategoryAdapter(Context context, ArrayList<CategoryItem> categoryList) {
        super(context, 0, categoryList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_category_row, parent, false
            );
        }

        ImageView ivCategory = convertView.findViewById(R.id.image_category);
        TextView tvCategory = convertView.findViewById(R.id.text_category);

        CategoryItem currentItem = getItem(position);

        if (currentItem != null){
            ivCategory.setImageResource(currentItem.getCategoryMarkerImage());
            tvCategory.setText(currentItem.getCategoryName());
        }

        return convertView;
    }
}
