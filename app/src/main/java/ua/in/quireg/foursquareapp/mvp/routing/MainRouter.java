package ua.in.quireg.foursquareapp.mvp.routing;

import android.support.annotation.StringRes;

import ru.terrakok.cicerone.BaseRouter;
import ru.terrakok.cicerone.commands.Command;
import ua.in.quireg.foursquareapp.mvp.routing.commands.NavigateBackwards;
import ua.in.quireg.foursquareapp.mvp.routing.commands.NavigateFilterScreen;
import ua.in.quireg.foursquareapp.mvp.routing.commands.NavigateLocationPickerScreen;
import ua.in.quireg.foursquareapp.mvp.routing.commands.NavigatePlacesListScreen;
import ua.in.quireg.foursquareapp.mvp.routing.commands.RequestPermissions;
import ua.in.quireg.foursquareapp.mvp.routing.commands.SendShortToast;
import ua.in.quireg.foursquareapp.mvp.routing.commands.ShowError;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 3:39 PM.
 * foursquareapp
 */

public final class MainRouter extends BaseRouter {

    public void showSystemMessage(@StringRes int stringId) {
        executeCommand(new SendShortToast(stringId));
    }

    public void placesListScreen() {
        executeCommand(new NavigatePlacesListScreen());
    }

    public void filterScreen() {
        executeCommand(new NavigateFilterScreen());
    }

    public void placeExtendedScreen() {
        executeCommand(new NavigateBackwards());
    }

    public void locationPickerScreen() {
        executeCommand(new NavigateLocationPickerScreen());
    }

    public void requestPermissions() {
        executeCommand(new RequestPermissions());
    }

    public void showErrorDialog(String text) {
        executeCommand(new ShowError(text));
    }

    @Override
    protected void executeCommand(Command command) {
        super.executeCommand(command);
    }

}
