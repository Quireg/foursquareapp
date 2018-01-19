package ua.in.quireg.foursquareapp.mvp.routing.commands;

import android.support.annotation.StringRes;

import ru.terrakok.cicerone.commands.Command;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 4:02 PM.
 * foursquareapp
 */

public class SendShortToast implements Command {

    private int stringResource;

    public SendShortToast(@StringRes int i) {
        this.stringResource = i;
    }

    public int getToast() {
        return stringResource;
    }
}
