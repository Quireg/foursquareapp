package ua.in.quireg.foursquareapp.common;

import android.annotation.SuppressLint;
import android.location.LocationManager;

import com.google.android.gms.location.LocationListener;

import ua.in.quireg.foursquareapp.FoursquareApplication;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Arcturus Mengsk on 1/23/2018, 7:00 PM.
 * foursquareapp
 */

public class LocRepositoryImpl implements LocRepository {

    private FoursquareApplication mFoursquareApplication;

    public LocRepositoryImpl(FoursquareApplication foursquareApplication) {
        this.mFoursquareApplication = foursquareApplication;
    }

    @Override
    public void subscribeToLocUpdates(LocationListener l) {

    }

    @Override
    @SuppressLint("MissingPermission")
    public void unsubscribeFromLocUpdates(LocationListener listener) {

        LocationManager locationManager = (LocationManager) mFoursquareApplication.getSystemService(LOCATION_SERVICE);

        if (locationManager != null) {
//            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, listener, Looper.getMainLooper());
        }

    }

}
