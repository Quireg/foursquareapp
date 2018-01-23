package ua.in.quireg.foursquareapp.mvp.views;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import ua.in.quireg.foursquareapp.mvp.models.presentation.PlaceEntity;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 4:26 PM.
 * foursquareapp
 */

public interface PlacesListView extends MvpView {

    void showLoading();

    void showPlaces(List<PlaceEntity> places, String title);

    void showErrorView(String text);
}
