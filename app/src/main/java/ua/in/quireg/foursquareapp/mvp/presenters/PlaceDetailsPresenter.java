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

    @Inject
    protected MainRouter mRouter;

    PlaceDetailsInteractor mPlaceDetailsInteractor = new PlaceDetailsInteractor();
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private int mCurrentTipsOffset = 0;
    private boolean mShouldFetchTips = true;

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
        Disposable d = mPlaceDetailsInteractor
                .getPlaceDetails(mPlaceId)
                .subscribe(
                        placeEntityExtended -> getViewState().setPlaceInfo(placeEntityExtended),
                        throwable -> {
                            mRouter.showErrorDialog(throwable.getLocalizedMessage());
                            Timber.w(throwable.getLocalizedMessage());
                        }
                );
        mCompositeDisposable.add(d);
    }

    private void obtainPlacePhoto() {
        Disposable d = mPlaceDetailsInteractor
                .getPlacePhotos(mPlaceId)
                .subscribe(
                        photosList -> getViewState().setPlacePhotos(photosList),
                        throwable -> {
                            mRouter.showErrorDialog(throwable.getLocalizedMessage());
                            Timber.w(throwable.getLocalizedMessage());
                        }
                );
        mCompositeDisposable.add(d);
    }

    private void obtainPlaceTips() {
        if (!mShouldFetchTips) return;
        mShouldFetchTips = false;

        getViewState().toggleTipsLoading(true);
        int tipsOffsetBeforeFetch = mCurrentTipsOffset;

        Disposable d = mPlaceDetailsInteractor
                .getPlaceTip(mPlaceId, String.valueOf(mCurrentTipsOffset))
                .subscribe(
                        tipEntity -> {
                            getViewState().toggleTipsLayout(true);
                            getViewState().setPlaceTip(tipEntity);
                            mCurrentTipsOffset++;
                        },
                        throwable -> {
                            mRouter.showErrorDialog(throwable.getLocalizedMessage());
                            getViewState().toggleTipsLoading(false);
                            getViewState().toggleTipsLayout(false);
                            Timber.w(throwable.getLocalizedMessage());

                            mShouldFetchTips = true;
                        },
                        () -> {
                            getViewState().toggleTipsLoading(false);
                            mShouldFetchTips = true;

                            if (mCurrentTipsOffset == 0) {
                                getViewState().toggleTipsLayout(false);
                            }
                            if (tipsOffsetBeforeFetch == mCurrentTipsOffset) {
                                mShouldFetchTips = false;
                            }
                        }
                );
        mCompositeDisposable.add(d);
    }
}
