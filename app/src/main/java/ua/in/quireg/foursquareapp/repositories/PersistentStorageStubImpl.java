package ua.in.quireg.foursquareapp.repositories;

import java.io.FileNotFoundException;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import ua.in.quireg.foursquareapp.repositories.api_models.single_venue.VenueExtended;

/**
 * Created by Arcturus Mengsk on 1/22/2018, 7:52 PM.
 * foursquareapp
 */

public class PersistentStorageStubImpl implements PersistentStorage {

    @Override
    public Single<VenueExtended> getVenueFromCache(String id) {
        return Single.error(new FileNotFoundException());
    }

    @Override
    public Completable addVenueToCache(VenueExtended venue) {
        return Completable.error(new FileNotFoundException());
    }
}
