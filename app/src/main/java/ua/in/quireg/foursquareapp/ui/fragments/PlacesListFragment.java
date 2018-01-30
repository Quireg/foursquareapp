package ua.in.quireg.foursquareapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.models.PlaceEntity;
import ua.in.quireg.foursquareapp.mvp.presenters.PlacesListPresenter;
import ua.in.quireg.foursquareapp.mvp.views.PlacesListView;
import ua.in.quireg.foursquareapp.ui.adapters.PlacesListRecyclerViewAdapter;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 4:09 PM.
 * foursquareapp
 */

public class PlacesListFragment extends MvpFragment implements PlacesListView {

    @BindView(R.id.places_list) protected RecyclerView mRecyclerView;
    @BindView(R.id.header_textview) protected TextView mHeaderView;
    @BindView(R.id.shadowline) protected View mShadowline;
    @BindView(R.id.progress_bar) protected ProgressBar mProgressBar;

    @InjectPresenter(type = PresenterType.WEAK)
    PlacesListPresenter mPlacesListPresenter;

    PlacesListRecyclerViewAdapter mPlacesListRecyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mPlacesListRecyclerViewAdapter = new PlacesListRecyclerViewAdapter(getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.search);

        initMenuSearch(searchViewItem);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh: {
                mPlacesListPresenter.startNegotiation();
                return true;
            }
            case R.id.filter: {
                mPlacesListPresenter.openFilterScreen();
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
            getActivity().getActionBar().setTitle(R.string.app_name);
        }

        View view = inflater.inflate(R.layout.fragment_list_screen, container, false);

        unbinder = ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.addItemDecoration(new LeftPaddedDivider(getActivity(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mPlacesListRecyclerViewAdapter);

        return view;
    }

    @Override
    public void setListTitle(String title) {
        if (mShadowline.getVisibility() != View.VISIBLE) {
            mShadowline.setVisibility(View.VISIBLE);
        }
        mHeaderView.setText(title);
    }

    @Override
    public void addToList(PlaceEntity place) {
        if (mShadowline.getVisibility() != View.VISIBLE) {
            mShadowline.setVisibility(View.VISIBLE);
        }
        mPlacesListRecyclerViewAdapter.addToList(place);
    }

    @Override
    public void clear() {
        mPlacesListRecyclerViewAdapter.clearList();
        mShadowline.setVisibility(View.INVISIBLE);
    }

    @Override
    public void toggleLoadingView(boolean visible) {
        if (visible) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void initMenuSearch(MenuItem searchViewItem) {

        SearchView searchView = (SearchView) searchViewItem.getActionView();

        Observable<String> stringObservable = Observable.defer(
                () -> Observable.create(
                        searchRequested -> {
                                    Observable.create(searchTextChanged -> {

                                        searchView.setOnQueryTextListener(
                                                new SearchView.OnQueryTextListener() {
                                                    @Override
                                                    public boolean onQueryTextSubmit(String s) {
                                                        searchRequested.onNext(s);
                                                        searchView.clearFocus();
                                                        return true;
                                                    }

                                                    @Override
                                                    public boolean onQueryTextChange(String s) {
                                                        searchTextChanged.onNext(s);
                                                        return true;
                                                    }
                                                });

                                    })
                                    .debounce(1000, TimeUnit.MILLISECONDS)
                                    .subscribe(searchRequested::onNext);

                        })
                        .cast(String.class)
                        .distinctUntilChanged()
        );

        searchViewItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                mPlacesListPresenter.onSearchRequest(stringObservable);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mPlacesListPresenter.onSearchCanceled();
                return true;
            }
        });

    }

}
