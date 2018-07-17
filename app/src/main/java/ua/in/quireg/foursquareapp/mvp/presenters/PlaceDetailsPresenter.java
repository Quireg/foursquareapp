package ua.in.quireg.foursquareapp.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
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

    @Inject
    protected MainRouter mRouter;

    PlaceDetailsInteractor mPlaceDetailsInteractor = new PlaceDetailsInteractor();

    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private int currentTipsOffset = 0;
    private boolean shouldFetchTips = true;

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

        if (!shouldFetchTips) return;
        shouldFetchTips = false;

        getViewState().toggleTipsLoading(true);
        int tipsOffsetBeforeFetch = currentTipsOffset;

        mCompositeDisposable.add(
                mPlaceDetailsInteractor
                        .getPlaceTip(mPlaceId, String.valueOf(currentTipsOffset))
                        .subscribe(
                                tipEntity -> {
                                    getViewState().toggleTipsLayout(true);
                                    getViewState().setPlaceTip(tipEntity);
                                    currentTipsOffset++;
                                },
                                throwable -> {
                                    mRouter.showErrorDialog(throwable.getLocalizedMessage());
                                    getViewState().toggleTipsLoading(false);
                                    getViewState().toggleTipsLayout(false);
                                    Timber.w(throwable);

                                    shouldFetchTips = true;
                                },
                                () -> {
                                    getViewState().toggleTipsLoading(false);
                                    shouldFetchTips = true;

                                    if (currentTipsOffset == 0) {
                                        getViewState().toggleTipsLayout(false);
                                    }

                                    if (tipsOffsetBeforeFetch == currentTipsOffset) {
                                        shouldFetchTips = false;
                                    }
                                }
                        )
        );
    }

}
