package ua.in.quireg.foursquareapp.mvp.models.presentation;

import android.net.Uri;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 4:31 PM.
 * foursquareapp
 */

public class PlaceEntity {

    private String name = "Unknown";
    private String type = "Uncategorized";
    private String priceCategory = "$";
    private String address = "";
    private String distanceTo = "?";
    private Uri image = Uri.parse("https://cdn2.iconfinder.com/data/icons/picons-essentials/71/smiley_sad-512.png");
    private String rating = "";
    private String ratingColor = "#00000000";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPriceCategory() {
        return priceCategory;
    }

    public void setPriceCategory(String priceCategory) {
        this.priceCategory = priceCategory;
    }

    public String getDistanceTo() {
        return distanceTo;
    }

    public void setDistanceTo(String distanceTo) {
        this.distanceTo = distanceTo;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRatingColor() {
        return ratingColor;
    }

    public void setRatingColor(String ratingColor) {
        this.ratingColor = ratingColor;
    }
}
