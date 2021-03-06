package ua.in.quireg.foursquareapp.mvp.routing;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.commands.Command;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.common.PermissionManager;
import ua.in.quireg.foursquareapp.mvp.presenters.MainActivityPresenter;
import ua.in.quireg.foursquareapp.mvp.routing.commands.NavigateFilterScreen;
import ua.in.quireg.foursquareapp.mvp.routing.commands.NavigateLocationPickerScreen;
import ua.in.quireg.foursquareapp.mvp.routing.commands.NavigatePlaceScreen;
import ua.in.quireg.foursquareapp.mvp.routing.commands.NavigatePlacesListScreen;
import ua.in.quireg.foursquareapp.mvp.routing.commands.NavigateWelcomeScreen;
import ua.in.quireg.foursquareapp.mvp.routing.commands.RequestPermissions;
import ua.in.quireg.foursquareapp.mvp.routing.commands.SendShortToast;
import ua.in.quireg.foursquareapp.mvp.routing.commands.ShowError;
import ua.in.quireg.foursquareapp.ui.fragments.FilterFragment;
import ua.in.quireg.foursquareapp.ui.fragments.MapViewFragment;
import ua.in.quireg.foursquareapp.ui.fragments.PlaceDetailsFragment;
import ua.in.quireg.foursquareapp.ui.fragments.PlacesListFragment;
import ua.in.quireg.foursquareapp.ui.fragments.WelcomeFragment;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 5:55 PM.
 * foursquareapp
 */

@SuppressWarnings("WeakerAccess")
public class MainNavigator implements Navigator {

    private static final String API_QUOTA_EXCEEDED_ERROR = "HTTP 429 ";

    @Inject
    protected BackStack mBackStack;

    private Toast mToast;
    private AppCompatActivity mActivity;
    private Fragment mCurrentFragment;

    public MainNavigator(AppCompatActivity activity) {
        FoursquareApplication.getAppComponent().inject(this);
        mActivity = activity;
    }

    @Override
    public void applyCommand(Command command) {
        if (command instanceof SendShortToast) {
            sendShortToast(((SendShortToast) command).getToast());
        }
        if (command instanceof NavigatePlacesListScreen) {
            navigatePlacesList();
        }
        if (command instanceof NavigateWelcomeScreen) {
            navigateWelcomeScreen();
        }
        if (command instanceof RequestPermissions) {
            requestPermissions();
        }
        if (command instanceof ShowError) {
            if (((ShowError) command).getErrorText().equals(API_QUOTA_EXCEEDED_ERROR)) {
                sendShortToast(R.string.error_api_quota);
            } else {
                showError(((ShowError) command).getErrorText());
            }
        }
        if (command instanceof NavigateFilterScreen) {
            navigateFilterScreen();
        }
        if (command instanceof NavigateLocationPickerScreen) {
            navigateLocationPickerScreen();
        }
        if (command instanceof NavigatePlaceScreen) {
            navigatePlaceScreen(((NavigatePlaceScreen) command).getId());
        }
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    public boolean onBackPressed() {
        boolean consumed = false;
        Fragment f = mBackStack.popFragment();
        if (f != null) {
            consumed = true;
            navigateToFragment(f);
        }
        return consumed;
    }

    public void sendShortToast(int stringId) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(mActivity.getApplicationContext(), stringId, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public void requestPermissions() {
        PermissionManager.requestPermissions(mActivity);
    }

    private void navigateToFragment(Fragment f) {
        mCurrentFragment = f;
        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mCurrentFragment)
                .commit();
    }

    public void navigateWelcomeScreen() {
        mCurrentFragment = new WelcomeFragment();
        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mCurrentFragment)
                .commit();
    }

    public void navigatePlacesList() {
        mCurrentFragment = new PlacesListFragment();
        mBackStack.pushFragment(mCurrentFragment);
        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mCurrentFragment)
                .commit();
    }

    public void navigateFilterScreen() {
        mCurrentFragment = new FilterFragment();
        mBackStack.pushFragment(mCurrentFragment);
        mActivity.getFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.animator.frag_enter_right_left,
                        R.animator.frag_exit_right_left,
                        R.animator.frag_enter_pop_left_right,
                        R.animator.frag_exit_pop_left_right)
                .replace(R.id.fragment_container, mCurrentFragment)
                .commit();
    }

    public void navigateLocationPickerScreen() {
        mCurrentFragment = new MapViewFragment();
        mBackStack.pushFragment(mCurrentFragment);
        mActivity.getFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.animator.frag_enter_right_left,
                        R.animator.frag_exit_right_left,
                        R.animator.frag_enter_pop_left_right,
                        R.animator.frag_exit_pop_left_right)
                .replace(R.id.fragment_container, mCurrentFragment)
                .commit();
    }

    public void navigatePlaceScreen(String id) {
        mCurrentFragment = PlaceDetailsFragment.getNewInstance(id);
        mBackStack.pushFragment(mCurrentFragment);
        mActivity.getFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.animator.frag_enter_right_left,
                        R.animator.frag_exit_right_left,
                        R.animator.frag_enter_pop_left_right,
                        R.animator.frag_exit_pop_left_right)
                .replace(R.id.fragment_container, mCurrentFragment)
                .commit();
    }

    public void showError(String errorText) {
        new Handler(Looper.getMainLooper()).post(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle(R.string.oh_error)
                    .setMessage(errorText)
                    .setNegativeButton(R.string.error_copy_button, (dialog, which) -> {
                        ClipboardManager clipboard = (ClipboardManager) mActivity
                                .getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        if (clipboard != null) {
                            clipboard.setPrimaryClip(ClipData.newPlainText("Error", errorText));
                            sendShortToast(R.string.error_done_text);
                        }
                    })
                    .setPositiveButton(R.string.error_ok_button, (dialog, which) -> {

                    })
                    .setIcon(R.drawable.sad_cloud)
                    .show();
        });
    }
}
