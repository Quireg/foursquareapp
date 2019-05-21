package ua.in.quireg.foursquareapp.mvp.presenters;

import android.content.SharedPreferences;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

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

    @Inject MainRouter mRouter;
    @Inject FoursquareApplication mFoursquareApplication;
    @Inject SharedPreferences mPreferences;

    public WelcomeScreenPresenter() {
        super();
        FoursquareApplication.getAppComponent().inject(this);
    }

    @Override
    public void attachView(WelcomeView view) {
        super.attachView(view);
        getViewState().clearState();
        getViewState().setAnimationsEnabled(
                mPreferences.getBoolean("key_animations", true));

        if (PermissionManager.verifyPermissions(mFoursquareApplication.getApplicationContext())) {
            getViewState().setReadyState();
        } else {
            getViewState().setPermissionsRequiredState();
        }
    }

    public void requestPermissions() {
        mRouter.requestPermissions();
    }

    public void lookAround() {
        getViewState().clearState();
        mRouter.placesListScreen();
    }
}
