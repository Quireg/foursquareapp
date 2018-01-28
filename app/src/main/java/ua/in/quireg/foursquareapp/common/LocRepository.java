package ua.in.quireg.foursquareapp.common;

import android.location.Location;

import com.google.android.gms.location.LocationListener;

/**
 * Created by Arcturus Mengsk on 1/23/2018, 12:43 PM.
 * foursquareapp
 */

public interface LocRepository {

    void subscribeToLocUpdates(LocationListener l);

    void unsubscribeFromLocUpdates(LocationListener l);

}
