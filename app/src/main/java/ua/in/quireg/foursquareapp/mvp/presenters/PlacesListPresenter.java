package ua.in.quireg.foursquareapp.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.common.PermissionManager;
import ua.in.quireg.foursquareapp.domain.NearbyPlacesInteractor;
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
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public PlacesListPresenter() {
        super();
        FoursquareApplication.getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        if (!checkPermissions()) {
            mRouter.requestPermissions();
            return;
        }
        fetchNearbyPlaces();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    public void refresh() {
        fetchNearbyPlaces();
    }

    private void fetchNearbyPlaces() {
        getViewState().clear();
        getViewState().toggleLoadingView(true);

        mCompositeDisposable.clear();

        mCompositeDisposable.add(
                new NearbyPlacesInteractor()
                        .getNearbyPlaces()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                next -> {
                                    getViewState().setListTitle(mFoursquareApplication.getString(R.string.default_list_title));
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

}
