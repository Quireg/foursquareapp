package ua.in.quireg.foursquareapp.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.mvp.routing.BackStack;
import ua.in.quireg.foursquareapp.mvp.views.MainActivityView;

/**
 * Created by Arcturus Mengsk on 27.04.2019, 6:48.
 * foursquareapp
 */

@InjectViewState
public class MainActivityPresenter extends MvpPresenter<MainActivityView> {

    private BackStack mBackStack;

    public MainActivityPresenter() {
        super();
        FoursquareApplication.getAppComponent().inject(this);
    }

    public synchronized BackStack getBackStack() {
        if (mBackStack == null) {
            mBackStack = new BackStack();
        }
        return mBackStack;
    }
}
