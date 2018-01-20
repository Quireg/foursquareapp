
package ua.in.quireg.foursquareapp.repositories.api_models.single_venue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contact_ {

    @SerializedName("twitter")
    @Expose
    private String twitter;

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

}
