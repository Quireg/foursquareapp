
package ua.in.quireg.foursquareapp.repositories.api_models.single_venue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item_______ {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("createdAt")
    @Expose
    private Integer createdAt;
    @SerializedName("tip")
    @Expose
    private Tip tip;
    @SerializedName("photo")
    @Expose
    private Photo_________ photo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    public Tip getTip() {
        return tip;
    }

    public void setTip(Tip tip) {
        this.tip = tip;
    }

    public Photo_________ getPhoto() {
        return photo;
    }

    public void setPhoto(Photo_________ photo) {
        this.photo = photo;
    }

}
