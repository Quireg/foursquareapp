package ua.in.quireg.foursquareapp.mvp.presenters;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.android.gms.location.LocationListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.common.LocRepository;
import ua.in.quireg.foursquareapp.common.PermissionManager;
import ua.in.quireg.foursquareapp.common.QueryFilter;
import ua.in.quireg.foursquareapp.domain.PlacesListInteractor;
import ua.in.quireg.foursquareapp.models.LocationEntity;
import ua.in.quireg.foursquareapp.models.PlaceEntity;
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

    private final int REQ_LOCATION_UPDATE_TIMEOUT = 5000;
    private final String DEFAULT_LIMIT = "20";

    @Inject MainRouter mRouter;
    @Inject QueryFilter mQueryFilter;
    @Inject LocRepository mLocRepository;
    @Inject PersistentStorage mPersistentStorage;
    @Inject FoursquareApplication mFoursquareApplication;

    private PlacesListInteractor mPlacesListInteractor = new PlacesListInteractor();
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private LocListener mLocListener = new LocListener();
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mReceiveLocUpdateTimeout = () -> getNearbyPlaces(
            mPersistentStorage.getLocationFromCache().getLatLonCommaSeparated(),
            String.valueOf(mPersistentStorage.getAreaFromCache())
    );
    private List<PlaceEntity> mPlacesAroundList = new ArrayList<>();
    private List<PlaceEntity> mPlacesFromSearchList = new ArrayList<>();

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
    public void detachView(PlacesListView view) {
        super.detachView(view);
        mCompositeDisposable.clear();
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
                    getViewState().setListTitle(String.format(
                            mFoursquareApplication.getString(R.string.search_list_title), q));
                    return q;
                })
                .switchMap(query -> mPlacesListInteractor
                        .getPlaces(
                                query,
                                mPersistentStorage.getLocationFromCache().getLatLonCommaSeparated(),
                                String.valueOf(mPersistentStorage.getAreaFromCache()),
                                DEFAULT_LIMIT)
                        .map(placeEntity -> {
                            Timber.d("Thread: %s", Thread.currentThread().getName());
                            return placeEntity;
                        })
                        .subscribeOn(Schedulers.io())
                )
                .map(placeEntity -> {
                    mPlacesFromSearchList.add(placeEntity);
                    return placeEntity;
                })
                .toList()
                .toObservable()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(mPlacesAroundObserver);
    }

    private void getNearbyPlaces(String lonLatCommaSeparated, String radius) {
        mPlacesListInteractor
                .getPlaces(null, lonLatCommaSeparated, radius, DEFAULT_LIMIT)
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .toObservable()
                .map(items -> {
                    mPlacesAroundList.clear();
                    mPlacesAroundList.addAll(items);
                    return items;
                })
                .subscribeOn(Schedulers.io())
                .subscribe(mPlacesAroundObserver);
    }

    public void onSearchCanceled() {
        Observable.just(mPlacesAroundList)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(mPlacesAroundObserver);
    }

    private Observer<List<PlaceEntity>> mPlacesAroundObserver = new Observer<List<PlaceEntity>>() {
        @Override
        public void onSubscribe(Disposable d) {
            getViewState().setListTitle(
                    mFoursquareApplication.getString(R.string.looading));
            mCompositeDisposable.add(d);
        }

        @Override
        public void onNext(List<PlaceEntity> placeEntities) {
            getViewState().clearList();
            getViewState().setList(placeEntities);
        }

        @Override
        public void onError(Throwable error) {
            Timber.e(error);
            mRouter.showErrorDialog(error.getMessage());
            getViewState().toggleLoadingView(false);
            getViewState().setListTitle(
                    mFoursquareApplication.getString(R.string.something_went_wrong));
        }

        @Override
        public void onComplete() {
            getViewState().setListTitle(
                    mFoursquareApplication.getString(R.string.default_list_title));
            getViewState().toggleLoadingView(false);
        }
    };

    public void startNegotiation() {
        if (!checkPreconditions()) {
            return;
        }
        if (mQueryFilter.getLocation() != null && !mFoursquareApplication.isMockLocation()) {
            getNearbyPlaces(mQueryFilter.getLocation().getLatLonCommaSeparated(),
                    String.valueOf(mQueryFilter.getSearchArea()));
        } else {
            mHandler.postDelayed(mReceiveLocUpdateTimeout, REQ_LOCATION_UPDATE_TIMEOUT);
            getViewState().setListTitle(mFoursquareApplication.getString(R.string.waiting_for_gps));
            getViewState().toggleLoadingView(true);
            mLocRepository.subscribeToLocUpdates(mLocListener);
        }
    }

    private class LocListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            mHandler.removeCallbacks(mReceiveLocUpdateTimeout);
            mLocRepository.unsubscribeFromLocUpdates(mLocListener);
            mPersistentStorage.addLocationToCache(
                    new LocationEntity(location.getLatitude(), location.getLongitude()));
            getNearbyPlaces(String.format("%s,%s", location.getLatitude(), location.getLongitude()),
                    String.valueOf(mPersistentStorage.getAreaFromCache()));
        }
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
        ConnectivityManager conMgr = (ConnectivityManager)
                mFoursquareApplication.getSystemService(Context.CONNECTIVITY_SERVICE);
        return conMgr != null && conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private boolean isGpsEnabled() {
        LocationManager manager = (LocationManager)
                mFoursquareApplication.getSystemService(Context.LOCATION_SERVICE);
        return manager != null && manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
