package ua.in.quireg.foursquareapp.repositories;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import ua.in.quireg.foursquareapp.models.LocationEntity;
import ua.in.quireg.foursquareapp.repositories.api_models.single_venue.VenueExtended;

/**
 * Created by Arcturus Mengsk on 1/21/2018, 9:23 AM.
 * foursquareapp
 */

public interface PersistentStorage {

    LocationEntity getLocationFromCache();

    void addLocationToCache(LocationEntity e);

    String getRadiusFromCache();

    void addRadiusToCache(String r);


}
