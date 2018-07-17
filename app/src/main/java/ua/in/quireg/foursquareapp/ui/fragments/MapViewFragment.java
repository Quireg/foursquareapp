package ua.in.quireg.foursquareapp.ui.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.repositories.PersistentStorage;

/**
 * Created by Arcturus Mengsk on 1/27/2018, 12:35 AM.
 * foursquareapp
 */

public class MapViewFragment extends Fragment {

    private MapView mMapView;
    private SeekBar mSeekBar;
    @Inject
    protected PersistentStorage mPersistentStorage;

    private static final int RADIUS_LOW = 500;
    private static final int RADIUS_HIGH = 10000;

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
        mSeekBar = rootView.findViewById(R.id.seekBar);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(mMap -> {

            mMap.getUiSettings().setZoomGesturesEnabled(false);




            LatLng currentLocation = new LatLng(
                    mPersistentStorage.getLocationFromCache().getLat(),
                    mPersistentStorage.getLocationFromCache().getLon()
            );

            Marker marker =  mMap.addMarker(new MarkerOptions().position(mMap.getCameraPosition().target));

            Circle circle = mMap.addCircle(new CircleOptions()
                    .center(mMap.getCameraPosition().target)
                    .radius(1000)
                    .strokeColor(Color.RED));


            CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocation).zoom(12).build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            mMap.setOnCameraMoveListener(() -> {
                marker.setPosition(mMap.getCameraPosition().target);
                circle.setCenter(mMap.getCameraPosition().target);
            });

            float currentZoom = mMap.getCameraPosition().zoom;

            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (progress == 0) {
                        int effectiveRadius = RADIUS_LOW ;
                    } else {
                        int effectiveRadius = RADIUS_LOW + ((RADIUS_HIGH - RADIUS_LOW)/progress);
                    }
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

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
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