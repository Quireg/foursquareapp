package ua.in.quireg.foursquareapp.ui.fragments;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.models.PlaceEntity;
import ua.in.quireg.foursquareapp.models.TipEntity;
import ua.in.quireg.foursquareapp.mvp.presenters.PlaceDetailsPresenter;
import ua.in.quireg.foursquareapp.mvp.views.PlaceDetailsView;

/**
 * Created by Arcturus Mengsk on 1/29/2018, 5:53 PM.
 * foursquareapp
 */

public class PlaceDetailsFragment extends MvpFragment implements PlaceDetailsView {

    private static final String EXTRA_PLACE_ID = "extra_name";
    private static final int MAP_BEARING = 0;
    private static final int MAP_TILT = 30;
    private static final int MAP_ZOOM = 14;

    @BindView(R.id.image1) ImageView mImage1;
    @BindView(R.id.image2) ImageView mImage2;
    @BindView(R.id.image3) ImageView mImage3;
    @BindView(R.id.rating) TextView mRating;
    @BindView(R.id.title) TextView mTitle;
    @BindView(R.id.price_category) TextView mPriceCategory;
    @BindView(R.id.category) TextView mCategory;
    @BindView(R.id.description) TextView mDescription;
    @BindView(R.id.address) TextView mAddress;
    @BindView(R.id.tips_not_found) TextView mNoTips;
    @BindView(R.id.tips_load_progress_bar) ProgressBar mTipsProgressBar;
    @BindView(R.id.tips_layout) LinearLayout mTipsLinearLayout;
    @BindView(R.id.root_scrollview) ScrollView mScrollView;

    @ProvidePresenter
    PlaceDetailsPresenter providePlaceDetailsPresenter() {
        return new PlaceDetailsPresenter(getArguments().getString(EXTRA_PLACE_ID));
    }

    @InjectPresenter
    PlaceDetailsPresenter mPlaceDetailsPresenter;

    public static PlaceDetailsFragment getNewInstance(String id) {
        PlaceDetailsFragment fragment = new PlaceDetailsFragment();
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_PLACE_ID, id);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        View view = inflater.inflate(R.layout.fragment_place_details_screen, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        mScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (mScrollView != null) {
                if (mScrollView.getChildAt(0).getBottom() <=
                        (mScrollView.getHeight() + mScrollView.getScrollY())) {
                    //scroll view is at bottom
                    mPlaceDetailsPresenter.loadMorePlaceTips();
                }
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void setPlaceInfo(PlaceEntity e) {
        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setTitle(e.getName());
        }

        mTitle.setText(e.getName());
        mRating.setText(e.getRating());
        mRating.setTextColor(Color.parseColor(e.getRatingColor()));
        mPriceCategory.setText(e.getPriceCategory());
        mCategory.setText(e.getType());
        mAddress.setText(e.getAddress());

        if (e.getDescription() != null) {
            mDescription.setVisibility(View.VISIBLE);
            mDescription.setText(e.getDescription());
        }

        ((MapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(googleMap -> {
            googleMap.setBuildingsEnabled(true);
            googleMap.getUiSettings().setMapToolbarEnabled(false);
            googleMap.setOnMapClickListener(latLng -> {
                //do nothing
            });

            LatLng latLng = new LatLng(e.getLocationEntity().getLat(), e.getLocationEntity().getLon());

            googleMap.addMarker(new MarkerOptions().position(latLng).title(e.getName()));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(MAP_ZOOM)
                    .tilt(MAP_TILT)
                    .bearing(MAP_BEARING)
                    .build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        });
    }

    @Override
    public void setPlacePhotos(List<Uri> photos) {
        if (photos.size() > 0) {
            Glide.with(getActivity().getApplicationContext())
                    .load(photos.get(0))
                    .into(mImage1);
        }
        if (photos.size() > 1) {
            Glide.with(getActivity().getApplicationContext())
                    .load(photos.get(1))
                    .into(mImage2);
        }
        if (photos.size() > 2) {
            Glide.with(getActivity().getApplicationContext())
                    .load(photos.get(2))
                    .into(mImage3);
        }
    }

    @Override
    public void setPlaceTip(TipEntity tip) {
        View tipView = LayoutInflater.from(getActivity())
                .inflate(R.layout.single_tip_simple, mTipsLinearLayout, false);

        TextView tipText = tipView.findViewById(R.id.tip_text);
        TextView authorName = tipView.findViewById(R.id.author_name);
        ImageView authorImg = tipView.findViewById(R.id.author_img);
        TextView likes = tipView.findViewById(R.id.likes);
        ImageView likes_img = tipView.findViewById(R.id.likes_img);
        tipText.setText(tip.getTipText());
        authorName.setText(tip.getAuthorName());
        if (tip.getLikes() != null && Integer.parseInt(tip.getLikes()) > 0) {
            likes.setText(tip.getLikes());
            likes_img.setVisibility(View.VISIBLE);
            likes.setVisibility(View.VISIBLE);
        }
        Glide.with(getActivity().getApplicationContext())
                .load(tip.getAuthorImage())
                .into(authorImg);
        mTipsLinearLayout.addView(tipView);
    }

    @Override
    public void toggleTipsLoading(boolean isLoading) {
        mTipsProgressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void toggleTipsLayout(boolean visible) {
        mNoTips.setVisibility(visible ? View.INVISIBLE : View.VISIBLE);
        mTipsLinearLayout.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }
}
