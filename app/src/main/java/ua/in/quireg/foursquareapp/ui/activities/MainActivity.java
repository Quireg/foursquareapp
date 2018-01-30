package ua.in.quireg.foursquareapp.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
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
import ua.in.quireg.foursquareapp.ui.fragments.PlaceDetailsFragment;
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

            FragmentManager.enableDebugLogging(true);

            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.frag_enter_up_down, R.animator.frag_exit_down_up, R.animator.frag_enter_up_down, R.animator.frag_exit_down_up)
                    .replace(R.id.fragment_container, new PlacesListFragment())
                    .commit();

        }

        @Override
        public void navigateFilterScreen() {

            getFragmentManager().beginTransaction()
                    .setCustomAnimations(
                            R.animator.frag_enter_right_left, //fragment entering
                            R.animator.frag_exit_right_left, //fragment leaving
                            R.animator.frag_enter_pop_left_right, //fragment returning
                            R.animator.frag_exit_pop_left_right //fragment leaving
                    )
                    .replace(R.id.fragment_container, new FilterFragment())
                    .addToBackStack(null)
                    .commit();

        }

        @Override
        public void navigateLocationPickerScreen() {
            FragmentTransaction ft = getFragmentManager().beginTransaction()
                    .setCustomAnimations(
                            R.animator.frag_enter_right_left,
                            R.animator.frag_exit_right_left,
                            R.animator.frag_enter_pop_left_right,
                            R.animator.frag_exit_pop_left_right
                    )
                    .replace(R.id.fragment_container, new MapViewFragment(), "mf")
                    .addToBackStack(null);
            ft.commit();
        }

        @Override
        public void navigatePlaceScreen(String id) {
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(
                            R.animator.frag_enter_right_left,
                            R.animator.frag_exit_right_left,
                            R.animator.frag_enter_pop_left_right,
                            R.animator.frag_exit_pop_left_right
                    )
                    .replace(R.id.fragment_container, PlaceDetailsFragment.getNewInstance(id))
                    .addToBackStack(null)
                    .commit();
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
