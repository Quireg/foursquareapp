package ua.in.quireg.foursquareapp.mvp.presenters;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.android.gms.location.LocationListener;

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
import ua.in.quireg.foursquareapp.domain.PlacesListInteractor;
import ua.in.quireg.foursquareapp.mvp.routing.MainRouter;
import ua.in.quireg.foursquareapp.mvp.views.PlacesListView;
import ua.in.quireg.foursquareapp.repositories.PersistentStorage;

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
    @Inject PersistentStorage mPersistentStorage;
    @Inject FoursquareApplication mFoursquareApplication;

    private PlacesListInteractor mPlacesListInteractor = new PlacesListInteractor();

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
                .observeOn(AndroidSchedulers.mainThread())
                .filter(s -> !s.isEmpty())
                .filter(s1 -> checkPreconditions())
                .map(q -> {
                    getViewState().setListTitle(String.format(mFoursquareApplication.getString(R.string.search_list_title), q));
                    getViewState().clear();
                    return q;
                })
                .switchMap(query -> mPlacesListInteractor
                        .getNearbyPlaces(
                                query,
                                mPersistentStorage.getLocationFromCache().getLatLonCommaSeparated(),
                                mPersistentStorage.getRadiusFromCache(), DEFAULT_LIMIT
                        )
                        .doOnComplete(() -> getViewState().toggleLoadingView(false))
                )
                .subscribe(
                        next -> getViewState().addToList(next),
                        error -> {
                            getViewState().toggleLoadingView(false);
                            mRouter.showErrorDialog(error.getLocalizedMessage());
                        },
                        () -> getViewState().toggleLoadingView(false)
                );

    }

    public void onSearchCanceled() {
        startNegotiation();
    }

    public void startNegotiation() {

        if (!checkPreconditions()) {
            return;
        }

        if (mQueryFilter.getLocation() != null) {

            getNearbyPlaces(mQueryFilter.getLocation().getLatLonCommaSeparated(), mQueryFilter.getSearchRadius());

        } else {
            if (true) {

                getNearbyPlaces(mPersistentStorage.getLocationFromCache().getLatLonCommaSeparated(), mPersistentStorage.getRadiusFromCache());

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

            getNearbyPlaces(String.format("%s,%s", location.getLatitude(), location.getLongitude()), mPersistentStorage.getRadiusFromCache());

            mLocRepository.unsubscribeFromLocUpdates(mLocListener);

        }
    }

    private void getNearbyPlaces(String lonLatCommaSeparated, String radius) {

        getViewState().clear();
        getViewState().setListTitle(mFoursquareApplication.getString(R.string.default_list_title));

        mCompositeDisposable.clear();

        mCompositeDisposable.add(
                mPlacesListInteractor
                        .getNearbyPlaces(null, lonLatCommaSeparated, radius, DEFAULT_LIMIT)
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
