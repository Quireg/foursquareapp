package ua.in.quireg.foursquareapp.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.domain.PlaceDetailsInteractor;
import ua.in.quireg.foursquareapp.mvp.routing.MainRouter;
import ua.in.quireg.foursquareapp.mvp.views.PlaceDetailsView;

/**
 * Created by Arcturus Mengsk on 1/29/2018, 8:32 PM.
 * foursquareapp
 */
@InjectViewState
@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal"})
public class PlaceDetailsPresenter extends MvpPresenter<PlaceDetailsView> {

    private String mPlaceId;

    @Inject protected MainRouter mRouter;

    PlaceDetailsInteractor mPlaceDetailsInteractor = new PlaceDetailsInteractor();

    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    Disposable mObtainPlaceTipsDisposable;

    private int tipsOffset = 0;

    public PlaceDetailsPresenter(String id) {
        super();
        FoursquareApplication.getAppComponent().inject(this);
        mPlaceId = id;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        obtainPlaceInfo();
        obtainPlacePhoto();
        obtainPlaceTips();
    }

    @Override
    public void destroyView(PlaceDetailsView view) {
        super.destroyView(view);
        mCompositeDisposable.clear();
    }

    public void loadMorePlaceTips() {
        if (mObtainPlaceTipsDisposable != null) {
            if (!mObtainPlaceTipsDisposable.isDisposed()) {
                return;
            }
        }
        obtainPlaceTips();
    }

    private void obtainPlaceInfo() {
        mCompositeDisposable.add(
                mPlaceDetailsInteractor
                        .getPlaceDetails(mPlaceId)
                        .subscribe(
                                placeEntityExtended -> getViewState().setPlaceInfo(placeEntityExtended),
                                throwable -> {
                                    mRouter.showErrorDialog(throwable.getLocalizedMessage());
                                    Timber.e(throwable);
                                }
                        )
        );
    }

    private void obtainPlacePhoto() {
        mCompositeDisposable.add(
                mPlaceDetailsInteractor
                        .getPlacePhotos(mPlaceId)
                        .subscribe(
                                photosList -> getViewState().setPlacePhotos(photosList),
                                throwable -> {
                                    mRouter.showErrorDialog(throwable.getLocalizedMessage());
                                    Timber.e(throwable);
                                }
                        )
        );
    }

    private void obtainPlaceTips() {

        mObtainPlaceTipsDisposable = mPlaceDetailsInteractor
                .getPlaceTip(mPlaceId, String.valueOf(tipsOffset))
                .subscribe(
                        tipEntity -> {
                            getViewState().setPlaceTip(tipEntity);
                            tipsOffset++;
                        },
                        throwable -> {
                            mRouter.showErrorDialog(throwable.getLocalizedMessage());
                            Timber.e(throwable);
                        }
                );
        mCompositeDisposable.add(mObtainPlaceTipsDisposable);
    }

}
