package ua.in.quireg.foursquareapp.mvp.views;

import android.net.Uri;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import ua.in.quireg.foursquareapp.models.PlaceEntity;
import ua.in.quireg.foursquareapp.models.TipEntity;

/**
 * Created by Arcturus Mengsk on 1/29/2018, 8:32 PM.
 * foursquareapp
 */

public interface PlaceDetailsView extends MvpView {

    void setPlaceInfo(PlaceEntity e);

    void setPlacePhotos(List<Uri> photos);

    void setPlaceTip(TipEntity tip);

    void toggleTipsLoading(boolean isLoading);

    void toggleTipsLayout(boolean visible);
}
