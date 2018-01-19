package ua.in.quireg.foursquareapp.domain;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ua.in.quireg.foursquareapp.mvp.models.domain.Venue;
import ua.in.quireg.foursquareapp.mvp.models.presentation.PlaceEntity;
import ua.in.quireg.foursquareapp.repositories.NearbyPlacesRepository;
import ua.in.quireg.foursquareapp.repositories.NearbyPlacesRepositoryImpl;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 9:40 PM.
 * foursquareapp
 */

public class NearbyPlacesInteractor {

    private NearbyPlacesRepository nearbyPlacesRepository = new NearbyPlacesRepositoryImpl();

    public Observable<List<PlaceEntity>> getNearbyPlaces() {

        return nearbyPlacesRepository.getNearbyPlaces()
                .subscribeOn(Schedulers.io())
                .map(this::mapEntitiesList);
    }

    private List<PlaceEntity> mapEntitiesList(List<Venue> venues) {
        ArrayList<PlaceEntity> placeEntityArrayList = new ArrayList<>();

        for (Venue v : venues) {
            placeEntityArrayList.add(mapEntities(v));
        }
        return placeEntityArrayList;
    }

    private PlaceEntity mapEntities(Venue v) {
        PlaceEntity placeEntity = new PlaceEntity();
        placeEntity.setName(v.getName());
        if (v.getCategories() != null && !v.getCategories().isEmpty()) {
            placeEntity.setType(v.getCategories().get(0).getName());
        }
        if (v.getLocation() != null && v.getLocation().getDistance() != null) {
            placeEntity.setDistanceTo(v.getLocation().getDistance().toString());

        }
        placeEntity.setRating(v.getStats().getTipCount().toString());
        placeEntity.setAddress(v.getLocation().getAddress());

        return placeEntity;
    }
}
