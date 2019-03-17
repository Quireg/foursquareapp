package ua.in.quireg.foursquareapp.repositories.api_models.single_venue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Listed {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("groups")
    @Expose
    private List<Group______> groups = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Group______> getGroups() {
        return groups;
    }

    public void setGroups(List<Group______> groups) {
        this.groups = groups;
    }

}
