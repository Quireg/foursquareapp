package ua.in.quireg.foursquareapp.mvp.routing.commands;

import ru.terrakok.cicerone.commands.Command;

/**
 * Created by Arcturus Mengsk on 1/29/2018, 5:48 PM.
 * foursquareapp
 */

public class NavigatePlaceScreen implements Command {

    private String id;

    public NavigatePlaceScreen(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
