
package ua.in.quireg.foursquareapp.repositories.api_models.single_venue;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tips_ {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("groups")
    @Expose
    private List<Group____> groups = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Group____> getGroups() {
        return groups;
    }

    public void setGroups(List<Group____> groups) {
        this.groups = groups;
    }

}
