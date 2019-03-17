package ua.in.quireg.foursquareapp.repositories;

import ua.in.quireg.foursquareapp.models.LocationEntity;

/**
 * Created by Arcturus Mengsk on 1/21/2018, 9:23 AM.
 * foursquareapp
 */

public interface PersistentStorage {

    LocationEntity getLocationFromCache();

    void addLocationToCache(LocationEntity e);

    int getAreaFromCache();
}
