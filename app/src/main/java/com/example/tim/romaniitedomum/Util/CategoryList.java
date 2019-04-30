package com.example.tim.romaniitedomum.Util;

import com.example.tim.romaniitedomum.R;
import com.example.tim.romaniitedomum.artefact.Category;

import java.util.ArrayList;

public class CategoryList {

    private static ArrayList<Category> categoryList = new ArrayList<Category>();

    public static ArrayList<Category> getCategoryList() {

        categoryList.add(new Category("SaltAndPepper", R.drawable.ic_salt_and_pepper));
        categoryList.add(new Category(Util.CATEGORY_BASILIKA, R.drawable.ic_map_basilica));
        categoryList.add(new Category(Util.CATEGORY_BOGEN, R.drawable.ic_map_bogen));
        categoryList.add(new Category(Util.CATEGORY_CHRISTENTUM, R.drawable.ic_map_christentum));
        categoryList.add(new Category(Util.CATEGORY_GRABSTAETTE, R.drawable.ic_map_grabstaette));
        categoryList.add(new Category(Util.CATEGORY_GRUENDUNGSMYTHOS, R.drawable.ic_map_grundungsmythos));
        categoryList.add(new Category(Util.CATEGORY_INFRASTRUKTUR, R.drawable.ic_map_infrastruktur));
        categoryList.add(new Category(Util.CATEGORY_KULTSTAETTE, R.drawable.ic_map_kultstaette));
        categoryList.add(new Category(Util.CATEGORY_PLATZANLAGE, R.drawable.ic_map_platzanlage));
        categoryList.add(new Category(Util.CATEGORY_POLITISCHE_INSTITUTION, R.drawable.ic_map_politische_institution));
        categoryList.add(new Category(Util.CATEGORY_SPIELSTAETTE, R.drawable.ic_map_spielstaette));
        categoryList.add(new Category(Util.CATEGORY_THERME, R.drawable.ic_map_therme));
        categoryList.add(new Category(Util.CATEGORY_WOHNKOMPLEX, R.drawable.ic_map_wohnkomplex));

        return categoryList;
    }
}
