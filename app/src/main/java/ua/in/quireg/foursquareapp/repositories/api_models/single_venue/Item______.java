package ua.in.quireg.foursquareapp.repositories.api_models.single_venue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item______ {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("user")
    @Expose
    private User___ user;
    @SerializedName("editable")
    @Expose
    private Boolean editable;
    @SerializedName("public")
    @Expose
    private Boolean _public;
    @SerializedName("collaborative")
    @Expose
    private Boolean collaborative;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("canonicalUrl")
    @Expose
    private String canonicalUrl;
    @SerializedName("createdAt")
    @Expose
    private Integer createdAt;
    @SerializedName("updatedAt")
    @Expose
    private Integer updatedAt;
    @SerializedName("photo")
    @Expose
    private Photo______ photo;
    @SerializedName("logView")
    @Expose
    private Boolean logView;
    @SerializedName("followers")
    @Expose
    private Followers followers;
    @SerializedName("listItems")
    @Expose
    private ListItems listItems;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User___ getUser() {
        return user;
    }

    public void setUser(User___ user) {
        this.user = user;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getPublic() {
        return _public;
    }

    public void setPublic(Boolean _public) {
        this._public = _public;
    }

    public Boolean getCollaborative() {
        return collaborative;
    }

    public void setCollaborative(Boolean collaborative) {
        this.collaborative = collaborative;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCanonicalUrl() {
        return canonicalUrl;
    }

    public void setCanonicalUrl(String canonicalUrl) {
        this.canonicalUrl = canonicalUrl;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Integer updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Photo______ getPhoto() {
        return photo;
    }

    public void setPhoto(Photo______ photo) {
        this.photo = photo;
    }

    public Boolean getLogView() {
        return logView;
    }

    public void setLogView(Boolean logView) {
        this.logView = logView;
    }

    public Followers getFollowers() {
        return followers;
    }

    public void setFollowers(Followers followers) {
        this.followers = followers;
    }

    public ListItems getListItems() {
        return listItems;
    }

    public void setListItems(ListItems listItems) {
        this.listItems = listItems;
    }

}
