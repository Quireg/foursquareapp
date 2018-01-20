package ua.in.quireg.foursquareapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.mvp.models.presentation.PlaceEntity;
import ua.in.quireg.foursquareapp.mvp.presenters.PlacesListPresenter;
import ua.in.quireg.foursquareapp.mvp.views.PlacesListView;
import ua.in.quireg.foursquareapp.ui.adapters.PlacesListRecyclerViewAdapter;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 4:09 PM.
 * foursquareapp
 */

public class PlacesListFragment extends MvpAppCompatFragment implements PlacesListView {

    @BindView(R.id.listView) protected RecyclerView mRecyclerView;
    @BindView(R.id.loadingView) protected View mLoadingView;

    @InjectPresenter(type = PresenterType.WEAK)
    PlacesListPresenter mPlacesListPresenter;

    PlacesListRecyclerViewAdapter mPlacesListRecyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mPlacesListRecyclerViewAdapter = new PlacesListRecyclerViewAdapter(getContext());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);

        //SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_places_screen, container, false);

        ButterKnife.bind(this, view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mPlacesListRecyclerViewAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        return view;

    }

    @Override
    public void showLoading() {
        mRecyclerView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPlaces(List<PlaceEntity> places) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
        mPlacesListRecyclerViewAdapter.updateList(places);
        System.out.println("Stub!");
    }
}
