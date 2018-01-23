package ua.in.quireg.foursquareapp;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;
import ua.in.quireg.foursquareapp.di.AppComponent;
import ua.in.quireg.foursquareapp.di.BaseModule;
import ua.in.quireg.foursquareapp.di.DaggerAppComponent;
import ua.in.quireg.foursquareapp.di.NavigationModule;
import ua.in.quireg.foursquareapp.di.OkHttpModule;
import ua.in.quireg.foursquareapp.di.RepositoryModule;
import ua.in.quireg.foursquareapp.di.RetrofitModule;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 3:39 PM.
 * foursquareapp
 */

public class FoursquareApplication extends Application {

    private static AppComponent mComponent;

    public static AppComponent getAppComponent() {
        return mComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            return;
        }

        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
            Timber.plant(new Timber.DebugTree());
        }

        mComponent = buildComponent();
    }

    private AppComponent buildComponent() {
        return DaggerAppComponent.builder()
                .baseModule(new BaseModule(this))
                .navigationModule(new NavigationModule())
                .okHttpModule(new OkHttpModule())
                .retrofitModule(new RetrofitModule())
                .repositoryModule(new RepositoryModule())
                .build();
    }
}
