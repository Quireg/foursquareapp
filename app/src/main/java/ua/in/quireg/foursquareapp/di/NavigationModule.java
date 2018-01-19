package ua.in.quireg.foursquareapp.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;
import ua.in.quireg.foursquareapp.mvp.routing.MainRouter;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 3:53 PM.
 * foursquareapp
 */

@Module
public class NavigationModule {

    private Cicerone<MainRouter> cicerone;

    public NavigationModule() {
        cicerone = Cicerone.create(new MainRouter());
    }

    @Provides
    @Singleton
    MainRouter provideRouter() {
        return cicerone.getRouter();
    }

    @Provides
    @Singleton
    NavigatorHolder provideNavigatorHolder() {
        return cicerone.getNavigatorHolder();
    }
}
