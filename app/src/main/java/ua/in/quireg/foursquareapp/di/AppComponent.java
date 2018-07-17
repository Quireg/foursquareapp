package ua.in.quireg.foursquareapp.di;

import javax.inject.Singleton;

import dagger.Component;
import ua.in.quireg.foursquareapp.domain.PlacesListInteractor;
import ua.in.quireg.foursquareapp.domain.PlaceDetailsInteractor;
import ua.in.quireg.foursquareapp.mvp.presenters.FilterScreenPresenter;
import ua.in.quireg.foursquareapp.mvp.presenters.PlaceDetailsPresenter;
import ua.in.quireg.foursquareapp.mvp.presenters.PlacesListPresenter;
import ua.in.quireg.foursquareapp.mvp.presenters.WelcomeScreenPresenter;
import ua.in.quireg.foursquareapp.repositories.PlacesRepositoryImpl;
import ua.in.quireg.foursquareapp.ui.activities.MainActivity;
import ua.in.quireg.foursquareapp.ui.adapters.PlacesListRecyclerViewAdapter;
import ua.in.quireg.foursquareapp.ui.fragments.MapViewFragment;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 3:51 PM.
 * foursquareapp
 */

@Singleton
@Component(modules = {BaseModule.class, NavigationModule.class, OkHttpModule.class, RetrofitModule.class, RepositoryModule.class})
public interface AppComponent {

    void inject(MainActivity activity);
    void inject(PlacesRepositoryImpl i);
    void inject(PlacesListPresenter i);
    void inject(PlacesListInteractor i);
    void inject(FilterScreenPresenter i);
    void inject(PlacesListRecyclerViewAdapter i);
    void inject(PlaceDetailsInteractor i);
    void inject(PlaceDetailsPresenter i);
    void inject(MapViewFragment i);
    void inject(WelcomeScreenPresenter i);

}
