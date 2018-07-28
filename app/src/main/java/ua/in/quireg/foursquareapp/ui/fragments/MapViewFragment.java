package ua.in.quireg.foursquareapp.ui.fragments;

import android.app.Fragment;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import timber.log.Timber;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.common.QueryFilter;
import ua.in.quireg.foursquareapp.models.LocationEntity;
import ua.in.quireg.foursquareapp.repositories.PersistentStorage;

/**
 * Created by Arcturus Mengsk on 1/27/2018, 12:35 AM.
 * foursquareapp
 */

public class MapViewFragment extends Fragment {

    private MapView mMapView;
    private ImageView mMapCircle;
    private SeekBar mSeekBar;
    private TextView mCurrentArea;
    @Inject
    protected PersistentStorage mPersistentStorage;
    @Inject
    protected QueryFilter mQueryFilter;
    protected Geocoder mGeocoder;
    protected LatLng mCurrentLocation;
    protected GoogleMap mMap;
    ;

    private static final int DIAMETER_LOW = 500;
    private static final int DIAMETER_HIGH = 100000;
    private static final int SEEKBAR_MAX_PROGRESS = 100;
    private static final double EQUATORIAL_CIRCUMFERENCE_OF_THE_EARTH = 40075016.686;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FoursquareApplication.getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_fragment, container, false);

        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            getActivity().getActionBar().setTitle(R.string.location_picker_screen_title);
        }

        mMapView = rootView.findViewById(R.id.mapView);
        mMapCircle = rootView.findViewById(R.id.map_circle);
        mSeekBar = rootView.findViewById(R.id.seekBar);
        mCurrentArea = rootView.findViewById(R.id.current_radius);
        mGeocoder = new Geocoder(getActivity(), Locale.getDefault());
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map displayed immediately

        if (mQueryFilter.getLocation() != null) {
            mCurrentLocation = new LatLng(
                    mQueryFilter.getLocation().getLat(),
                    mQueryFilter.getLocation().getLon()
            );
        } else {
            mCurrentLocation = new LatLng(
                    mPersistentStorage.getLocationFromCache().getLat(),
                    mPersistentStorage.getLocationFromCache().getLon()
            );
        }

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(map -> {
            mMap = map;

            final float requiredZoomLevel = calcZoomLevelForDistance((DIAMETER_HIGH - DIAMETER_LOW) / 2);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(mCurrentLocation).zoom(requiredZoomLevel).build();

            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mMap.getUiSettings().setZoomGesturesEnabled(false);

            mCurrentArea.setText(String.format(Locale.getDefault(), "%dm", (DIAMETER_HIGH - DIAMETER_LOW) / 2));

            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    int effectiveDiameter;
                    if (progress == 0) {
                        effectiveDiameter = DIAMETER_LOW;
                    } else {
                        int step = (DIAMETER_HIGH - DIAMETER_LOW) / SEEKBAR_MAX_PROGRESS;
                        effectiveDiameter = DIAMETER_LOW + (step * progress);
                    }
                    mQueryFilter.setSearchArea(effectiveDiameter);
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(calcZoomLevelForDistance(effectiveDiameter)));
                    mCurrentArea.setText(String.format(Locale.getDefault(), "%dm", effectiveDiameter));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        });
        return rootView;
    }

    private float calcZoomLevelForDistance(int distance) {

        double pixelSizeInMeters = (EQUATORIAL_CIRCUMFERENCE_OF_THE_EARTH * Math.cos(mPersistentStorage.getLocationFromCache().getLat()))
                / distance * mMapCircle.getWidth();

        float requiredZoomLevel = (float) (Math.log(pixelSizeInMeters) / Math.log(2) - 8);
        Timber.d("Calculated zoom level: %f for distance: %d", requiredZoomLevel, distance);

        return requiredZoomLevel;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        LocationEntity locationEntity = new LocationEntity(
                mMap.getCameraPosition().target.latitude,
                mMap.getCameraPosition().target.longitude
        );
        try {
            List<Address> addresses = mGeocoder.getFromLocation(
                    mMap.getCameraPosition().target.latitude,
                    mMap.getCameraPosition().target.longitude,
                    1
            );
            if (addresses != null && addresses.get(0) != null) {
                Address obj = addresses.get(0);
                if (obj != null) {
                    locationEntity.setAddress(obj.getAddressLine(0));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mQueryFilter.setLocation(locationEntity);
        }

        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}