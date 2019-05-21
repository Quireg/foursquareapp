package ua.in.quireg.foursquareapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.mvp.presenters.FilterScreenPresenter;
import ua.in.quireg.foursquareapp.mvp.views.FilterView;
import ua.in.quireg.foursquareapp.ui.activities.MainActivity;

/**
 * Created by Arcturus Mengsk on 1/23/2018, 5:42 PM.
 * foursquareapp
 */

public class FilterFragment extends MvpFragment implements FilterView {

    @BindView(R.id.relevance_button) protected RadioButton relevance_button;
    @BindView(R.id.distance_button) protected RadioButton distance_button;
    @BindView(R.id.price_1_button) protected ToggleButton price_1_button;
    @BindView(R.id.price_2_button) protected ToggleButton price_2_button;
    @BindView(R.id.price_3_button) protected ToggleButton price_3_button;
    @BindView(R.id.price_4_button) protected ToggleButton price_4_button;
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
            case android.R.id.home: {
                getActivity().onBackPressed();
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_screen, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((MainActivity) getActivity())
                .getSupportActionBar()).setTitle(R.string.filter_screen_title);
        Objects.requireNonNull(((MainActivity) getActivity())
                .getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.relevance_button)
    void setSortByRelevance() {
        mFilterScreenPresenter.onRelevanceClick();
    }

    @OnClick(R.id.distance_button)
    void setSortByDistance() {
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

    @OnClick(R.id.select_custom_location_button)
    void pickLocation() {
        mFilterScreenPresenter.pickLocation();
    }

    @OnClick(R.id.clear_custom_location_button)
    void resetLocation() {
        mFilterScreenPresenter.resetLocation();
    }

    @Override
    public void toggleRelevance(boolean relevance) {
        relevance_button.setChecked(relevance);
        distance_button.setChecked(!relevance);
    }

    @Override
    public void togglePriceTier(int tier) {
        // can't figure out better way to parse 4 bits of data =/
        if (tier - 8 >= 0) {
            tier = tier - 8;
            price_4_button.setChecked(true);
        } else {
            price_4_button.setChecked(false);
        }
        if (tier - 4 >= 0) {
            tier = tier - 4;
            price_3_button.setChecked(true);
        } else {
            price_3_button.setChecked(false);
        }
        if (tier - 2 >= 0) {
            tier = tier - 2;
            price_2_button.setChecked(true);
        } else {
            price_2_button.setChecked(false);
        }
        if (tier == 1) {
            price_1_button.setChecked(true);
        } else {
            price_1_button.setChecked(false);
        }
    }

    @Override
    public void setLocation(String text) {
        current_loc_textview.setText(text);
    }

    private void onPriceTierButtonClick() {
        int i = 0;
        if (price_1_button.isChecked()) i = i + 1;
        if (price_2_button.isChecked()) i = i + 2;
        if (price_3_button.isChecked()) i = i + 4;
        if (price_4_button.isChecked()) i = i + 8;
        mFilterScreenPresenter.updatePriceFilter(i);
    }
}
