package ua.in.quireg.foursquareapp.repositories;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ua.in.quireg.foursquareapp.repositories.api_models.search_venues.PlacesNearbyRespond;
import ua.in.quireg.foursquareapp.repositories.api_models.single_venue.SingleVenueRespond;
import ua.in.quireg.foursquareapp.repositories.api_models.venue_photos.VenuePhotosRespond;
import ua.in.quireg.foursquareapp.repositories.api_models.venue_tips.VenueTipsRespond;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 6:46 PM.
 * foursquareapp
 */

public interface FoursquareApi {

    @GET("venues/search")
    Observable<PlacesNearbyRespond> executeSearchNearbyPlacesQuery(
            @Query("ll") String latLonCommaSeparated, @Query("query") String query,
            @Query("intent") String intent, @Query("radius") String radius,
            @Query("limit") String limit);

    @GET("venues/{venueId}")
    Observable<SingleVenueRespond> executeObtainPlaceInfo(@Path("venueId") String venueId);

    @GET("venues/{venueId}/photos")
    Observable<VenuePhotosRespond> executeObtainPlacePhotos(@Path("venueId") String venueId);

    @GET("venues/{venueId}/tips")
    Observable<VenueTipsRespond> executeObtainPlaceTips(@Path("venueId") String venueId,
                                                        @Query("offset") String offset,
                                                        @Query("limit") String limit);
}
