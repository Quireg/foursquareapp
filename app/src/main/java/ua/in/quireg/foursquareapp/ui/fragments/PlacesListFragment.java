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
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.models.PlaceEntity;
import ua.in.quireg.foursquareapp.mvp.presenters.PlacesListPresenter;
import ua.in.quireg.foursquareapp.mvp.views.PlacesListView;
import ua.in.quireg.foursquareapp.ui.activities.MainActivity;
import ua.in.quireg.foursquareapp.ui.adapters.PlacesListRecyclerViewAdapter;
import ua.in.quireg.foursquareapp.ui.views.IOnBackPressed;
import ua.in.quireg.foursquareapp.ui.views.LeftPaddedDivider;

import static ua.in.quireg.foursquareapp.FoursquareApplication.PREF_MOC_LOC_KEY;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 4:09 PM.
 * foursquareapp
 */

public class PlacesListFragment extends MvpFragment implements PlacesListView, IOnBackPressed {

    @BindView(R.id.places_list) protected RecyclerView mRecyclerView;
    @BindView(R.id.header_textview) protected TextView mHeaderView;
    @BindView(R.id.shadowline) protected View mShadowLine;
    @BindView(R.id.progress_bar) protected ProgressBar mProgressBar;
    @BindView(R.id.main_layout) protected RelativeLayout mLayout;
    private SearchView mSearchView;

    @SuppressWarnings("FieldCanBeLocal")
    private final int SEARCH_QUERY_DEBOUNCE_TIMEOUT = 1000;

    @InjectPresenter(type = PresenterType.GLOBAL)
    PlacesListPresenter mPlacesListPresenter;
    @Inject
    SharedPreferences mSharedPreferences;
    PlacesListRecyclerViewAdapter mPlacesListRecyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        FoursquareApplication.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPlacesListRecyclerViewAdapter = new PlacesListRecyclerViewAdapter(getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);
        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        initMenuSearch();
        initMenuMockLoc(menu.findItem(R.id.mock_location));
        initMenuAnimations(menu.findItem(R.id.animations));
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
        mUnbinder = ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new LeftPaddedDivider(getActivity()));
        mRecyclerView.setAdapter(mPlacesListRecyclerViewAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((MainActivity) getActivity())
                .getSupportActionBar()).setTitle(R.string.app_name);
//        Objects.requireNonNull(((MainActivity) getActivity())
//                .getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void setListTitle(String title) {
        Timber.d("setListTitle(%s)", title);
        mShadowLine.setVisibility(View.VISIBLE);
        mHeaderView.setText(title);
    }

    @Override
    public void setList(List<PlaceEntity> places) {
        Timber.d("setList()");
        mShadowLine.setVisibility(View.VISIBLE);
        mPlacesListRecyclerViewAdapter.addAll(places);
    }

    @Override
    public void cancelSearch() {
        if (mSearchView != null && !mSearchView.isIconified()) {
            mSearchView.onActionViewCollapsed();
        }
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

    private void initMenuAnimations(MenuItem menuItem) {
        menuItem.setChecked(mSharedPreferences.getBoolean("key_animations", true));
        menuItem.setOnMenuItemClickListener(item -> {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            boolean newState = !item.isChecked();
            item.setChecked(newState);
            editor.putBoolean("key_animations", newState);
            editor.apply();
            return true;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initMenuSearch() {
        mSearchView.setImeOptions(mSearchView.getImeOptions() |
                        EditorInfo.IME_FLAG_NO_EXTRACT_UI |
                        EditorInfo.IME_ACTION_SEARCH |
                        EditorInfo.IME_FLAG_NO_FULLSCREEN);

        mRecyclerView.setOnTouchListener((v, event) -> {
            //remove keyboard when user scrolls recyclerView
            if (mSearchView.hasFocus()) mSearchView.clearFocus();
            return false;
        });
        mSearchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && mSearchView.getQuery().length() == 0) {
                //In case focus lost and no input provided - close searchView
                mSearchView.setIconified(true);
            }
        });

        //Dispatches search once in a while during typing
        PublishSubject<String> onQueryChangedSubject = PublishSubject.create();
        //Always dispatches search request
        PublishSubject<String> onQuerySubmitSubject = PublishSubject.create();

        mPlacesListPresenter.onSearchRequest(
                onQuerySubmitSubject
                        .distinctUntilChanged()
                        .map(s -> {
                            Timber.d("onQueryTextDispatch(%s)", s);
                            return s;
                        }));

        onQueryChangedSubject
                .debounce(SEARCH_QUERY_DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe(onQuerySubmitSubject);

        SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Timber.d("onQueryTextSubmit(%s)", s);
                onQuerySubmitSubject.onNext(s);
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Timber.d("onQueryTextChange(%s)", s);
                onQueryChangedSubject.onNext(s);
                return true;
            }
        };
        mSearchView.setOnQueryTextListener(listener);
    }

    @Override
    public boolean onBackPressed() {
        return mPlacesListPresenter.onBackPressed();
    }
}
