
package ua.in.quireg.foursquareapp.repositories.api_models.venue_photos;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photos {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("items")
    @Expose
    private List<Item_> items = null;
    @SerializedName("dupesRemoved")
    @Expose
    private Integer dupesRemoved;

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

    public Integer getDupesRemoved() {
        return dupesRemoved;
    }

    public void setDupesRemoved(Integer dupesRemoved) {
        this.dupesRemoved = dupesRemoved;
    }

}
