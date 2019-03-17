package ua.in.quireg.foursquareapp.repositories.api_models.single_venue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User_ {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("photo")
    @Expose
    private Photo_ photo;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("tips")
    @Expose
    private Tips tips;
    @SerializedName("lists")
    @Expose
    private Lists lists;
    @SerializedName("homeCity")
    @Expose
    private String homeCity;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("contact")
    @Expose
    private Contact_ contact;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Photo_ getPhoto() {
        return photo;
    }

    public void setPhoto(Photo_ photo) {
        this.photo = photo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Tips getTips() {
        return tips;
    }

    public void setTips(Tips tips) {
        this.tips = tips;
    }

    public Lists getLists() {
        return lists;
    }

    public void setLists(Lists lists) {
        this.lists = lists;
    }

    public String getHomeCity() {
        return homeCity;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Contact_ getContact() {
        return contact;
    }

    public void setContact(Contact_ contact) {
        this.contact = contact;
    }

}
