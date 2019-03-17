package ua.in.quireg.foursquareapp.repositories.api_models.venue_tips;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item_ {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("createdAt")
    @Expose
    private Integer createdAt;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("canonicalUrl")
    @Expose
    private String canonicalUrl;
    @SerializedName("photo")
    @Expose
    private Photo photo;
    @SerializedName("photourl")
    @Expose
    private String photourl;
    @SerializedName("likes")
    @Expose
    private Likes likes;
    @SerializedName("like")
    @Expose
    private Boolean like;
    @SerializedName("logView")
    @Expose
    private Boolean logView;
    @SerializedName("agreeCount")
    @Expose
    private Integer agreeCount;
    @SerializedName("disagreeCount")
    @Expose
    private Integer disagreeCount;
    @SerializedName("todo")
    @Expose
    private Todo todo;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("authorInteractionType")
    @Expose
    private String authorInteractionType;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("editedAt")
    @Expose
    private Integer editedAt;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCanonicalUrl() {
        return canonicalUrl;
    }

    public void setCanonicalUrl(String canonicalUrl) {
        this.canonicalUrl = canonicalUrl;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public Likes getLikes() {
        return likes;
    }

    public void setLikes(Likes likes) {
        this.likes = likes;
    }

    public Boolean getLike() {
        return like;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }

    public Boolean getLogView() {
        return logView;
    }

    public void setLogView(Boolean logView) {
        this.logView = logView;
    }

    public Integer getAgreeCount() {
        return agreeCount;
    }

    public void setAgreeCount(Integer agreeCount) {
        this.agreeCount = agreeCount;
    }

    public Integer getDisagreeCount() {
        return disagreeCount;
    }

    public void setDisagreeCount(Integer disagreeCount) {
        this.disagreeCount = disagreeCount;
    }

    public Todo getTodo() {
        return todo;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAuthorInteractionType() {
        return authorInteractionType;
    }

    public void setAuthorInteractionType(String authorInteractionType) {
        this.authorInteractionType = authorInteractionType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(Integer editedAt) {
        this.editedAt = editedAt;
    }

}
