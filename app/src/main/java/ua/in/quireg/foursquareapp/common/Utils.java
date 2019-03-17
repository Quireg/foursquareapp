package ua.in.quireg.foursquareapp.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Arcturus Mengsk on 1/20/2018, 9:45 AM.
 * foursquareapp
 */

public class Utils {

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile(String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    public static int convertPixelsToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isNotNullOrEmpty(String string) {
        return !isNullOrEmpty(string);
    }

    public static Point getDisplaySize(Context context) {

        Point p = new Point(0, 0);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            Display display = wm.getDefaultDisplay();
            display.getSize(p);
        }
        return p;
    }

    public static StateListDrawable getStateListDrawable(Context context, @DrawableRes int drawable) {

        Drawable stateSelected = ContextCompat.getDrawable(context, drawable);
        Drawable stateUnselected = stateSelected.getConstantState().newDrawable();

        stateUnselected.mutate();
        stateUnselected.setAlpha(126);

        StateListDrawable stateListDrawable = new StateListDrawable();

        stateListDrawable.addState(
                new int[]{android.R.attr.state_selected},
                stateSelected
        );
        stateListDrawable.addState(
                new int[]{android.R.attr.state_enabled},
                stateUnselected
        );

        return stateListDrawable;
    }

}
