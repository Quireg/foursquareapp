package ua.in.quireg.foursquareapp.domain;

import android.net.Uri;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ua.in.quireg.foursquareapp.mvp.models.presentation.PlaceEntity;
import ua.in.quireg.foursquareapp.repositories.PlacesRepository;
import ua.in.quireg.foursquareapp.repositories.PlacesRepositoryImpl;
import ua.in.quireg.foursquareapp.repositories.PlacesRepositoryStubImpl;
import ua.in.quireg.foursquareapp.repositories.api_models.search_venues.Venue;
import ua.in.quireg.foursquareapp.repositories.api_models.single_venue.VenueExtended;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 9:40 PM.
 * foursquareapp
 */

public class NearbyPlacesInteractor {

    private static final String PREFFERED_IMAGE_SIZE = "100x100";

    private PlacesRepository placesRepository = new PlacesRepositoryStubImpl();

    public Observable<List<PlaceEntity>> getNearbyPlaces() {

        return placesRepository.getPlaces()
                .subscribeOn(Schedulers.io())
                .map(this::mapEntitiesList);
    }

    private List<PlaceEntity> mapEntitiesList(List<Pair<Venue, VenueExtended>> venues) {
        ArrayList<PlaceEntity> placeEntityArrayList = new ArrayList<>();

        for (Pair<Venue, VenueExtended> v : venues) {
            placeEntityArrayList.add(mapEntities(v));
        }
        return placeEntityArrayList;
    }

    private PlaceEntity mapEntities(Pair<Venue, VenueExtended> pair) {

        PlaceEntity placeEntity = new PlaceEntity();

        Venue venue = pair.first;
        VenueExtended venueExtended = pair.second;

        if (venue.getName() != null) {
            placeEntity.setName(venue.getName());
        }
        if (venue.getLocation() != null && venue.getLocation().getDistance() != null) {
            placeEntity.setDistanceTo(venue.getLocation().getDistance().toString());
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
        if (venue.getLocation() != null && venueExtended.getLocation().getAddress() != null) {
            placeEntity.setAddress(venueExtended.getLocation().getAddress());
        }
        if (venue.getCategories() != null && !venue.getCategories().isEmpty() && venue.getCategories().get(0).getName() != null) {
            placeEntity.setType(venue.getCategories().get(0).getName());
        }
        if (venueExtended.getBestPhoto() != null) {
            placeEntity.setImage(
                    Uri.parse(String.format("%s%s%s", venueExtended.getBestPhoto().getPrefix(), PREFFERED_IMAGE_SIZE, venueExtended.getBestPhoto().getSuffix()))
            );
        }
        return placeEntity;
    }
}
