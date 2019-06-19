package com.example.tim.romaniitedomum.artefact;

import com.example.tim.romaniitedomum.R;
import com.example.tim.romaniitedomum.Util.Util;

/**
 * Created by TimStaats 19.03.2019
 */

public class Category {

    private String mCategoryName;
    private int mCategoryMarkerImage;

    public Category(String categoryName, int categoryMarker) {
        this.mCategoryName = categoryName;
        this.mCategoryMarkerImage = categoryMarker;
    }

    public static final Category[] categories = {
            new Category(Util.CATEGORY_BASILIKA, R.drawable.ic_map_basilica),
            new Category(Util.CATEGORY_BOGEN, R.drawable.ic_map_bogen),
            new Category(Util.CATEGORY_CHRISTENTUM, R.drawable.ic_map_christentum),
            new Category(Util.CATEGORY_GRABSTAETTE, R.drawable.ic_map_grabstaette),
            new Category(Util.CATEGORY_GRUENDUNGSMYTHOS, R.drawable.ic_map_grundungsmythos),
            new Category(Util.CATEGORY_INFRASTRUKTUR, R.drawable.ic_map_infrastruktur),
            new Category(Util.CATEGORY_KULTSTAETTE, R.drawable.ic_map_kultstaette),
            new Category(Util.CATEGORY_PLATZANLAGE, R.drawable.ic_map_platzanlage),
            new Category(Util.CATEGORY_POLITISCHE_INSTITUTION, R.drawable.ic_map_politische_institution),
            new Category(Util.CATEGORY_SPIELSTAETTE, R.drawable.ic_map_spielstaette),
            new Category(Util.CATEGORY_THERME, R.drawable.ic_map_therme),
            new Category(Util.CATEGORY_WOHNKOMPLEX, R.drawable.ic_map_wohnkomplex)
    };

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
