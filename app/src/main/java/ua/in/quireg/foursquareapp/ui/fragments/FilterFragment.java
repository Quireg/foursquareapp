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
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ua.in.quireg.foursquareapp.R;
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

    @BindView(R.id.current_loc_textview) protected TextView current_loc_textview;

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
                mFilterScreenPresenter.resetFilter();
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_filter_screen, container, false);

        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.relevance_button)
    void setSortbyRelevance() {
        mFilterScreenPresenter.onRelevanceClick();
    }

    @OnClick(R.id.distance_button)
    void setSortbyDistance() {
        mFilterScreenPresenter.onDistanceClick();
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
    public void toggleRelevance(boolean relevance) {

        if (relevance) {
            relevance_button.setPressed(true);
            distance_button.setPressed(false);
        } else {
            relevance_button.setPressed(false);
            distance_button.setPressed(true);
        }

    }

    @Override
    public void togglePriceTier(int tier) {

        // can't figure out better way to parse 4 bits of data =/

        if (tier % 8 == 1) {
            tier = tier - 8;
            price_4_button.setPressed(true);
        } else {
            price_4_button.setPressed(false);
        }

        if (tier % 4 == 1) {
            tier = tier - 4;
            price_3_button.setPressed(true);
        } else {
            price_3_button.setPressed(false);
        }

        if (tier % 2 == 1) {
            price_2_button.setPressed(true);
            tier = tier - 2;
        } else {
            price_2_button.setPressed(false);
        }
        if (tier == 1) {
            price_1_button.setPressed(true);
        } else {
            price_1_button.setPressed(false);
        }

    }

    @Override
    public void resetLocation(String text) {
        current_loc_textview.setText(text);
    }

    private void onPriceTierButtonClick() {
        int i = 0;
        if (price_1_button.isPressed()) i = +1;
        if (price_2_button.isPressed()) i = +2;
        if (price_3_button.isPressed()) i = +4;
        if (price_4_button.isPressed()) i = +8;

        mFilterScreenPresenter.updatePriceFilters(i);
    }

    @OnClick(R.id.select_custom_location_button)
    void pickLocation() {
        mFilterScreenPresenter.pickLocation();
    }

    @OnClick(R.id.clear_custom_location_button)
    void resetLocation() {
        mFilterScreenPresenter.resetLocation();
    }

}
