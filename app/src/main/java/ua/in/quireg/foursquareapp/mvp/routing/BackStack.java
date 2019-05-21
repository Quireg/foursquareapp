package ua.in.quireg.foursquareapp.mvp.routing;

import android.app.Fragment;

import java.util.Iterator;
import java.util.LinkedList;

import timber.log.Timber;

/**
 * Created by Arcturus Mengsk on 27.04.2019, 8:41.
 * foursquareapp
 */

public class BackStack {

    private LinkedList<Fragment> mBackStack = new LinkedList<>();

    public Fragment popFragment() {
        Timber.d("popFragment()");
        if (mBackStack.size() > 0) {
            mBackStack.remove();
            if (mBackStack.size() > 0) {
                return mBackStack.getFirst();
            }
        }
        return null;
    }

    public <T extends Fragment> void pushFragment(T f) {
        Timber.d("pushFragment()");
        removeDuplicates(f.getClass());
        mBackStack.push(f);
    }

    private <T extends Fragment> void removeDuplicates(Class<T> type) {
        Iterator<Fragment> i = mBackStack.iterator();
        while (i.hasNext()) {
            Fragment iterated = i.next();
            if (type.isInstance(iterated)) {
                Timber.d("remove duplicate " + iterated);
                i.remove();
            }
        }
    }
}
