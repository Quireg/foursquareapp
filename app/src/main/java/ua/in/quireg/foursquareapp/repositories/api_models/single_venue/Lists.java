package ua.in.quireg.foursquareapp.repositories.api_models.single_venue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Lists {

    @SerializedName("groups")
    @Expose
    private List<Group__> groups = null;

    public List<Group__> getGroups() {
        return groups;
    }

    public void setGroups(List<Group__> groups) {
        this.groups = groups;
    }

}
