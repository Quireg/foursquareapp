package ua.in.quireg.foursquareapp.common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

public class PermissionManager {

    public static final int LOCATION_REQUEST_ID = 1;

    private static String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static boolean verifyPermissions(Context context) {

        boolean granted = true;

        for (String PERMISSION : PERMISSIONS) {
            granted = granted & (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(context, PERMISSION));
        }

        return granted;
    }

    public static void requestPermissions(Activity activity) {

        ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS,
                LOCATION_REQUEST_ID
        );
    }

}
