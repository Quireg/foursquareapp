package ua.in.quireg.foursquareapp.repositories;

import android.content.SharedPreferences;

import ua.in.quireg.foursquareapp.models.LocationEntity;

/**
 * Created by Arcturus Mengsk on 1/22/2018, 7:52 PM.
 * foursquareapp
 */

public class PersistentStorageImpl implements PersistentStorage {

    private static final String PREFS_PERSIST_LOC_LAT = "loc_lat_persist";
    private static final String PREFS_PERSIST_LOC_LON = "loc_lon_persist";
    private static final String PREFS_PERSIST_LOC_ADDRESS = "loc_address_persist";
    private static final String PREFS_PERSIST_RADIUS = "radius_persist";

    private SharedPreferences mSharedPreferences;

    public PersistentStorageImpl(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    @Override
    public LocationEntity getLocationFromCache() {

        LocationEntity locationEntity;

        long latitudeLongBits = mSharedPreferences.getLong(PREFS_PERSIST_LOC_LAT, -1);
        long longitudeLongBits = mSharedPreferences.getLong(PREFS_PERSIST_LOC_LON, -1);
        String address = mSharedPreferences.getString(PREFS_PERSIST_LOC_ADDRESS, null);

        if (latitudeLongBits != -1 && longitudeLongBits != -1) {

            locationEntity = new LocationEntity(
                    Double.longBitsToDouble(latitudeLongBits),
                    Double.longBitsToDouble(longitudeLongBits)
            );

            locationEntity.setAddress(address);
        } else {
            locationEntity = new LocationEntity(50.442326, 30.521137);
        }

        return locationEntity;
    }

    @Override
    public void addLocationToCache(LocationEntity location) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putLong(PREFS_PERSIST_LOC_LAT,  Double.doubleToLongBits(location.getLat()))
                .putLong(PREFS_PERSIST_LOC_LON, Double.doubleToLongBits(location.getLon()));

        if(location.getAddress() != null) {
            editor.putString(PREFS_PERSIST_LOC_ADDRESS, location.getAddress());
        }

        editor.apply();
    }

    @Override
    public String getRadiusFromCache() {
        return String.valueOf(mSharedPreferences.getInt(PREFS_PERSIST_RADIUS, 10000));
    }

    @Override
    public void addRadiusToCache(String r) {
        mSharedPreferences.edit().putInt(PREFS_PERSIST_RADIUS, Integer.parseInt(r)).apply();
    }
}
