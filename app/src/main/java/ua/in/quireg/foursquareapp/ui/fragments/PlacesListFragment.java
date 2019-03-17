package ua.in.quireg.foursquareapp.ui.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
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
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.models.PlaceEntity;
import ua.in.quireg.foursquareapp.mvp.presenters.PlacesListPresenter;
import ua.in.quireg.foursquareapp.mvp.views.PlacesListView;
import ua.in.quireg.foursquareapp.ui.activities.MainActivity;
import ua.in.quireg.foursquareapp.ui.adapters.PlacesListRecyclerViewAdapter;
import ua.in.quireg.foursquareapp.ui.views.LeftPaddedDivider;

import static ua.in.quireg.foursquareapp.FoursquareApplication.PREF_MOC_LOC_KEY;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 4:09 PM.
 * foursquareapp
 */

public class PlacesListFragment extends MvpFragment implements PlacesListView {

    @BindView(R.id.places_list) protected RecyclerView mRecyclerView;
    @BindView(R.id.header_textview) protected TextView mHeaderView;
    @BindView(R.id.shadowline) protected View mShadowLine;
    @BindView(R.id.progress_bar) protected ProgressBar mProgressBar;
    @BindView(R.id.main_layout) protected RelativeLayout mLayout;

    @SuppressWarnings("FieldCanBeLocal")
    private final int SEARCH_QUERY_DEBOUNCE_TIMEOUT = 2000;

    @InjectPresenter(type = PresenterType.GLOBAL)
    PlacesListPresenter mPlacesListPresenter;
    @Inject SharedPreferences mSharedPreferences;
    PlacesListRecyclerViewAdapter mPlacesListRecyclerViewAdapter;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        FoursquareApplication.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPlacesListRecyclerViewAdapter = new PlacesListRecyclerViewAdapter(getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);
        initMenuSearch(menu.findItem(R.id.search));
        initMenuMockLoc(menu.findItem(R.id.mock_location));
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_screen, container, false);
        unbinder = ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new LeftPaddedDivider(getActivity()));
        mRecyclerView.setAdapter(mPlacesListRecyclerViewAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
    }

    @Override
    public void setListTitle(String title) {
        Timber.d("setListTitle(%s)", title);

        if (mShadowLine.getVisibility() != View.VISIBLE) {
            mShadowLine.setVisibility(View.VISIBLE);
        }
        mHeaderView.setText(title);
    }

    @Override
    public void setList(List<PlaceEntity> places) {
        Timber.d("setList()");
        if (mShadowLine.getVisibility() != View.VISIBLE) {
            mShadowLine.setVisibility(View.VISIBLE);
        }
        mPlacesListRecyclerViewAdapter.addAll(places);
    }

    @Override
    public void clearList() {
        Timber.d("clearList()");
        mPlacesListRecyclerViewAdapter.clearList();
        mShadowLine.setVisibility(View.INVISIBLE);
    }

    @Override
    public void toggleLoadingView(boolean visible) {
        Timber.d("toggleLoadingView(%b)", visible);
        mProgressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCompositeDisposable.clear();
    }

    private void initMenuMockLoc(MenuItem mockLoc) {
        mockLoc.setChecked(mSharedPreferences.getBoolean(PREF_MOC_LOC_KEY, false));
        mockLoc.setOnMenuItemClickListener(item -> {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            boolean newState = !item.isChecked();
            item.setChecked(newState);
            editor.putBoolean(PREF_MOC_LOC_KEY, newState);
            editor.apply();
            return true;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initMenuSearch(MenuItem searchViewItem) {
        SearchView searchView = (SearchView) searchViewItem.getActionView();

        mRecyclerView.setOnTouchListener((v, event) -> {
            //remove keyboard when user scrolls recyclerView
            if (searchView.hasFocus()) searchView.clearFocus();
            return false;
        });
        int id = searchView.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        searchView.findViewById(id).setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && searchView.getQuery().length() == 0) {
                //In case focus lost and no input provided - close searchView
                searchView.setIconified(true);
            }
        });
        searchView.setOnCloseListener(() -> {
            mPlacesListPresenter.onSearchCanceled();
            return false;
        });

        PublishSubject<String> onQueryChangedSubject = PublishSubject.create();
        PublishSubject<String> onQuerySubmitSubject = PublishSubject.create();

        onQueryChangedSubject
                .debounce(SEARCH_QUERY_DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe(onQuerySubmitSubject);

        onQuerySubmitSubject
                .distinctUntilChanged()
                .onErrorResumeNext(onQuerySubmitSubject)
                .subscribe(onQueryChangedSubject);


        SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Timber.d("onQueryTextSubmit()");
                Timber.d("onQueryTextSubmit(%s)", s);
                onQuerySubmitSubject.onNext(s);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mPlacesListPresenter.onSearchRequest(onQuerySubmitSubject);

                Timber.d("onQueryTextChange(%s)", s);
                onQueryChangedSubject.onNext(s);
                return true;
            }
        };
        searchView.setOnQueryTextListener(listener);
    }
}
