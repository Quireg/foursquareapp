package ua.in.quireg.foursquareapp.mvp.models.presentation;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 4:31 PM.
 * foursquareapp
 */

public class PlaceEntity {

    private static final String PREFERRED_IMAGE_SIZE = "100x100";

    private String name = "Unknown";
    private String type = "Uncategorized";
    private String priceCategory = "$";
    private String address = "";
    private String distanceTo = "?";
    private Uri imageUri;
    private String rating = "^_^";
    private String ratingColor = "#1B5E20";

    @NonNull public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @NonNull public String getPriceCategory() {
        return priceCategory;
    }

    public void setPriceCategory(String priceCategory) {
        this.priceCategory = priceCategory;
    }

    @NonNull public String getDistanceTo() {
        return distanceTo;
    }

    public void setDistanceTo(String distanceTo) {
        this.distanceTo = distanceTo;
    }

    @Nullable public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public void setImageUri(String prefix, String suffix) {
        this.imageUri = Uri.parse(prefix.concat(PREFERRED_IMAGE_SIZE).concat(suffix));
    }

    @NonNull public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @NonNull public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @NonNull public String getRatingColor() {
        return ratingColor;
    }

    public void setRatingColor(String ratingColor) {
        this.ratingColor = ratingColor;
    }
}
