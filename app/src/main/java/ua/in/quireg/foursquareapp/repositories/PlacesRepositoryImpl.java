package ua.in.quireg.foursquareapp.repositories;

import android.support.v4.util.Pair;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import ua.in.quireg.foursquareapp.repositories.api_models.search_venues.Venue;
import ua.in.quireg.foursquareapp.repositories.api_models.single_venue.VenueExtended;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 10:03 PM.
 * foursquareapp
 */

public class PlacesRepositoryImpl implements PlacesRepository {

    private Retrofit mRetrofit;

    public PlacesRepositoryImpl(Retrofit retrofit) {
        mRetrofit = retrofit;

    }

    @Override
    public Observable<List<Pair<Venue, VenueExtended>>> getPlaces() {

        FoursquareApi foursquareApi = mRetrofit.create(FoursquareApi.class);

        return foursquareApi.executeSearchNearbyPlacesQuery("50.450100,30.523400", "browse", "200", "50")
                .flatMap(respond ->
                        Observable.fromIterable(respond.getResponse().getVenues())
                                .flatMap(venue ->
                                        foursquareApi
                                                .executeObtainPlaceInfo(venue.getId())
                                                .map(singleVenueRespond -> singleVenueRespond.getResponse().getVenue())
                                                .map(venueExtended -> new Pair<>(venue, venueExtended)))
                                .toList()
                                .toObservable()
                );
    }
}
