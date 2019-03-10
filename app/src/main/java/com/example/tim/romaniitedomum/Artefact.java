package com.example.tim.romaniitedomum;

import com.backendless.geo.GeoPoint;

import java.util.Date;
import java.util.List;

/**
 * Created by TimStaats 21.02.2019
 */

// in Backendless classnames become a table
public class Artefact {

    private Date created;
    private Date updated;
    private String objectId;
    private String userEmail;
    private String artefactName;
    private String artefactImageUrl;
    private String artefactAudioUrl;
    private String artefactDescription;
    private GeoPoint location;
    private double latitude;
    private double longitude;
    private List<Artefact> artefactSiblings;

    public Artefact() {
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getArtefactName() {
        return artefactName;
    }

    public void setArtefactName(String artefactName) {
        this.artefactName = artefactName;
    }

    public String getArtefactImageUrl() {
        return artefactImageUrl;
    }

    public void setArtefactImageUrl(String artefactImageUrl) {
        this.artefactImageUrl = artefactImageUrl;
    }

    public String getArtefactAudioUrl() {
        return artefactAudioUrl;
    }

    public void setArtefactAudioUrl(String artefactAudioUrl) {
        this.artefactAudioUrl = artefactAudioUrl;
    }

    public String getArtefactDescription() {
        return artefactDescription;
    }

    public void setArtefactDescription(String artefactDescription) {
        this.artefactDescription = artefactDescription;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public List<Artefact> getArtefactSiblings() {
        return artefactSiblings;
    }

    public void setArtefactSiblings(List<Artefact> artefactSiblings) {
        this.artefactSiblings = artefactSiblings;
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
}
/*    --------------------------------------------------------------------------
public class Artefact {

    // in Backendless variables become a column
    private int id;
    private String user;
    private String name;
    private String dating;
    private String description;
    private String imageUrl;
    private String audioUrl;
    private double latitude = -1;
    private double longitude = -1;
    private Location location;
    //private Category category;        // basilika, bogen, grabstätte, therme, ...
    //private int categoryId;
    //private List<ArtefactItem> artefactItems;
    private List<Artefact> artefactList;

    public Artefact(){
        id = 0;
        name = null;
        dating = null;
        description = null;
        imageUrl = null;
        location = null;
        artefactList = new ArrayList<>();
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
*/
