package ua.in.quireg.foursquareapp.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ua.in.quireg.foursquareapp.R;

/**
 * Created by Arcturus Mengsk on 1/27/2018, 12:35 AM.
 * foursquareapp
 */

public class MapViewFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_fragment, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(mMap -> {
            googleMap = mMap;

            mMap.addMarker(new MarkerOptions().position(mMap.getCameraPosition().target));
            mMap.addCircle(new CircleOptions().center(mMap.getCameraPosition().target).radius(mMap.getMaxZoomLevel()));

            mMap.setOnCameraMoveListener(() -> {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(mMap.getCameraPosition().target));
                mMap.addCircle(new CircleOptions().center(mMap.getCameraPosition().target));
            });



            // For showing a move to my location button
            googleMap.setMyLocationEnabled(true);

            // For dropping a marker at a point on the Map
            LatLng sydney = new LatLng(-34, 151);

//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description")).setDraggable(false);

            // For zooming automatically to the location of the marker
            CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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