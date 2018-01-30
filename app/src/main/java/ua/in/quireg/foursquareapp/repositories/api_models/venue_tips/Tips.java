
package ua.in.quireg.foursquareapp.repositories.api_models.venue_tips;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tips {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("items")
    @Expose
    private List<Item_> items = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Item_> getItems() {
        return items;
    }

    public void setItems(List<Item_> items) {
        this.items = items;
    }

}
