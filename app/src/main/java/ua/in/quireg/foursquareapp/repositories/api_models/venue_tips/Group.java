package ua.in.quireg.foursquareapp.repositories.api_models.venue_tips;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Group {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("items")
    @Expose
    private List<Item__> items = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Item__> getItems() {
        return items;
    }

    public void setItems(List<Item__> items) {
        this.items = items;
    }

}
