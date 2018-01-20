
package ua.in.quireg.foursquareapp.repositories.api_models.single_venue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item________ {

    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("displayValue")
    @Expose
    private String displayValue;
    @SerializedName("priceTier")
    @Expose
    private Integer priceTier;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    public Integer getPriceTier() {
        return priceTier;
    }

    public void setPriceTier(Integer priceTier) {
        this.priceTier = priceTier;
    }

}
