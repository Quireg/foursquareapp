
package ua.in.quireg.foursquareapp.repositories.api_models.search_venues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VenuePage {

    @SerializedName("id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
