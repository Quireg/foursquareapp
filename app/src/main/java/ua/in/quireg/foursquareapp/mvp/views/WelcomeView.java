package ua.in.quireg.foursquareapp.mvp.views;

import android.support.annotation.StringRes;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by Arcturus Mengsk on 2/4/2018, 4:00 PM.
 * foursquareapp
 */

@StateStrategyType(SkipStrategy.class)
public interface WelcomeView extends MvpView {

    void animateEnterFragment(int duration);

    void animateExitFragment(int duration);

    void setReadyState();

    void setPermissionsRequiredState();

}
