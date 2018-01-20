package ua.in.quireg.foursquareapp.mvp.routing;

import android.support.annotation.StringRes;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.commands.Command;
import ua.in.quireg.foursquareapp.mvp.routing.commands.NavigatePlacesListScreen;
import ua.in.quireg.foursquareapp.mvp.routing.commands.SendShortToast;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 5:55 PM.
 * foursquareapp
 */

public abstract class MainNavigator implements Navigator {

    public abstract void sendShortToast(@StringRes int stringId);

    public abstract void navigatePlacesList();

    @Override
    public void applyCommand(Command command) {
        if (command instanceof SendShortToast) {
            sendShortToast(((SendShortToast) command).getToast());
        }

        if (command instanceof NavigatePlacesListScreen) {
            navigatePlacesList();
        }

    }
}