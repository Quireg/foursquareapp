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
import android.widget.SearchView;
import android.widget.Toolbar;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.mvp.models.presentation.PlaceEntity;
import ua.in.quireg.foursquareapp.mvp.presenters.PlacesListPresenter;
import ua.in.quireg.foursquareapp.mvp.views.PlacesListView;
import ua.in.quireg.foursquareapp.ui.adapters.PlacesListRecyclerViewAdapter;
import ua.in.quireg.foursquareapp.ui.views.LeftPaddedDivider;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 4:09 PM.
 * foursquareapp
 */

public class PlacesListFragment extends MvpFragment implements PlacesListView {

    @BindView(R.id.places_list) protected RecyclerView mRecyclerView;
    @BindView(R.id.loadingView) protected View mLoadingView;
    @BindView(R.id.errorView) protected View mErrorView;
//    @BindView(R.id.toolbar_) Toolbar mToolbar;

    private SearchView mSearchView;

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

        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
//        mSearchView.setOnFocusChangeListener((view, b) -> {
//            if(view.getId() == R.id.search && !view.hasFocus()) {
//
//                InputMethodManager imm =  (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                }
//
//            }
//        });
//        mSearchView.setIconified(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh: {
                mPlacesListPresenter.refresh();
                return true;
            }
        }
        return false;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_places_screen, container, false);

        ButterKnife.bind(this, view);
//        getActivity().setActionBar(mToolbar);
//        getActivity().getActionBar().setLogo(R.drawable.appbar_logo_extended_margin);
//        getActivity().getActionBar().setDisplayShowHomeEnabled(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new LeftPaddedDivider(getActivity(), LinearLayoutManager.VERTICAL));

//        SlideInLeftAnimator animator = new SlideInLeftAnimator();
//        animator.setInterpolator(new OvershootInterpolator());
//
//        mRecyclerView.setItemAnimator(animator);

//        mRecyclerView.getItemAnimator().setAddDuration(10000);
//        mRecyclerView.getItemAnimator().setRemoveDuration(10000);
//        mRecyclerView.getItemAnimator().setMoveDuration(10000);
//        mRecyclerView.getItemAnimator().setChangeDuration(10000);

        mRecyclerView.setAdapter(mPlacesListRecyclerViewAdapter);

        return view;
    }

    @Override
    public void showLoadingView() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorView.setVisibility(View.INVISIBLE);
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void clearList() {
        mPlacesListRecyclerViewAdapter.clearList();
    }

    @Override
    public void updateListTitle(String title) {
        mPlacesListRecyclerViewAdapter.updateListTitle(title);
    }

    @Override
    public void showPlace(PlaceEntity place) {
        mLoadingView.setVisibility(View.INVISIBLE);
        mErrorView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mPlacesListRecyclerViewAdapter.addToList(place);
    }

    @Override
    public void showErrorView(String text) {
        mLoadingView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorView.setVisibility(View.VISIBLE);
    }
}
