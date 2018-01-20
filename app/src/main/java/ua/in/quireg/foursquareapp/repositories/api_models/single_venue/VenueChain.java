
package ua.in.quireg.foursquareapp.repositories.api_models.single_venue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VenueChain {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("bestName")
    @Expose
    private BestName bestName;
    @SerializedName("logo")
    @Expose
    private Logo logo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BestName getBestName() {
        return bestName;
    }

    public void setBestName(BestName bestName) {
        this.bestName = bestName;
    }

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

}
