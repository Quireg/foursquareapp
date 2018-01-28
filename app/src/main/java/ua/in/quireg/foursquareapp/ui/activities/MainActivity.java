package ua.in.quireg.foursquareapp.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import javax.inject.Inject;

import ru.terrakok.cicerone.NavigatorHolder;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.common.PermissionManager;
import ua.in.quireg.foursquareapp.mvp.routing.MainNavigator;
import ua.in.quireg.foursquareapp.mvp.routing.MainRouter;
import ua.in.quireg.foursquareapp.ui.fragments.FilterFragment;
import ua.in.quireg.foursquareapp.ui.fragments.MapViewFragment;
import ua.in.quireg.foursquareapp.ui.fragments.PlacesListFragment;

public class MainActivity extends Activity {

    @Inject NavigatorHolder mNavigatorHolder;
    @Inject MainRouter mMainRouter;

    private Toast mToast;

    private MainNavigator mNavigator = new MainNavigator() {

        Fragment currentFragment;

        @Override
        public void sendShortToast(int stringId) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(getApplicationContext(), stringId, Toast.LENGTH_SHORT);
            mToast.show();
        }

        @Override
        public void requestPermissions() {
            PermissionManager.requestPermissions(MainActivity.this);
        }

        @Override
        public void showError(String errorText) {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(MainActivity.this);

            builder.setTitle("Oh! Error!")
                    .setMessage(errorText)
                    .setNegativeButton(R.string.error_copy_button, (dialog, which) -> {

                        ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        if (clipboard != null) {
                            clipboard.setPrimaryClip(ClipData.newPlainText("Error", errorText));
                            mMainRouter.showSystemMessage(R.string.error_done_text);
                        }
                    })
                    .setPositiveButton(R.string.error_ok_button, (dialog, which) -> {

                    })
                    .setIcon(R.drawable.sad_cloud)
                    .show();
        }

        @Override
        public void navigatePlacesList() {

            currentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);

            FragmentManager.enableDebugLogging(true);

            FragmentTransaction ft = getFragmentManager().beginTransaction();

            Fragment f = getFragmentManager().findFragmentByTag("placesList");

            if (f == null) {
                f = PlacesListFragment.getNewInstance("placesList");

                ft.add(R.id.fragment_container, f, "placesList");

            }

            if (currentFragment != null) {
                ft.detach(currentFragment);
            }

            ft.attach(f);
            ft.commit();

        }

        @Override
        public void navigateFilterScreen() {

            currentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);

            FragmentTransaction ft = getFragmentManager().beginTransaction();

            Fragment f = getFragmentManager().findFragmentByTag("filterFrag");

            if (f == null) {

                f = FilterFragment.getNewInstance("filterFrag");

                ft.add(R.id.fragment_container, f, "filterFrag");

            }

            if (currentFragment != null) {
                ft.detach(currentFragment);
            }

            ft.attach(f);

            ft.addToBackStack(null);

            ft.commit();


        }

        @Override
        public void navigateLocationPickerScreen() {
            FragmentTransaction ft = getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.frag_enter_right_left, R.animator.frag_exit_left_right, R.animator.frag_enter_right_left, R.animator.frag_exit_left_right)
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, new MapViewFragment(), "mf");
            ft.commit();
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FoursquareApplication.getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
        
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            mMainRouter.placesListScreen();
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
