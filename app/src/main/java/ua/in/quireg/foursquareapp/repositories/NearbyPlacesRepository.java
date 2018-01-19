package ua.in.quireg.foursquareapp.repositories;

import java.util.List;

import io.reactivex.Observable;
import ua.in.quireg.foursquareapp.mvp.models.domain.Venue;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 10:01 PM.
 * foursquareapp
 */

public interface NearbyPlacesRepository {

    Observable<List<Venue>> getNearbyPlaces();

}
