package ua.in.quireg.foursquareapp.repositories.api_models.single_venue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("venue")
    @Expose
    private VenueExtended venue;

    public VenueExtended getVenue() {
        return venue;
    }

    public void setVenue(VenueExtended venue) {
        this.venue = venue;
    }

}
