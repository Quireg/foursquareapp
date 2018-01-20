
package ua.in.quireg.foursquareapp.repositories.api_models.single_venue;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RichStatus {

    @SerializedName("entities")
    @Expose
    private List<Object> entities = null;
    @SerializedName("text")
    @Expose
    private String text;

    public List<Object> getEntities() {
        return entities;
    }

    public void setEntities(List<Object> entities) {
        this.entities = entities;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
