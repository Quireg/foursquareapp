package ua.in.quireg.foursquareapp.ui.views;

/**
 * Created by Arcturus Mengsk on 27.04.2019, 7:26.
 * foursquareapp
 */
public interface IOnBackPressed {
    /**
     * If you return true the back press will not be taken into account, otherwise the activity will act naturally
     * @return true if your processing has priority if not false
     */
    boolean onBackPressed();
}
