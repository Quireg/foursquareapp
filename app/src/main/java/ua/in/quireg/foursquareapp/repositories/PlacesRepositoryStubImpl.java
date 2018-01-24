package ua.in.quireg.foursquareapp.repositories;

import android.content.Context;
import android.support.v4.util.Pair;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.common.ResourceManager;
import ua.in.quireg.foursquareapp.common.Utils;
import ua.in.quireg.foursquareapp.repositories.api_models.search_venues.PlacesNearbyRespond;
import ua.in.quireg.foursquareapp.repositories.api_models.search_venues.Venue;
import ua.in.quireg.foursquareapp.repositories.api_models.single_venue.SingleVenueRespond;
import ua.in.quireg.foursquareapp.repositories.api_models.single_venue.VenueExtended;

/**
 * Created by Arcturus Mengsk on 1/20/2018, 9:37 AM.
 * foursquareapp
 */

public class PlacesRepositoryStubImpl implements PlacesRepository {

    private ResourceManager mManager;

    public PlacesRepositoryStubImpl(ResourceManager manager) {
        mManager = manager;
    }

    @Override
    public Observable<Pair<Venue, VenueExtended>> getPlaces(String latLonCommaSeparated, String radius, String limit) {
        ArrayList<Pair<Venue, VenueExtended>> arrayList = new ArrayList<>();

        try {
            String venues_search = Utils.convertStreamToString(mManager.getFileInputStreamFromAssets("venues_search.json"));
            String single_venue = Utils.convertStreamToString(mManager.getFileInputStreamFromAssets("single_venue.json"));

            PlacesNearbyRespond placesNearbyRespond = new Gson().fromJson(venues_search, PlacesNearbyRespond.class);
            SingleVenueRespond singleVenueRespond = new Gson().fromJson(single_venue, SingleVenueRespond.class);

            for (int i = 0; i < placesNearbyRespond.getResponse().getVenues().size(); i++) {
                arrayList.add(new Pair<>(placesNearbyRespond.getResponse().getVenues().get(i), singleVenueRespond.getResponse().getVenue()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Observable.fromIterable(arrayList);
    }
}
