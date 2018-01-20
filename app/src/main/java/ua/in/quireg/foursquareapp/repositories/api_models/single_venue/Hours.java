
package ua.in.quireg.foursquareapp.repositories.api_models.single_venue;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hours {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("richStatus")
    @Expose
    private RichStatus richStatus;
    @SerializedName("isOpen")
    @Expose
    private Boolean isOpen;
    @SerializedName("isLocalHoliday")
    @Expose
    private Boolean isLocalHoliday;
    @SerializedName("dayData")
    @Expose
    private List<Object> dayData = null;
    @SerializedName("timeframes")
    @Expose
    private List<Timeframe> timeframes = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RichStatus getRichStatus() {
        return richStatus;
    }

    public void setRichStatus(RichStatus richStatus) {
        this.richStatus = richStatus;
    }

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

    public List<Object> getDayData() {
        return dayData;
    }

    public void setDayData(List<Object> dayData) {
        this.dayData = dayData;
    }

    public List<Timeframe> getTimeframes() {
        return timeframes;
    }

    public void setTimeframes(List<Timeframe> timeframes) {
        this.timeframes = timeframes;
    }

}
