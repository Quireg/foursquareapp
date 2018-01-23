package ua.in.quireg.foursquareapp.di;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import ua.in.quireg.foursquareapp.common.ResourceManager;
import ua.in.quireg.foursquareapp.repositories.PersistentStorage;
import ua.in.quireg.foursquareapp.repositories.PersistentStorageStubImpl;
import ua.in.quireg.foursquareapp.repositories.PlacesRepository;
import ua.in.quireg.foursquareapp.repositories.PlacesRepositoryImpl;
import ua.in.quireg.foursquareapp.repositories.PlacesRepositoryStubImpl;

/**
 * Created by Arcturus Mengsk on 1/21/2018, 10:35 AM.
 * foursquareapp
 */

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    @Named("stub")
    PlacesRepository placesFakeRepository(ResourceManager c) {
        return new PlacesRepositoryStubImpl(c);
    }

    @Provides
    @Singleton
    @Named("release")
    PlacesRepository placesRepository(Retrofit retrofit) {
        return new PlacesRepositoryImpl(retrofit);
    }

    @Provides
    @Singleton
    @Named("stub")
    PersistentStorage persistentStubStorage() {
        return new PersistentStorageStubImpl();
    }
}
