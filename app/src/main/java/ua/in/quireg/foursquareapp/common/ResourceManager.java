package ua.in.quireg.foursquareapp.common;

import android.content.Context;
import android.content.res.Resources;

import java.io.IOException;
import java.io.InputStream;

import ua.in.quireg.foursquareapp.FoursquareApplication;

/**
 * Created by Arcturus Mengsk on 1/22/2018, 11:30 PM.
 * foursquareapp
 */

public class ResourceManager {

    private FoursquareApplication mApplication;

    public ResourceManager(FoursquareApplication application) {
        mApplication = application;
    }

    public InputStream getFileInputStreamFromAssets(String name) throws IOException {
        return mApplication.getResources().getAssets().open(name);
    }

    public Resources getResources() {
        return mApplication.getResources();
    }




}
