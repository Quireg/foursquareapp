package ua.in.quireg.foursquareapp.mvp.routing;

import android.support.annotation.StringRes;

import ru.terrakok.cicerone.BaseRouter;
import ru.terrakok.cicerone.commands.Command;
import ua.in.quireg.foursquareapp.mvp.routing.commands.NavigateBackwards;
import ua.in.quireg.foursquareapp.mvp.routing.commands.NavigatePlacesListScreen;
import ua.in.quireg.foursquareapp.mvp.routing.commands.SendShortToast;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 3:39 PM.
 * foursquareapp
 */

public final class MainRouter extends BaseRouter {

    public void showSystemMessage(@StringRes int stringId) {
        executeCommand(new SendShortToast(stringId));
    }

    public void onBackPressed() {
        executeCommand(new NavigateBackwards());
    }

    public void navigatePlacesListScreen() {
        executeCommand(new NavigatePlacesListScreen());
    }

    public void navigateFilterScreen() {
        executeCommand(new NavigateBackwards());
    }

    public void navigatePlaceScreen() {
        executeCommand(new NavigateBackwards());
    }

    @Override
    protected void executeCommand(Command command) {
        super.executeCommand(command);
    }

}
