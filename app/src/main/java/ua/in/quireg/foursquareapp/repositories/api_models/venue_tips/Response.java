package ua.in.quireg.foursquareapp.repositories.api_models.venue_tips;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("tips")
    @Expose
    private Tips tips;

    public Tips getTips() {
        return tips;
    }

    public void setTips(Tips tips) {
        this.tips = tips;
    }

}
