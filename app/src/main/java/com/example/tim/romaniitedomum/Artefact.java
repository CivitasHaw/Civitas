package com.example.tim.romaniitedomum;

import android.location.Location;

// in Backendless classnames become a table
public class Artefact {

    // in Backendless variables become a column
    private int id;
    private String name;
    private String dating;
    private String description;
    private String image;
    private double latitude = -1;
    private double longitude = -1;
    private Location location;
    //private Category category;
    //private int categoryId;
    //private List<ArtefactItem> artefactItems;

    public Artefact(){
        id = 0;
        name = null;
        dating = null;
        description = null;
        image = null;
        location = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDating() {
        return dating;
    }

    public void setDating(String dating) {
        this.dating = dating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
