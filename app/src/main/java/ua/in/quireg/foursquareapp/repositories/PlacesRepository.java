package ua.in.quireg.foursquareapp.repositories;

import android.support.v4.util.Pair;

import io.reactivex.Observable;
import ua.in.quireg.foursquareapp.repositories.api_models.search_venues.Venue;
import ua.in.quireg.foursquareapp.repositories.api_models.single_venue.VenueExtended;
import ua.in.quireg.foursquareapp.repositories.api_models.venue_photos.Photos;
import ua.in.quireg.foursquareapp.repositories.api_models.venue_tips.Tips;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 10:01 PM.
 * foursquareapp
 */

public interface PlacesRepository {

    Observable<Pair<Venue, VenueExtended>> getPlaces(String latLonCommaSeparated, String query,
                                                     String radius, String limit);

    Observable<VenueExtended> getPlaceDetails(String id);

    Observable<Photos> getPlacePhotos(String id);

    Observable<Tips> getPlaceTips(String id, String offset);

}
