package ua.in.quireg.foursquareapp.mvp.routing;

import android.support.annotation.StringRes;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.commands.Command;
import ua.in.quireg.foursquareapp.mvp.routing.commands.NavigatePlacesListScreen;
import ua.in.quireg.foursquareapp.mvp.routing.commands.RequestPermissions;
import ua.in.quireg.foursquareapp.mvp.routing.commands.SendShortToast;
import ua.in.quireg.foursquareapp.mvp.routing.commands.ShowError;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 5:55 PM.
 * foursquareapp
 */

public abstract class MainNavigator implements Navigator {

    public abstract void sendShortToast(@StringRes int stringId);

    public abstract void navigatePlacesList();

    public abstract void requestPermissions();

    public abstract void showError(String text);

    @Override
    public void applyCommand(Command command) {
        if (command instanceof SendShortToast) {
            sendShortToast(((SendShortToast) command).getToast());
        }

        if (command instanceof NavigatePlacesListScreen) {
            navigatePlacesList();
        }

        if (command instanceof RequestPermissions) {
            requestPermissions();
        }

        if (command instanceof ShowError) {
            showError(((ShowError) command).getErrorText());
        }

    }
}
