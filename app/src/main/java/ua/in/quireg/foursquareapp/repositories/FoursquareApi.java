package ua.in.quireg.foursquareapp.repositories;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ua.in.quireg.foursquareapp.repositories.api_models.search_venues.PlacesNearbyRespond;
import ua.in.quireg.foursquareapp.repositories.api_models.single_venue.SingleVenueRespond;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 6:46 PM.
 * foursquareapp
 */

public interface FoursquareApi {

    String intent = "browse";
    String radius = "20";

    @GET("venues/search")
    Observable<PlacesNearbyRespond> executeSearchNearbyPlacesQuery(@Query("ll") String latLonCommaSeparated, @Query("intent") String intent, @Query("radius") String radius, @Query("limit") String limit);

    @GET("venues/{venueId}")
    Observable<SingleVenueRespond> executeObtainPlaceInfo(@Path("venueId") String venueId);
}
