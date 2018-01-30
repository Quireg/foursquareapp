package ua.in.quireg.foursquareapp.models;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 4:31 PM.
 * foursquareapp
 */

public class PlaceEntity {

    private static final String PREFERRED_IMAGE_SIZE = "100x100";

    private String id = "id";
    private String name = "Unknown";
    private String type = "Uncategorized";
    private String priceCategory = "$";
    private String address = "";
    private String distanceTo = "?";
    private Uri imageUri;
    private String rating = "^_^";
    private String ratingColor = "#1B5E20";
    private String description;
    private LocationEntity locationEntity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @NonNull
    public String getPriceCategory() {
        return priceCategory;
    }

    public void setPriceCategory(String priceCategory) {
        this.priceCategory = priceCategory;
    }

    @NonNull
    public String getDistanceTo() {
        return distanceTo;
    }

    public void setDistanceTo(String distanceTo) {
        this.distanceTo = distanceTo;
    }

    @Nullable
    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public void setImageUri(String prefix, String suffix) {
        this.imageUri = Uri.parse(prefix.concat(PREFERRED_IMAGE_SIZE).concat(suffix));
    }

    @NonNull
    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @NonNull
    public String getRatingColor() {
        return ratingColor;
    }

    public void setRatingColor(String ratingColor) {
        this.ratingColor = ratingColor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocationEntity getLocationEntity() {
        return locationEntity;
    }

    public void setLocationEntity(LocationEntity locationEntity) {
        this.locationEntity = locationEntity;
    }
}
