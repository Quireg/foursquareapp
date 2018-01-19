package ua.in.quireg.foursquareapp.repositories;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ua.in.quireg.foursquareapp.mvp.models.domain.PlacesNearbyRespond;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 6:46 PM.
 * foursquareapp
 */

public interface ForsquareApi {

    String intent = "browse";
    String radius = "20";

    @GET("venues/search")
    Observable<PlacesNearbyRespond> executeSearchNearbyPlacesQuery(@Query("ll") String latLonCommaSeparated, @Query("intent") String intent, @Query("radius") String radius);
}
