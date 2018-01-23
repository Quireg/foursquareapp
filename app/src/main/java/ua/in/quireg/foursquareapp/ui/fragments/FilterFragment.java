package ua.in.quireg.foursquareapp.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.ui.adapters.PlacesListRecyclerViewAdapter;

/**
 * Created by Arcturus Mengsk on 1/23/2018, 5:42 PM.
 * foursquareapp
 */

public class FilterFragment extends MvpFragment {


    @OnClick(R.id.relevance_button)
    void setSortbyRelevance() {

    }
    @OnClick(R.id.distance_button)
    void setSortbyDistance() {

    }
    @OnClick(R.id.price_1_button)
    void toggleTier1Prices() {

    }
    @OnClick(R.id.price_2_button)
    void toggleTier2Prices() {

    }
    @OnClick(R.id.price_3_button)
    void toggleTier3Prices() {

    }
    @OnClick(R.id.price_4_button)
    void toggleTier4Prices() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);

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
}
