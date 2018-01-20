
package ua.in.quireg.foursquareapp.repositories.api_models.search_venues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LabeledLatLng {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("lat")
    @Expose
    private Float lat;
    @SerializedName("lng")
    @Expose
    private Float lng;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

}
