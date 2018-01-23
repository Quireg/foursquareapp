package ua.in.quireg.foursquareapp.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.common.ResourceManager;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 3:52 PM.
 * foursquareapp
 */

@Module
public class BaseModule {

    private FoursquareApplication mApplication;

    public BaseModule(FoursquareApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    FoursquareApplication providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Context providesApplicationContext() {
        return mApplication.getApplicationContext();
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    ResourceManager providesResourceManager(Context context) {
        return new ResourceManager(context);
    }

}
