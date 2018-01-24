package ua.in.quireg.foursquareapp.domain;

import android.support.v4.util.Pair;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.models.PlaceEntity;
import ua.in.quireg.foursquareapp.repositories.PlacesRepository;
import ua.in.quireg.foursquareapp.repositories.api_models.search_venues.Venue;
import ua.in.quireg.foursquareapp.repositories.api_models.single_venue.VenueExtended;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 9:40 PM.
 * foursquareapp
 */

public class NearbyPlacesInteractor {

    @Inject @Named("release") PlacesRepository mPlacesRepository;

    public NearbyPlacesInteractor() {
        FoursquareApplication.getAppComponent().inject(this);
    }

    public Observable<PlaceEntity> getNearbyPlaces(String latLonCommaSeparated, String query, String radius, String limit) {

        return mPlacesRepository.getPlaces(latLonCommaSeparated, query, radius, limit)
                .map(NearbyPlacesInteractor::mapEntities);
    }

    private static PlaceEntity mapEntities(Pair<Venue, VenueExtended> pair) {

        PlaceEntity placeEntity = new PlaceEntity();

        Venue venue = pair.first;
        VenueExtended venueExtended = pair.second;

        if (venue == null || venueExtended == null) {
            throw new RuntimeException("received null response from api");
        }

        if (venue.getName() != null) {
            placeEntity.setName(venue.getName());
        }
        if (venue.getLocation() != null && venue.getLocation().getDistance() != null) {
            placeEntity.setDistanceTo(venue.getLocation().getDistance().toString());
        }
        if (venue.getCategories() != null && !venue.getCategories().isEmpty() && venue.getCategories().get(0).getName() != null) {
            placeEntity.setType(venue.getCategories().get(0).getName());
        }
        if (venueExtended.getRating() != null) {
            placeEntity.setRating(String.valueOf(venueExtended.getRating()));
        }
        if (venueExtended.getRatingColor() != null) {
            placeEntity.setRatingColor("#".concat(venueExtended.getRatingColor()));
        }
        if (venueExtended.getPrice() != null && venueExtended.getPrice().getTier() != null) {
            placeEntity.setPriceCategory(new String(new char[venueExtended.getPrice().getTier()]).replace("\0", "$"));
        }
        if (venueExtended.getLocation() != null && venueExtended.getLocation().getAddress() != null) {
            placeEntity.setAddress(venueExtended.getLocation().getAddress());
        }

        if (venueExtended.getBestPhoto() != null) {
            placeEntity.setImageUri(venueExtended.getBestPhoto().getPrefix(), venueExtended.getBestPhoto().getSuffix());
        }
        return placeEntity;
    }
}
