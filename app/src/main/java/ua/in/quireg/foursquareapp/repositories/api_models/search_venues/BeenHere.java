
package ua.in.quireg.foursquareapp.repositories.api_models.search_venues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BeenHere {

    @SerializedName("lastCheckinExpiredAt")
    @Expose
    private Integer lastCheckinExpiredAt;

    public Integer getLastCheckinExpiredAt() {
        return lastCheckinExpiredAt;
    }

    public void setLastCheckinExpiredAt(Integer lastCheckinExpiredAt) {
        this.lastCheckinExpiredAt = lastCheckinExpiredAt;
    }

}
