package ua.in.quireg.foursquareapp.repositories;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.mvp.models.domain.Venue;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 10:03 PM.
 * foursquareapp
 */

public class NearbyPlacesRepositoryImpl implements NearbyPlacesRepository {

    @Inject Retrofit mRetrofit;

    public NearbyPlacesRepositoryImpl() {
        FoursquareApplication.getAppComponent().inject(this);
    }

    @Override
    public Observable<List<Venue>> getNearbyPlaces() {
        ForsquareApi forsquareApi = mRetrofit.create(ForsquareApi.class);

        return forsquareApi.executeSearchNearbyPlacesQuery("50.450100,30.523400", "browse", "200")
                .map(r -> r.getResponse().getVenues());
    }
}
