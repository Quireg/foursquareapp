
package ua.in.quireg.foursquareapp.mvp.models.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("unreadCount")
    @Expose
    private Integer unreadCount;

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

}
