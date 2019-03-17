package ua.in.quireg.foursquareapp;

import android.app.Application;
import android.preference.PreferenceManager;

import com.squareup.leakcanary.LeakCanary;

import java.math.BigDecimal;

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

    public static final int RADIUS_LOW = 500; //meters
    public static final int RADIUS_HIGH = 10000; //meters
    public static final int DEFAULT_SEARCH_RADIUS = RADIUS_HIGH / 2;
    public static final BigDecimal DEFAULT_LATITUDE = BigDecimal.valueOf(50.442326);
    public static final BigDecimal DEFAULT_LONGITUDE = BigDecimal.valueOf(30.521137);
    public static final BigDecimal EQUATORIAL_CIRCUMFERENCE = BigDecimal.valueOf(40075016.686);
    public static final String PREF_MOC_LOC_KEY = "pref_mock_loc_key";

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

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isMockLocation() {
        return PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext()).getBoolean(PREF_MOC_LOC_KEY, false);
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
