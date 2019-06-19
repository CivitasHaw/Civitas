package com.example.tim.romaniitedomum.Util;

import android.util.Log;

import com.example.tim.romaniitedomum.artefact.Artefact;

import java.util.List;

public class FilterHelper {

    private static final String TAG = "FilterHelper";

    public static FilterHelper instance;
    private String artefactName;
    private String category;
    private String ageFrom;
    private String ageTo;
    private List<Artefact> filteredArtefactList;
    private BcAc annoDominiFrom;
    private BcAc annoDominiTo;
    private boolean isFilterSet;

    private FilterHelper() {

    }

    public static FilterHelper getInstance() {
        if (instance == null) {
            Log.d(TAG, "getInstance: instance created");
            instance = new FilterHelper();
        }
        return instance;
    }

    public String getArtefactName() {
        return artefactName;
    }

    public void setArtefactName(String artefactName) {
        this.artefactName = artefactName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAgeFrom() {
        return ageFrom;
    }

    public void setAgeFrom(String ageFrom) {
        this.ageFrom = ageFrom;
    }

    public String getAgeTo() {
        return ageTo;
    }

    public void setAgeTo(String ageTo) {
        this.ageTo = ageTo;
    }

    public List<Artefact> getFilteredArtefactList() {
        return filteredArtefactList;
    }

    public void setFilteredArtefactList(List<Artefact> filteredArtefactList) {
        this.filteredArtefactList = filteredArtefactList;
    }

    public BcAc getAnnoDominiFrom() {
        return annoDominiFrom;
    }

    public void setAnnoDominiFrom(BcAc annoDominiFrom) {
        this.annoDominiFrom = annoDominiFrom;
    }

    public BcAc getAnnoDominiTo() {
        return annoDominiTo;
    }

    public void setAnnoDominiTo(BcAc annoDominiTo) {
        this.annoDominiTo = annoDominiTo;
    }

    public boolean isFilterSet() {
        return isFilterSet;
    }

    public void setFilterSet(boolean filterSet) {
        isFilterSet = filterSet;
    }


    public void prepareFilterHelper(String name, String category, String ageFrom, BcAc adFrom, String ageTo, BcAc adTo, boolean filter) {
        this.artefactName = name;
        this.category = category;
        this.ageFrom = ageFrom;
        this.annoDominiFrom = adFrom;
        this.ageTo = ageTo;
        this.annoDominiTo = adTo;
        this.isFilterSet = filter;
    }

    public void resetFilterHelperSettings() {
        this.annoDominiFrom = null;
        this.annoDominiTo = null;
        this.artefactName = null;
        this.ageFrom = null;
        this.ageTo = null;
        this.filteredArtefactList = null;
        this.category = null;
        this.isFilterSet = false;
    }

    @Override
    public String toString() {
        return "FilterHelper{" +
                "artefactName='" + artefactName + '\'' +
                ", category='" + category + '\'' +
                ", ageFrom=" + ageFrom +
                ", ageTo=" + ageTo +
                ", annoDominiFrom=" + annoDominiFrom +
                ", annoDominiTo=" + annoDominiTo +
                ", isFilterSet=" + isFilterSet +
                '}';
    }
}
