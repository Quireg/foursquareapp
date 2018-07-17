package ua.in.quireg.foursquareapp.common;

import android.annotation.SuppressLint;
import android.app.Application;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Vector;

import timber.log.Timber;

/**
 * Created by Arcturus Mengsk on 1/24/2018, 2:47 AM.
 * foursquareapp
 */

public class LocRepositoryGmsImpl implements LocRepository, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;

    private Vector<LocationListener> mLocationlisteners = new Vector<>();

    public LocRepositoryGmsImpl(Application app) {

        mGoogleApiClient = new GoogleApiClient.Builder(app.getApplicationContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void subscribeToLocUpdates(LocationListener l) {

        synchronized (this) {
            if (mLocationlisteners.contains(l)) {
                Timber.w("This subscribe already receiving loc updates!");
                return;
            }
            mLocationlisteners.add(l);
        }

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

    }

    @Override
    public void unsubscribeFromLocUpdates(LocationListener listener) {
        synchronized (this) {
            mLocationlisteners.remove(listener);

            if (mLocationlisteners.size() == 0) {
                mGoogleApiClient.disconnect();
            }
        }
    }

    @Override
    @SuppressLint("MissingPermission")
    public void onConnected(Bundle bundle) {

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Timber.e("GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.e("GoogleApiClient connection has failed: %s", connectionResult.getErrorMessage());
    }

    @Override
    public void onLocationChanged(Location location) {
        synchronized (this) {
            for (LocationListener locationListener : mLocationlisteners) {
                locationListener.onLocationChanged(location);
            }
        }
    }

}
