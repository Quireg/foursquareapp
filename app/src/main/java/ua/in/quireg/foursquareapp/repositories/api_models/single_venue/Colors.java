
package ua.in.quireg.foursquareapp.repositories.api_models.single_venue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Colors {

    @SerializedName("highlightColor")
    @Expose
    private HighlightColor highlightColor;
    @SerializedName("highlightTextColor")
    @Expose
    private HighlightTextColor highlightTextColor;
    @SerializedName("algoVersion")
    @Expose
    private Integer algoVersion;

    public HighlightColor getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(HighlightColor highlightColor) {
        this.highlightColor = highlightColor;
    }

    public HighlightTextColor getHighlightTextColor() {
        return highlightTextColor;
    }

    public void setHighlightTextColor(HighlightTextColor highlightTextColor) {
        this.highlightTextColor = highlightTextColor;
    }

    public Integer getAlgoVersion() {
        return algoVersion;
    }

    public void setAlgoVersion(Integer algoVersion) {
        this.algoVersion = algoVersion;
    }

}
