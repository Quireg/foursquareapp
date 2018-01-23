package ua.in.quireg.foursquareapp.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.common.ResourceManager;
import ua.in.quireg.foursquareapp.domain.NearbyPlacesInteractor;
import ua.in.quireg.foursquareapp.mvp.views.PlacesListView;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 6:26 PM.
 * foursquareapp
 */

@InjectViewState
public class PlacesListPresenter extends MvpPresenter<PlacesListView> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Inject ResourceManager mResourceManager;

    public PlacesListPresenter() {
        super();
        FoursquareApplication.getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        //kickOffGetNearbyPlacesQuery();
        getViewState().showErrorView("Error!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    public void refreshLast() {
        kickOffGetNearbyPlacesQuery();
    }

    private void kickOffGetNearbyPlacesQuery() {
        //getViewState().showLoading();

        final String nearbyPlacesQueryTitle = mResourceManager.getResources().getString(R.string.default_list_title);

        compositeDisposable.clear();
        compositeDisposable.add(
                new NearbyPlacesInteractor()
                        .getNearbyPlaces()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                placesList -> {
                                    getViewState().showPlaces(placesList, nearbyPlacesQueryTitle);
                                },
                                e -> {
                                    Timber.e(e);
                                    getViewState().showErrorView(e.getLocalizedMessage());
                                }
                        )
        );
    }

}
