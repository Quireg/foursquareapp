package ua.in.quireg.foursquareapp.common;

import android.content.Context;
import android.content.res.Resources;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Arcturus Mengsk on 1/22/2018, 11:30 PM.
 * foursquareapp
 */

public class ResourceManager {

    private Context mContext;

    public ResourceManager(Context context) {
        mContext = context;
    }

    public InputStream getFileInputStreamFromAssets(String name) throws IOException {
        return mContext.getAssets().open(name);
    }

    public Resources getResources() {
        return mContext.getResources();
    }

}
