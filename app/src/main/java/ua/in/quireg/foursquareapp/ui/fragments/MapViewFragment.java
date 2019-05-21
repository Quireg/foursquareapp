package ua.in.quireg.foursquareapp.ui.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import timber.log.Timber;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.common.QueryFilter;
import ua.in.quireg.foursquareapp.models.LocationEntity;
import ua.in.quireg.foursquareapp.repositories.PersistentStorage;
import ua.in.quireg.foursquareapp.ui.activities.MainActivity;

import static ua.in.quireg.foursquareapp.FoursquareApplication.DEFAULT_SEARCH_RADIUS;
import static ua.in.quireg.foursquareapp.FoursquareApplication.EQUATORIAL_CIRCUMFERENCE;
import static ua.in.quireg.foursquareapp.FoursquareApplication.RADIUS_HIGH;
import static ua.in.quireg.foursquareapp.FoursquareApplication.RADIUS_LOW;

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

    private int mCurrentAreaRadius;
    private final int MENU_ITEM_ID_DONE = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
                    mQueryFilter.getLocation().getLon());
        } else {
            mCurrentLocation = new LatLng(
                    mPersistentStorage.getLocationFromCache().getLat(),
                    mPersistentStorage.getLocationFromCache().getLon());
        }

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            Timber.e(e);
        }

        mMapView.getMapAsync(map -> {
            mMap = map;
            mCurrentAreaRadius = DEFAULT_SEARCH_RADIUS;

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(mCurrentLocation)
                    .zoom(calcZoomLevelForDistance(mCurrentAreaRadius * 2).floatValue())
                    .build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            int seekBarStepInMeters = (RADIUS_HIGH - RADIUS_LOW) / mSeekBar.getMax();
            int step = (RADIUS_HIGH - RADIUS_LOW) / mSeekBar.getMax();
            mCurrentAreaRadius = RADIUS_LOW + (step * mSeekBar.getProgress());

            mMap.moveCamera(CameraUpdateFactory.zoomTo(
                    calcZoomLevelForDistance(mCurrentAreaRadius * 2).floatValue()));
            updateDistanceIndicator();

            mMap.getUiSettings().setZoomGesturesEnabled(false);
            mSeekBar.setProgress(DEFAULT_SEARCH_RADIUS / seekBarStepInMeters);
            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    int step = (RADIUS_HIGH - RADIUS_LOW) / mSeekBar.getMax();
                    mCurrentAreaRadius = RADIUS_LOW + (step * progress);

                    mMap.moveCamera(CameraUpdateFactory.zoomTo(
                            calcZoomLevelForDistance(mCurrentAreaRadius * 2).floatValue()));
                    updateDistanceIndicator();
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

    private void updateDistanceIndicator() {
        final int metersInKilometer = 1000;
        if (mCurrentAreaRadius >= metersInKilometer) {
            BigDecimal distanceInKm = BigDecimal.valueOf(mCurrentAreaRadius)
                    .divide(BigDecimal.valueOf(metersInKilometer), MathContext.DECIMAL32);
            mCurrentArea.setText(
                    String.format(Locale.getDefault(), "%.2fkm", distanceInKm.floatValue()));
        } else {
            mCurrentArea.setText(
                    String.format(Locale.getDefault(), "%dm", mCurrentAreaRadius));
        }
    }

    private BigDecimal calcZoomLevelForDistance(int distance) {
        //At zoom level N, the width of the world is approximately 256 * 2 N dp,
        //i.e., at zoom level 2, the whole world is approximately 1024dp wide
        //https://developers.google.com/android/reference/com/google/android/gms/maps/model/CameraPosition.Builder
        int baseMapSizeDp = 256;
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        double circleWidthDp = mMapCircle.getWidth() /
                (metrics.densityDpi / DisplayMetrics.DENSITY_MEDIUM);
        double earthCircumferenceLatBased = EQUATORIAL_CIRCUMFERENCE.doubleValue() *
                Math.cos(mPersistentStorage.getLocationFromCache().getLat());
        double mapSizeDp = circleWidthDp * earthCircumferenceLatBased / distance;
        double zoom = Math.log(mapSizeDp / baseMapSizeDp) / Math.log(2.0);
        return BigDecimal.valueOf(zoom);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.location_picker_screen_title);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        SpannableString title = new SpannableString(getString(R.string.map_view_fragment_save));
        title.setSpan(new ForegroundColorSpan(
                getResources().getColor(R.color.colorAccent)), 0, title.length(), 0);
        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, title.length(), 0);

        MenuItem menuItem = menu.add(Menu.NONE, MENU_ITEM_ID_DONE, Menu.NONE, title);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_ITEM_ID_DONE) {
            LocationEntity locationEntity = new LocationEntity(
                    mMap.getCameraPosition().target.latitude,
                    mMap.getCameraPosition().target.longitude);
            try {
                List<Address> addresses = mGeocoder.getFromLocation(
                        mMap.getCameraPosition().target.latitude,
                        mMap.getCameraPosition().target.longitude,
                        1);
                if (addresses != null && addresses.size() > 0 && addresses.get(0) != null) {
                    locationEntity.setAddress(addresses.get(0).getAddressLine(0));
                }
            } catch (IOException e) {
                Timber.e(e);
            }
            mQueryFilter.setLocation(locationEntity);
            mQueryFilter.setSearchArea(mCurrentAreaRadius);
        }
        getActivity().onBackPressed();
        return false;
    }
}