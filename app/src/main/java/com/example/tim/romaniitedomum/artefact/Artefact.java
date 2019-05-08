package com.example.tim.romaniitedomum.artefact;

import com.backendless.geo.GeoPoint;

import java.util.Date;
import java.util.List;

/**
 * Created by TimStaats 21.02.2019
 */

// in Backendless classnames become a table
public class Artefact {

    // created, updated and objectId will be automatically created by backendless for each Artefact object
    private Date created;
    private Date updated;
    private String objectId;
    private String ownerId;
    private String userEmail;
    private String authorName;
    private String artefactName;
    private String artefactAge;
    private String annoDomini;
    private String artefactImageUrl;
    private String artefactImageFileName;
    private String artefactAudioFileName;
    private String artefactAudioUrl;
    private String artefactDescription;
    private String artefactLocationObjectId;
    private GeoPoint location;
    private double latitude;
    private double longitude;
    private String categoryName;
    private int categoryMarkerImage;
    //private Category category;
    private List<Artefact> artefactSiblings;

    public Artefact() {
    }

    public String getAnnoDomini() {
        return annoDomini;
    }

    public void setAnnoDomini(String annoDomini) {
        this.annoDomini = annoDomini;
    }

    public String getArtefactAge() {
        return artefactAge;
    }

    public void setArtefactAge(String artefactAge) {
        this.artefactAge = artefactAge;
    }

    public String getArtefactLocationObjectId() {
        return artefactLocationObjectId;
    }

    public void setArtefactLocationObjectId(String artefactLocationObjectId) {
        this.artefactLocationObjectId = artefactLocationObjectId;
    }

    public String getArtefactImageFileName() {
        return artefactImageFileName;
    }

    public void setArtefactImageFileName(String artefactImageFileName) {
        this.artefactImageFileName = artefactImageFileName;
    }

    public String getArtefactAudioFileName() {
        return artefactAudioFileName;
    }

    public void setArtefactAudioFileName(String artefactAudioFileName) {
        this.artefactAudioFileName = artefactAudioFileName;
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
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

/*
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
*/

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryMarkerImage() {
        return categoryMarkerImage;
    }

    public void setCategoryMarkerImage(int categoryMarkerImage) {
        this.categoryMarkerImage = categoryMarkerImage;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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
