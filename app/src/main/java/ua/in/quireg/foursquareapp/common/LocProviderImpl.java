package ua.in.quireg.foursquareapp.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Artur Menchenko on 11/5/2017.
 */

public class LocProviderImpl implements LocationProvider{

    private Context mContext;

    public LocProviderImpl(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    @SuppressLint("MissingPermission")
    public Location provideLoc() {
        if (!isGpsOk()) {
            return null;
        }

        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager == null){
            return null;
        }

        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        long GPSLocationTime = 0;
        if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if ( 0 < GPSLocationTime - NetLocationTime ) {
            return locationGPS;
        }
        else {
            return locationNet;
        }
    }

    private boolean isGpsOk(){
        return isGpsPermissionOk() && isGpsEnabled();
    }

    private boolean isGpsPermissionOk(){
        return ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isGpsEnabled() {
        return Settings.Secure
                .isLocationProviderEnabled(mContext.getContentResolver(),
                        LocationManager.GPS_PROVIDER);
    }
}
