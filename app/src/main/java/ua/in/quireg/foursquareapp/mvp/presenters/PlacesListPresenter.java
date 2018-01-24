package ua.in.quireg.foursquareapp.mvp.presenters;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.android.gms.location.LocationListener;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.common.LocRepository;
import ua.in.quireg.foursquareapp.common.PermissionManager;
import ua.in.quireg.foursquareapp.common.QueryFilter;
import ua.in.quireg.foursquareapp.domain.NearbyPlacesInteractor;
import ua.in.quireg.foursquareapp.models.LocationEntity;
import ua.in.quireg.foursquareapp.mvp.routing.MainRouter;
import ua.in.quireg.foursquareapp.mvp.views.PlacesListView;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 6:26 PM.
 * foursquareapp
 */

@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal"})
@InjectViewState
public class PlacesListPresenter extends MvpPresenter<PlacesListView> {

    @Inject MainRouter mRouter;
    @Inject QueryFilter mQueryFilter;
    @Inject LocRepository mLocRepository;
    @Inject FoursquareApplication mFoursquareApplication;

    private final String DEFAULT_RADIUS = "10000";
    private final String DEFAULT_LIMIT = "20";

    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public PlacesListPresenter() {
        super();
        FoursquareApplication.getAppComponent().inject(this);
    }

    public void openFilterScreen() {
        mRouter.filterScreen();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        startNegotiation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    public void onSearchRequest(Observable<String> stringObservable) {

        stringObservable
                .debounce(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(query -> {
                            if (query.isEmpty()) return;
                            if (!checkPreconditions()) return;

                            Location l = mLocRepository.getLastKnownLocation();
                            LocationEntity e = new LocationEntity(l.getLatitude(), l.getLongitude());

                            getViewState().setListTitle(String.format(mFoursquareApplication.getString(R.string.search_list_title), query));
                            kickOffRequest(e, DEFAULT_RADIUS, query);
                        },
                        throwable -> mRouter.showErrorDialog(throwable.getLocalizedMessage())
                );
    }

    public void startNegotiation() {
        if (!checkPreconditions()) {
            return;
        }

        if (mQueryFilter.getLocation() != null) {

            kickOffRequest(mQueryFilter.getLocation(), mQueryFilter.getSearchRadius(), null);

        } else {

            if (mLocRepository.getLastKnownLocation() != null) {

                Location l = mLocRepository.getLastKnownLocation();
                LocationEntity e = new LocationEntity(l.getLatitude(), l.getLongitude(), false);
                getViewState().setListTitle(mFoursquareApplication.getString(R.string.default_list_title));

                kickOffRequest(e, DEFAULT_RADIUS, null);
            } else {
                //As soon as callback triggered network call will be dispatched.
                getViewState().setListTitle("Waiting for GPS");
                mLocRepository.subscribeToLocUpdates(mLocListener);
            }

        }

    }

    LocListener mLocListener = new LocListener();

    private class LocListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            kickOffRequest(new LocationEntity(location.getLatitude(), location.getLongitude()), DEFAULT_RADIUS, null);

            mLocRepository.unsubscribeFromLocUpdates(mLocListener);

            getViewState().setListTitle(mFoursquareApplication.getString(R.string.default_list_title));

        }
    }

    private void kickOffRequest(LocationEntity l, String radius, String query) {

        getViewState().clear();

        mCompositeDisposable.clear();

        mCompositeDisposable.add(
                new NearbyPlacesInteractor()
                        .getNearbyPlaces(l.getLatLonCommaSeparated(), query, radius, DEFAULT_LIMIT)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                next -> getViewState().addToList(next),
                                error -> {
                                    Timber.e(error);
                                    getViewState().toggleLoadingView(false);
                                    mRouter.showErrorDialog(error.getLocalizedMessage());
                                },
                                () -> getViewState().toggleLoadingView(false)
                        )
        );
    }

    public boolean checkPreconditions() {

        getViewState().toggleLoadingView(true);

        if (!isManifestPermissionsOk()) {

            mRouter.showSystemMessage(R.string.error_permission_text);
            mRouter.requestPermissions();
            getViewState().toggleLoadingView(false);

            return false;
        }

        if (!isInternetConnectionOk()) {
            getViewState().toggleLoadingView(false);
            mRouter.showSystemMessage(R.string.error_internet_text);

            return false;
        }

        if (!isGpsEnabled()) {
            getViewState().toggleLoadingView(false);
            mRouter.showSystemMessage(R.string.error_gps_text);
            return false;
        }

        return true;
    }

    private boolean isManifestPermissionsOk() {
        return PermissionManager.verifyPermissions(mFoursquareApplication.getApplicationContext());
    }

    private boolean isInternetConnectionOk() {
        ConnectivityManager conMgr = (ConnectivityManager) mFoursquareApplication.getSystemService(Context.CONNECTIVITY_SERVICE);

        return conMgr != null && conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isConnectedOrConnecting();

    }

    private boolean isGpsEnabled() {
        LocationManager manager = (LocationManager) mFoursquareApplication.getSystemService(Context.LOCATION_SERVICE);

        return manager != null && manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}
