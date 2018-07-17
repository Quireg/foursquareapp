package ua.in.quireg.foursquareapp.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import ru.terrakok.cicerone.NavigatorHolder;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.mvp.routing.MainNavigator;
import ua.in.quireg.foursquareapp.mvp.routing.MainRouter;

public class MainActivity extends AppCompatActivity {

    @Inject NavigatorHolder mNavigatorHolder;
    @Inject MainRouter mMainRouter;

    private MainNavigator mNavigator = new MainNavigator(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FoursquareApplication.getAppComponent().inject(this);

        setContentView(R.layout.activity_main);

        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            mMainRouter.welcomeScreen();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNavigatorHolder.setNavigator(mNavigator);
    }

    @Override
    protected void onPause() {
        mNavigatorHolder.removeNavigator();
        super.onPause();
    }
}
