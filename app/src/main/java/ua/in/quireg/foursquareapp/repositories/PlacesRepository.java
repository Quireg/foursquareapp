package ua.in.quireg.foursquareapp.repositories;

import android.support.v4.util.Pair;

import java.util.List;

import io.reactivex.Observable;
import ua.in.quireg.foursquareapp.repositories.api_models.search_venues.Venue;
import ua.in.quireg.foursquareapp.repositories.api_models.single_venue.VenueExtended;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 10:01 PM.
 * foursquareapp
 */

public interface PlacesRepository {

    Observable<Pair<Venue, VenueExtended>> getPlaces();

}
