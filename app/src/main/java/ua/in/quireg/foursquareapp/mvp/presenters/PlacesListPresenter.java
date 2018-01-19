package ua.in.quireg.foursquareapp.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;
import ua.in.quireg.foursquareapp.domain.NearbyPlacesInteractor;
import ua.in.quireg.foursquareapp.mvp.views.PlacesListView;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 6:26 PM.
 * foursquareapp
 */

@InjectViewState
public class PlacesListPresenter extends MvpPresenter<PlacesListView> {

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().showLoading();

        NearbyPlacesInteractor nearbyPlacesInteractor = new NearbyPlacesInteractor();
        compositeDisposable.add(
                nearbyPlacesInteractor.getNearbyPlaces()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                placesList -> getViewState().showPlaces(placesList),
                                Timber::e
                        )
        );
    }

    @Override
    public void detachView(PlacesListView view) {
        super.detachView(view);
        compositeDisposable.clear();
    }
}
