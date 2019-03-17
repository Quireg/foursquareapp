package ua.in.quireg.foursquareapp.models;

import java.util.Locale;

/**
 * Created by Arcturus Mengsk on 1/23/2018, 7:36 PM.
 * foursquareapp
 */

public class LocationEntity {

    private double lat;
    private double lon;
    private String address;

    public LocationEntity(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatLonCommaSeparated() {
        return String.format(Locale.getDefault(), "%.4f, %.4f", getLat(), getLon());
    }

    @Override
    public String toString() {
        return "LocationEntity{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", address='" + address + '\'' +
                '}';
    }
}
