
package ua.in.quireg.foursquareapp.repositories.api_models.single_venue;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Popular {

    @SerializedName("isOpen")
    @Expose
    private Boolean isOpen;
    @SerializedName("isLocalHoliday")
    @Expose
    private Boolean isLocalHoliday;
    @SerializedName("timeframes")
    @Expose
    private List<Timeframe_> timeframes = null;

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Boolean getIsLocalHoliday() {
        return isLocalHoliday;
    }

    public void setIsLocalHoliday(Boolean isLocalHoliday) {
        this.isLocalHoliday = isLocalHoliday;
    }

    public List<Timeframe_> getTimeframes() {
        return timeframes;
    }

    public void setTimeframes(List<Timeframe_> timeframes) {
        this.timeframes = timeframes;
    }

}
