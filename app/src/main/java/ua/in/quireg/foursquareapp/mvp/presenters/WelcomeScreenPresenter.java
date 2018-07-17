package ua.in.quireg.foursquareapp.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.common.PermissionManager;
import ua.in.quireg.foursquareapp.mvp.routing.MainRouter;
import ua.in.quireg.foursquareapp.mvp.views.WelcomeView;

/**
 * Created by Arcturus Mengsk on 2/4/2018, 6:54 AM.
 * foursquareapp
 */

@InjectViewState
public class WelcomeScreenPresenter extends MvpPresenter<WelcomeView> {

    @Inject
    FoursquareApplication mFoursquareApplication;

    @Inject
    MainRouter mRouter;

    private final int CLOUD_IN_ANIM_DURATION = 2000;
    private final int FRAGMENT_IN_ANIM_DURATION = 300;
    private final int FRAGMENT_OUT_ANIM_DURATION = 300;
    private final int WELCOME_TEXT_ANIM_DURATION = 1000;
    private final int PAUSE_PERMISSIONS_ANIM_DURATION = 1000;
    private final int PERMISSION_TEXT_ANIM_DURATION = 1000;

    private boolean shouldAnimate = false;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public WelcomeScreenPresenter() {
        super();
        FoursquareApplication.getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

    }

    @Override
    public void attachView(WelcomeView view) {
        super.attachView(view);

        if (shouldAnimate) {

            completeWithDelay(showCloud(), FRAGMENT_IN_ANIM_DURATION);

            if (PermissionManager.verifyPermissions(mFoursquareApplication.getApplicationContext())) {

                completeWithDelay(setReadyState(), CLOUD_IN_ANIM_DURATION);
            } else {

                completeWithDelay(setPermissionsRequiredState(), CLOUD_IN_ANIM_DURATION);
            }
            shouldAnimate = false;

        } else {

            getViewState().animateEnterFragment(CLOUD_IN_ANIM_DURATION);

            if (PermissionManager.verifyPermissions(mFoursquareApplication.getApplicationContext())) {
                getViewState().setReadyState();
            } else {
                getViewState().setPermissionsRequiredState();
            }

        }
    }

    @Override
    public void destroyView(WelcomeView view) {
        mCompositeDisposable.clear();
        super.destroyView(view);
    }

    private void completeWithDelay(CompletableObserver completableObserver, int delay) {
        Completable.complete()
                .delay(delay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(completableObserver);
    }

    private CompletableObserver showCloud() {

        return new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
                Timber.d("onSubscribe");
            }

            @Override
            public void onComplete() {
                getViewState().animateEnterFragment(CLOUD_IN_ANIM_DURATION);
                Timber.d("onComplete : showCloud");
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);
            }
        };

    }

    private CompletableObserver setReadyState() {

        return new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
                Timber.d("onSubscribe");
            }

            @Override
            public void onComplete() {
                getViewState().setReadyState();
                Timber.d("onComplete : setReadyState");
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);
            }
        };
    }

    private CompletableObserver setPermissionsRequiredState() {

        return new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
                Timber.d("onSubscribe");
            }

            @Override
            public void onComplete() {
                getViewState().setPermissionsRequiredState();
                Timber.d("onComplete : setPermissionsRequiredState");
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);
            }
        };
    }

    public void requestPermissions() {
        mRouter.requestPermissions();
    }

    public void lookAround() {
        //getViewState().animateExitFragment(FRAGMENT_OUT_ANIM_DURATION);
        mRouter.placesListScreen();
    }

}
