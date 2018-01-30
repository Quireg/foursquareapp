package ua.in.quireg.foursquareapp.di;

import android.content.SharedPreferences;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.common.LocRepository;
import ua.in.quireg.foursquareapp.common.LocRepositoryGmsImpl;
import ua.in.quireg.foursquareapp.common.QueryFilter;
import ua.in.quireg.foursquareapp.repositories.PersistentStorage;
import ua.in.quireg.foursquareapp.repositories.PersistentStorageImpl;
import ua.in.quireg.foursquareapp.repositories.PlacesRepository;
import ua.in.quireg.foursquareapp.repositories.PlacesRepositoryImpl;

/**
 * Created by Arcturus Mengsk on 1/21/2018, 10:35 AM.
 * foursquareapp
 */

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    @Named("release")
    PlacesRepository placesRepository(Retrofit retrofit) {
        return new PlacesRepositoryImpl(retrofit);
    }

    @Provides
    @Singleton
    LocRepository locRepository(FoursquareApplication foursquareApplication) {
        return new LocRepositoryGmsImpl(foursquareApplication);
    }

    @Provides
    @Singleton
    PersistentStorage persistentStorage(SharedPreferences sharedPreferences) {
        return new PersistentStorageImpl(sharedPreferences);
    }

    @Provides
    @Singleton
    QueryFilter provideQueryFilter(SharedPreferences sharedPreferences) {
        return QueryFilter.buildFromPreferences(sharedPreferences);
    }
}
