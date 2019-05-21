package ua.in.quireg.foursquareapp.domain;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.models.LocationEntity;
import ua.in.quireg.foursquareapp.models.PlaceEntity;
import ua.in.quireg.foursquareapp.models.TipEntity;
import ua.in.quireg.foursquareapp.repositories.PlacesRepository;
import ua.in.quireg.foursquareapp.repositories.api_models.single_venue.VenueExtended;
import ua.in.quireg.foursquareapp.repositories.api_models.venue_photos.Photos;
import ua.in.quireg.foursquareapp.repositories.api_models.venue_tips.Tips;

/**
 * Created by Arcturus Mengsk on 1/29/2018, 8:34 PM.
 * foursquareapp
 */

public class PlaceDetailsInteractor {

    @Inject @Named("release") PlacesRepository mPlacesRepository;

    public PlaceDetailsInteractor() {
        FoursquareApplication.getAppComponent().inject(this);
    }

    public Observable<PlaceEntity> getPlaceDetails(String id) {
        return mPlacesRepository
                .getPlaceDetails(id)
                .map(this::mapVenueEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Uri>> getPlacePhotos(String id) {
        return mPlacesRepository
                .getPlacePhotos(id)
                .map(this::mapPhotos)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<TipEntity> getPlaceTip(String id, String offset) {
        return mPlacesRepository
                .getPlaceTips(id, offset)
                .map(this::mapTips)
                .flatMapIterable(x -> x)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private PlaceEntity mapVenueEntity(VenueExtended venueExtended) {

        PlaceEntity placeEntity = new PlaceEntity();

        if (venueExtended == null) {
            return placeEntity;
        }
        if (venueExtended.getId() != null) {
            placeEntity.setId(venueExtended.getId());
        }
        if (venueExtended.getName() != null) {
            placeEntity.setName(venueExtended.getName());
        }
        if (venueExtended.getCategories() != null
                && !venueExtended.getCategories().isEmpty()
                && venueExtended.getCategories().get(0).getName() != null) {
            placeEntity.setType(venueExtended.getCategories().get(0).getName());
        }
        if (venueExtended.getRating() != null) {
            placeEntity.setRating(String.valueOf(venueExtended.getRating()));
        }
        if (venueExtended.getRatingColor() != null) {
            placeEntity.setRatingColor("#".concat(venueExtended.getRatingColor()));
        }
        if (venueExtended.getPrice() != null && venueExtended.getPrice().getTier() != null) {
            placeEntity.setPriceCategory(
                    new String(new char[venueExtended.getPrice().getTier()])
                            .replace("\0", "$"));
        }
        if (venueExtended.getLocation() != null && venueExtended.getLocation().getAddress() != null) {
            placeEntity.setAddress(venueExtended.getLocation().getAddress());
        }
        if (venueExtended.getBestPhoto() != null) {
            placeEntity.setImageUri(
                    venueExtended.getBestPhoto().getPrefix(),
                    venueExtended.getBestPhoto().getSuffix());
        }
        if (venueExtended.getPage() != null
                && venueExtended.getPage().getPageInfo() != null
                && venueExtended.getPage().getPageInfo().getDescription() != null) {
            placeEntity.setDescription(venueExtended.getPage().getPageInfo().getDescription());
        }
        if (venueExtended.getLocation() != null
                && venueExtended.getLocation().getLat() != null
                && venueExtended.getLocation().getLng() != null) {
            placeEntity.setLocationEntity(new LocationEntity(
                    venueExtended.getLocation().getLat(),
                    venueExtended.getLocation().getLng()));
        }
        return placeEntity;
    }

    private List<Uri> mapPhotos(Photos photos) {
        List<Uri> photoUris = new ArrayList<>();

        for (int i = 0; i < photos.getItems().size(); i++) {
            photoUris.add(
                    Uri.parse(photos.getItems().get(i).getPrefix()
                            + "300x300" + photos.getItems().get(i).getSuffix()));
        }
        return photoUris;
    }

    private List<TipEntity> mapTips(Tips tips) {
        ArrayList<TipEntity> tipEntities = new ArrayList<>();

        for (int i = 0; i < tips.getItems().size(); i++) {
            try {
                TipEntity tipEntity = new TipEntity();

                tipEntity.setTipText(tips.getItems().get(i).getText());

                if (tips.getItems().get(i).getUser().getLastName() == null) {
                    tipEntity.setAuthorName(tips.getItems().get(i).getUser().getFirstName());
                } else {
                    tipEntity.setAuthorName(String.format(
                            "%s, %s",
                            tips.getItems().get(i).getUser().getFirstName(),
                            tips.getItems().get(i).getUser().getLastName())
                    );
                }

                tipEntity.setLikes(tips.getItems().get(i).getLikes().getCount().toString());

                tipEntity.setAuthorImage(Uri.parse(
                        tips.getItems().get(i).getUser().getPhoto().getPrefix() +
                                "100x100" +
                                tips.getItems().get(i).getUser().getPhoto().getSuffix()
                ));
                tipEntities.add(tipEntity);
            } catch (Exception e) {
                Timber.e(e);
            }
        }
        return tipEntities;
    }
}
