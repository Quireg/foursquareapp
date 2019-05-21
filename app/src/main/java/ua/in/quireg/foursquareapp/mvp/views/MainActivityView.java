package ua.in.quireg.foursquareapp.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by Arcturus Mengsk on 27.04.2019, 6:48.
 * foursquareapp
 */

@StateStrategyType(SkipStrategy.class)
public interface MainActivityView extends MvpView {
}
