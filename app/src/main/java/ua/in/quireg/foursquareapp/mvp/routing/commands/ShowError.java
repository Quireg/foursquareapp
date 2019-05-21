package ua.in.quireg.foursquareapp.mvp.routing.commands;

import ru.terrakok.cicerone.commands.Command;

/**
 * Created by Arcturus Mengsk on 1/23/2018, 1:34 PM.
 * foursquareapp
 */

public class ShowError implements Command {

    private String mErrorText = "";

    public ShowError(String errorText) {
        mErrorText = mErrorText.concat(errorText);
    }

    public String getErrorText() {
        return mErrorText;
    }
}
