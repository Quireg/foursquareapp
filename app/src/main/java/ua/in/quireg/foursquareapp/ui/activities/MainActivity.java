package ua.in.quireg.foursquareapp.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.terrakok.cicerone.NavigatorHolder;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.mvp.routing.MainNavigator;
import ua.in.quireg.foursquareapp.mvp.routing.MainRouter;
import ua.in.quireg.foursquareapp.ui.fragments.PlacesListFragment;

public class MainActivity extends AppCompatActivity {

    @Inject NavigatorHolder mNavigatorHolder;
    @Inject MainRouter mMainRouter;

    @BindView(R.id.toolbar) protected Toolbar mToolbar;

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
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, new PlacesListFragment())
                    .commit();

            getSupportActionBar().setLogo(R.drawable.ic_logo);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FoursquareApplication.getAppComponent().inject(this);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMainRouter.navigatePlacesListScreen();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        mNavigatorHolder.setNavigator(mNavigator);
    }

    @Override
    protected void onPause() {
        mNavigatorHolder.removeNavigator();
        super.onPause();
    }

}
