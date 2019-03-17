package ua.in.quireg.foursquareapp.repositories;

import android.support.v4.util.Pair;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import ua.in.quireg.foursquareapp.repositories.api_models.search_venues.Venue;
import ua.in.quireg.foursquareapp.repositories.api_models.single_venue.VenueExtended;
import ua.in.quireg.foursquareapp.repositories.api_models.venue_photos.Photos;
import ua.in.quireg.foursquareapp.repositories.api_models.venue_tips.Tips;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 10:03 PM.
 * foursquareapp
 */

public class PlacesRepositoryImpl implements PlacesRepository {

    private static final String TIPS_LIMIT = "5";

    private FoursquareApi mFoursquareApi;

    public PlacesRepositoryImpl(Retrofit retrofit) {
        mFoursquareApi = retrofit.create(FoursquareApi.class);
    }

    @Override
    public Observable<Pair<Venue, VenueExtended>> getPlaces(
            String latLonCommaSeparated, String query, String radius, String limit) {

        return mFoursquareApi
                .executeSearchNearbyPlacesQuery(
                        latLonCommaSeparated, query, "browse", radius, limit)
                .flatMap(respond ->
                        Observable.fromIterable(respond.getResponse().getVenues())
                                .flatMap(venue ->
                                        mFoursquareApi
                                                .executeObtainPlaceInfo(venue.getId())
                                                .map(singleVenueRespond ->
                                                        singleVenueRespond.getResponse().getVenue())
                                                .map(venueExtended ->
                                                        new Pair<>(venue, venueExtended)))
                );
    }

    @Override
    public Observable<VenueExtended> getPlaceDetails(String id) {
        return mFoursquareApi
                .executeObtainPlaceInfo(id)
                .map(singleVenueRespond -> singleVenueRespond.getResponse().getVenue());
    }

    @Override
    public Observable<Photos> getPlacePhotos(String id) {
        return mFoursquareApi
                .executeObtainPlacePhotos(id)
                .map(venuePhotosRespond -> venuePhotosRespond.getResponse().getPhotos());
    }

    @Override
    public Observable<Tips> getPlaceTips(String id, String offset) {

        return mFoursquareApi
                .executeObtainPlaceTips(id, offset, TIPS_LIMIT)
                .map(venueTipsRespond -> venueTipsRespond.getResponse().getTips());
    }
}


