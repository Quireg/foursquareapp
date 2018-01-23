package ua.in.quireg.foursquareapp.mvp.views;

import com.arellomobile.mvp.MvpView;

import ua.in.quireg.foursquareapp.models.PlaceEntity;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 4:26 PM.
 * foursquareapp
 */

public interface PlacesListView extends MvpView {

    void setListTitle(String title);

    void addToList(PlaceEntity place);

    void clear();

    void toggleLoadingView(boolean visible);

}
