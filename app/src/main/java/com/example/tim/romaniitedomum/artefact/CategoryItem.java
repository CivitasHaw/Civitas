package com.example.tim.romaniitedomum.artefact;

/**
 * Created by TimStaats 19.03.2019
 */

public class CategoryItem {

    private String mCategoryName;
    private int mCategoryMarkerImage;

    public CategoryItem(String categoryName, int categoryMarker) {
        this.mCategoryName = categoryName;
        this.mCategoryMarkerImage = categoryMarker;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        this.mCategoryName = categoryName;
    }

    public int getCategoryMarkerImage() {
        return mCategoryMarkerImage;
    }

    public void setCategoryMarkerImage(int categoryMarkerImage) {
        this.mCategoryMarkerImage = categoryMarkerImage;
    }
}
