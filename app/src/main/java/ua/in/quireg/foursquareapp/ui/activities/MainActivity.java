package ua.in.quireg.foursquareapp.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
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
                    .setPositiveButton(R.string.error_copy_button, (dialog, which) -> {

                        ClipboardManager clipboard = (ClipboardManager) MainActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                        if (clipboard != null) {
                            clipboard.setPrimaryClip(ClipData.newPlainText("Error", errorText));
                            mMainRouter.showSystemMessage(R.string.error_copied_text);
                        }
                    })
                    .setIcon(R.drawable.sad_cloud)
                    .show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FoursquareApplication.getAppComponent().inject(this);

        setContentView(R.layout.activity_main);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMainRouter.placesListScreen();
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
