package ua.in.quireg.foursquareapp.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.common.QueryFilter;
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
    @Inject QueryFilter mQueryFilter;

    public FilterScreenPresenter() {
        super();
        FoursquareApplication.getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
//        getViewState().togglePriceTier(mQueryFilter.getPriceTierFilter());
//        getViewState().toggleRelevance(mQueryFilter.getSortType() == QueryFilter.SORT_TYPE_RELEVANCE);
//        getViewState().resetLocation("In the Middle of Nowhere");
    }

    public void resetFilter() {
        mQueryFilter.resetState();
        getViewState().togglePriceTier(mQueryFilter.getPriceTierFilter());
        getViewState().toggleRelevance(mQueryFilter.getSortType() == QueryFilter.SORT_TYPE_RELEVANCE);
    }

    public void updatePriceFilters(int i) {
        mQueryFilter.setPriceTierFilter(i);
        getViewState().togglePriceTier(mQueryFilter.getPriceTierFilter());
    }

    public void onDistanceClick() {
        mQueryFilter.setSortType(QueryFilter.SORT_TYPE_DISTANCE);
    }

    public void onRelevanceClick() {
        mQueryFilter.setSortType(QueryFilter.SORT_TYPE_RELEVANCE);
    }

    public void pickLocation() {
        mRouter.locationPickerScreen();
    }

    public void resetLocation() {
        getViewState().resetLocation("");
    }
}
