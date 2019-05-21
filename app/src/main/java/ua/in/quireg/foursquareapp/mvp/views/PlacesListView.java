package ua.in.quireg.foursquareapp.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ua.in.quireg.foursquareapp.models.PlaceEntity;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 4:26 PM.
 * foursquareapp
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface PlacesListView extends MvpView {

    void setListTitle(String title);

    void setList(List<PlaceEntity> place);

    void cancelSearch();

    void clearList();

    void toggleLoadingView(boolean visible);
}
