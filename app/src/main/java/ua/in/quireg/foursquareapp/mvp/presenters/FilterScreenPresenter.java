package ua.in.quireg.foursquareapp.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.mvp.routing.MainRouter;
import ua.in.quireg.foursquareapp.mvp.views.FilterView;

/**
 * Created by Arcturus Mengsk on 1/23/2018, 9:40 PM.
 * foursquareapp
 */

@InjectViewState
public class FilterScreenPresenter extends MvpPresenter<FilterView> {

    @Inject MainRouter mRouter;
    @Inject FoursquareApplication mFoursquareApplication;
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();




}
