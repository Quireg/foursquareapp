package ua.in.quireg.foursquareapp.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;
import ru.terrakok.cicerone.NavigatorHolder;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.mvp.routing.MainNavigator;
import ua.in.quireg.foursquareapp.mvp.routing.MainRouter;
import ua.in.quireg.foursquareapp.ui.fragments.PlacesListFragment;

public class MainActivity extends Activity {

    @Inject NavigatorHolder mNavigatorHolder;
    @Inject MainRouter mMainRouter;

    private Toast mToast;

    private MainNavigator mNavigator = new MainNavigator() {
        @Override
        public void sendShortToast(int stringId) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(getApplicationContext(), stringId, Toast.LENGTH_SHORT);
            mToast.show();
        }

        @Override
        public void navigatePlacesList() {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new PlacesListFragment())
                    .commit();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        FoursquareApplication.getAppComponent().inject(this);

        ButterKnife.bind(this);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMainRouter.navigatePlacesListScreen();
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
