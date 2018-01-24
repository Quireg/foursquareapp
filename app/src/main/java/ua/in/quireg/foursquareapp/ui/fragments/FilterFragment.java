package ua.in.quireg.foursquareapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.common.Utils;
import ua.in.quireg.foursquareapp.mvp.presenters.FilterScreenPresenter;
import ua.in.quireg.foursquareapp.mvp.views.FilterView;

/**
 * Created by Arcturus Mengsk on 1/23/2018, 5:42 PM.
 * foursquareapp
 */

public class FilterFragment extends MvpFragment implements FilterView {

    @BindView(R.id.relevance_button) protected Button relevance_button;
    @BindView(R.id.distance_button) protected Button distance_button;
    @BindView(R.id.price_1_button) protected Button price_1_button;
    @BindView(R.id.price_2_button) protected Button price_2_button;
    @BindView(R.id.price_3_button) protected Button price_3_button;
    @BindView(R.id.price_4_button) protected Button price_4_button;
    @BindView(R.id.clear_custom_location_button) protected ImageButton clear_custom_location_button;
    @BindView(R.id.select_custom_location_button) protected Button select_custom_location_button;

    @InjectPresenter(type = PresenterType.WEAK)
    FilterScreenPresenter mFilterScreenPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.filter_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset: {
                //TODO
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_filter_screen, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.relevance_button)
    void setSortbyRelevance() {

    }

    @OnClick(R.id.distance_button)
    void setSortbyDistance() {

    }

    @OnClick(R.id.price_1_button)
    void toggleTier1Prices() {
        onPriceTierButtonClick();
    }

    @OnClick(R.id.price_2_button)
    void toggleTier2Prices() {
        onPriceTierButtonClick();
    }

    @OnClick(R.id.price_3_button)
    void toggleTier3Prices() {
        onPriceTierButtonClick();
    }

    @OnClick(R.id.price_4_button)
    void toggleTier4Prices() {
        onPriceTierButtonClick();
    }

    @Override
    public void toggleRelevanceDistance(boolean relevanceTrueDistanceFalse) {
        if(relevanceTrueDistanceFalse) {
            relevance_button.setActivated(true);
            distance_button.setActivated(false);
        } else {
            relevance_button.setActivated(false);
            distance_button.setActivated(true);
        }

    }

    @Override
    public void togglePriceTier(int tier) {
        char[] e = Integer.toBinaryString(tier).toCharArray();

        price_1_button.setActivated(String.valueOf(e[0]).equals("1"));
        price_2_button.setActivated(String.valueOf(e[1]).equals("1"));
        price_3_button.setActivated(String.valueOf(e[2]).equals("1"));
        price_4_button.setActivated(String.valueOf(e[3]).equals("1"));

    }

    void onPriceTierButtonClick() {

        int i = Integer.parseInt(
                    String.format("%s%s%s%s",
                            Utils.booleanToInt(price_1_button.isActivated()),
                            Utils.booleanToInt(price_2_button.isActivated()),
                            Utils.booleanToInt(price_3_button.isActivated()),
                            Utils.booleanToInt(price_4_button.isActivated())
                    ), 2
        );
    }

}
