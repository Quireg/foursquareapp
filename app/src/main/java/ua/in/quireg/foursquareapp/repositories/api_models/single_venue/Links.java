
package ua.in.quireg.foursquareapp.repositories.api_models.single_venue;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("items")
    @Expose
    private List<Item___> items = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Item___> getItems() {
        return items;
    }

    public void setItems(List<Item___> items) {
        this.items = items;
    }

}
