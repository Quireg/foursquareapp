package ua.in.quireg.foursquareapp.repositories.api_models.single_venue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Attributes {

    @SerializedName("groups")
    @Expose
    private List<Group________> groups = null;

    public List<Group________> getGroups() {
        return groups;
    }

    public void setGroups(List<Group________> groups) {
        this.groups = groups;
    }

}
