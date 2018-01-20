
package ua.in.quireg.foursquareapp.repositories.api_models.single_venue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Page {

    @SerializedName("pageInfo")
    @Expose
    private PageInfo pageInfo;
    @SerializedName("user")
    @Expose
    private User_ user;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public User_ getUser() {
        return user;
    }

    public void setUser(User_ user) {
        this.user = user;
    }

}
