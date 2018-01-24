package ua.in.quireg.foursquareapp.mvp.views;

import com.arellomobile.mvp.MvpView;

/**
 * Created by Arcturus Mengsk on 1/23/2018, 8:58 PM.
 * foursquareapp
 */

public interface FilterView extends MvpView {

    void toggleRelevanceDistance(boolean relevanceTrueDistanceFalse);

    void togglePriceTier(int tier);

}
