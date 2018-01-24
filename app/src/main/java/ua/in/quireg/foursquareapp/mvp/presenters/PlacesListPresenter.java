package ua.in.quireg.foursquareapp.mvp.presenters;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.android.gms.location.LocationListener;

import javax.inject.Inject;

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

@SuppressWarnings("WeakerAccess")
@InjectViewState
public class PlacesListPresenter extends MvpPresenter<PlacesListView> {

    @Inject MainRouter mRouter;
    @Inject FoursquareApplication mFoursquareApplication;
    @Inject QueryFilter mQueryFilter;
    @Inject LocRepository mLocRepository;

    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    LocListener mLocListener = new LocListener();

    public PlacesListPresenter() {
        super();
        FoursquareApplication.getAppComponent().inject(this);
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

    public void startNegotiation() {

        getViewState().toggleLoadingView(true);
        getViewState().setListTitle("Checking network and location permissions.");

        if (!checkPermissions()) {

            mRouter.showSystemMessage(R.string.error_permission_text);
            mRouter.requestPermissions();

            getViewState().toggleLoadingView(false);
            getViewState().setListTitle("No location permission, please grant&retry.");

            return;
        }

        if (!checkInternetConnection()) {
            getViewState().toggleLoadingView(false);
            getViewState().setListTitle("No internet =(");
        }

        if (mQueryFilter.getLocation().shouldUse()) {

            fetchPlacesForArea(mQueryFilter.getLocation(), mQueryFilter.getSearchRadius());

        } else {
            getViewState().setListTitle("Where am I?");
            mLocRepository.subscribeToLocUpdates(mLocListener);
            //Upon callback with location process will be resumed;
        }

    }

    public void openFilterScreen() {
        mRouter.filterScreen();
    }

    private void fetchPlacesForArea(LocationEntity l, String radius) {

        getViewState().setListTitle(
                String.format("Results for %s %s, radius %s", l.getLat(), l.getLon(), radius)
        );

        mCompositeDisposable.clear();

        mCompositeDisposable.add(
                new NearbyPlacesInteractor()
                        .getNearbyPlaces(l.getLatLonCommaSeparated(), radius, "20")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                next -> {
                                    getViewState().addToList(next);
                                },
                                error -> {
                                    Timber.e(error);
                                    getViewState().toggleLoadingView(false);
                                    mRouter.showErrorDialog(error.getLocalizedMessage());
                                },
                                () -> {
                                    //onComplete
                                    getViewState().toggleLoadingView(false);
                                }
                        )
        );
    }

    private boolean checkPermissions() {
        return PermissionManager.verifyPermissions(mFoursquareApplication.getApplicationContext());
    }

    private boolean checkInternetConnection() {
        ConnectivityManager conMgr = (ConnectivityManager) mFoursquareApplication.getSystemService(Context.CONNECTIVITY_SERVICE);

        return conMgr != null && conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isConnectedOrConnecting();

    }
//
//    private boolean checkGps() {
//        return Settings.Secure.isLocationProviderEnabled(mContext.getContentResolver(),
//                        LocationManager.GPS_PROVIDER);
//    }

    private class LocListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {

            fetchPlacesForArea(new LocationEntity(location.getLatitude(), location.getLongitude()), "50");

            mLocRepository.unsubscribeFromLocUpdates(mLocListener);
        }
    }

}
