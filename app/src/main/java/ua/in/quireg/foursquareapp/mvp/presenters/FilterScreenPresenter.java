package ua.in.quireg.foursquareapp.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.Locale;

import javax.inject.Inject;

import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.common.QueryFilter;
import ua.in.quireg.foursquareapp.mvp.routing.MainRouter;
import ua.in.quireg.foursquareapp.mvp.views.FilterView;
import ua.in.quireg.foursquareapp.repositories.PersistentStorage;

/**
 * Created by Arcturus Mengsk on 1/23/2018, 9:40 PM.
 * foursquareapp
 */

@InjectViewState
public class FilterScreenPresenter extends MvpPresenter<FilterView> {

    @Inject MainRouter mRouter;
    @Inject FoursquareApplication mFoursquareApplication;
    @Inject QueryFilter mQueryFilter;
    @Inject PersistentStorage mPersistentStorage;

    public FilterScreenPresenter() {
        super();
        FoursquareApplication.getAppComponent().inject(this);
    }

    @Override
    public void attachView(FilterView view) {
        super.attachView(view);
        updateUI();
    }

    public void resetFilter() {
        mQueryFilter.resetState();
        updateUI();
    }

    public void updatePriceFilter(int i) {
        mQueryFilter.setPriceTierFilter(i);
        updateUI();
    }

    public void onDistanceClick() {
        mQueryFilter.setSortType(QueryFilter.SORT_TYPE_DISTANCE);
        updateUI();
    }

    public void onRelevanceClick() {
        mQueryFilter.setSortType(QueryFilter.SORT_TYPE_RELEVANCE);
        updateUI();
    }

    public void pickLocation() {
        mRouter.locationPickerScreen();
    }

    public void resetLocation() {
        mQueryFilter.setLocation(null);
        mQueryFilter.setSearchArea(QueryFilter.SEARCH_AREA_DEFAULT);
        updateUI();
    }

    private void updateUI() {
        getViewState().togglePriceTier(mQueryFilter.getPriceTierFilter());
        getViewState().toggleRelevance(mQueryFilter.getSortType() == QueryFilter.SORT_TYPE_RELEVANCE);

        if (mQueryFilter.getLocation() == null) {
            getViewState().setLocation(String.format(
                    Locale.getDefault(),
                    "GPS location:\n %s\nArea: %d2m",
                    mPersistentStorage.getLocationFromCache().getLatLonCommaSeparated(),
                    mPersistentStorage.getAreaFromCache()
            ));
        } else {
            getViewState().setLocation(String.format(
                    Locale.getDefault(),
                    "Custom location:\n %s\nArea: %dm",
                    mQueryFilter.getLocation().getLatLonCommaSeparated(),
                    mQueryFilter.getSearchArea()
            ));
        }

    }
}
