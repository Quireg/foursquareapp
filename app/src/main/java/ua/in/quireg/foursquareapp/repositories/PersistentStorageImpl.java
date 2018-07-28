package ua.in.quireg.foursquareapp.repositories;

import android.content.SharedPreferences;

import timber.log.Timber;
import ua.in.quireg.foursquareapp.models.LocationEntity;

/**
 * Created by Arcturus Mengsk on 1/22/2018, 7:52 PM.
 * foursquareapp
 */

public class PersistentStorageImpl implements PersistentStorage {

    private static final String PREFS_PERSIST_LOC_LAT = "loc_lat_persist";
    private static final String PREFS_PERSIST_LOC_LON = "loc_lon_persist";
    private static final String PREFS_PERSIST_LOC_ADDRESS = "loc_address_persist";
    private static final int PERSIST_AREA = 40000;

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
            Timber.i("Got location from cache %s", locationEntity.toString());
        } else {
            locationEntity = new LocationEntity(50.442326, 30.521137);
            locationEntity.setAddress("Wrong neighbourhood");
            Timber.i("Got hardcoded location %s", locationEntity.toString());
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
    public int getAreaFromCache() {
        return PERSIST_AREA;
    }

}
