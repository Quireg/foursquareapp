package ua.in.quireg.foursquareapp.di;

import javax.inject.Singleton;

import dagger.Component;
import ua.in.quireg.foursquareapp.repositories.PlacesRepositoryImpl;
import ua.in.quireg.foursquareapp.repositories.PlacesRepositoryStubImpl;
import ua.in.quireg.foursquareapp.ui.activities.MainActivity;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 3:51 PM.
 * foursquareapp
 */

@Singleton
@Component(modules = {BaseModule.class, NavigationModule.class, OkHttpModule.class, RetrofitModule.class})
public interface AppComponent {

    void inject(MainActivity activity);

    void inject(PlacesRepositoryImpl i);
    void inject(PlacesRepositoryStubImpl i);

}
