package ua.in.quireg.foursquareapp.models;

import android.net.Uri;

/**
 * Created by Arcturus Mengsk on 1/29/2018, 9:23 PM.
 * foursquareapp
 */

public class TipEntity {

    private String tipText;
    private String authorName;
    private String likes;
    private Uri authorImage;

    public String getTipText() {
        return tipText;
    }

    public void setTipText(String tipText) {
        this.tipText = tipText;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public Uri getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(Uri authorImage) {
        this.authorImage = authorImage;
    }
}
