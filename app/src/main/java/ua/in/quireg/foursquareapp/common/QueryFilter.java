package ua.in.quireg.foursquareapp.common;

import android.content.SharedPreferences;

import timber.log.Timber;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.models.LocationEntity;

/**
 * Created by Arcturus Mengsk on 1/23/2018, 7:00 PM.
 * foursquareapp
 */

@SuppressWarnings("WeakerAccess")
public class QueryFilter {

    public static final int SORT_TYPE_RELEVANCE = 0;
    public static final int SORT_TYPE_DISTANCE = 1;
    public static final int SEARCH_AREA_DEFAULT = -1;

    private static final String PREFS_SORT_TYPE = "sort_type";
    private static final String PREFS_PRICE_TIER = "price_tier";
    private static final String PREFS_AREA = "area";
    private static final String PREFS_LOC_LAT = "loc_lat";
    private static final String PREFS_LOC_LON = "loc_lon";
    private static final String PREFS_LOC_ADDRESS = "loc_address";

    private volatile int sortType;
    private volatile int priceTierFilter; //{1,2,4,8} bits
    private volatile int mSearchArea;
    private volatile LocationEntity location;
    private SharedPreferences mSharedPreferences;

    private QueryFilter(QueryFilter.Builder builder) {
        sortType = builder.sortType;
        priceTierFilter = builder.priceTierFilter;
        mSearchArea = builder.mSearchAreaRadius;
        location = builder.location;
        mSharedPreferences = builder.sharedPreferences;
    }

    private void saveState() {
        if (mSharedPreferences == null) {
            throw new UnsupportedOperationException("No shared preferences provided");
        }

        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putInt(PREFS_SORT_TYPE, this.getSortType())
                .putInt(PREFS_PRICE_TIER, this.getPriceTierFilter())
                .putInt(PREFS_AREA, this.getSearchArea());

        if (location != null) {

            editor.putLong(PREFS_LOC_LAT, Double.doubleToLongBits(location.getLat()))
                    .putLong(PREFS_LOC_LON, Double.doubleToLongBits(location.getLon()));

            if (location.getAddress() != null) {
                editor.putString(PREFS_LOC_ADDRESS, location.getAddress());
            }
        }
        editor.apply();
    }

    public synchronized void resetState() {
        if (mSharedPreferences == null) {
            Timber.e(new IllegalStateException("No shared preferences provided"));
            return;
        }

        QueryFilter defaultFilter = QueryFilter.buildNew().build();

        sortType = defaultFilter.sortType;
        priceTierFilter = defaultFilter.priceTierFilter;
        mSearchArea = defaultFilter.mSearchArea;
        location = defaultFilter.location;

        saveState();
    }

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
        saveState();
    }

    public int getPriceTierFilter() {
        return priceTierFilter;
    }

    public void setPriceTierFilter(int priceTierFilter) {
        if (priceTierFilter == 0) priceTierFilter = 0xf; //set all prices active
        this.priceTierFilter = priceTierFilter;
        saveState();
    }

    public int getSearchArea() {
        return mSearchArea;
    }

    public void setSearchArea(int searchArea) {
        if (searchArea == QueryFilter.SEARCH_AREA_DEFAULT)
            searchArea = buildNew().mSearchAreaRadius;
        this.mSearchArea = searchArea;
        saveState();
    }

    public LocationEntity getLocation() {
        return location;
    }

    public void setLocation(LocationEntity location) {
        this.location = location;
        saveState();
    }

    public static final class Builder {

        private int sortType = SORT_TYPE_RELEVANCE;
        private int priceTierFilter = 0xf;
        private int mSearchAreaRadius = FoursquareApplication.DEFAULT_SEARCH_RADIUS;
        private LocationEntity location = null;
        private SharedPreferences sharedPreferences;

        private Builder() {
        }

        public QueryFilter.Builder setSortType(int sortType) {
            if (sortType == SORT_TYPE_RELEVANCE | sortType == SORT_TYPE_DISTANCE) {
                this.sortType = sortType;
            }
            return this;
        }

        public QueryFilter.Builder setPriceTierFilter(int value) {
            if (value >= 0 && value <= priceTierFilter) {
                this.priceTierFilter = value;
            }
            return this;
        }

        public QueryFilter.Builder setSearchArea(int area) {
            if (area > FoursquareApplication.RADIUS_LOW
                    && area < FoursquareApplication.RADIUS_HIGH) {
                this.mSearchAreaRadius = area;
            }
            return this;
        }

        public QueryFilter.Builder setLocation(LocationEntity l) {
            this.location = l;
            return this;
        }

        public QueryFilter.Builder setBackupStorage(SharedPreferences sharedPreferences) {
            this.sharedPreferences = sharedPreferences;
            return this;
        }

        public QueryFilter build() {
            return new QueryFilter(this);
        }
    }

    public static QueryFilter.Builder buildNew() {
        return new QueryFilter.Builder();
    }

    public static QueryFilter buildFromPreferences(SharedPreferences sharedPreferences) {

        QueryFilter defaultQueryFilter = QueryFilter.buildNew().build();

        LocationEntity locationEntity = null;

        long latitudeLongBits = sharedPreferences.getLong(PREFS_LOC_LAT, 0);
        long longitudeLongBits = sharedPreferences.getLong(PREFS_LOC_LON, 0);
        String address = sharedPreferences.getString(PREFS_LOC_ADDRESS, null);

        if (latitudeLongBits != 0 && longitudeLongBits != 0) {

            /*Those who suggested to use putFloat and getFloat are unfortunately very wrong.
            Casting a double to a float can result in
            Lost precision
            Overflow
            Underflow
            Dead kittens*/

            locationEntity = new LocationEntity(
                    Double.longBitsToDouble(latitudeLongBits),
                    Double.longBitsToDouble(longitudeLongBits)
            );
            locationEntity.setAddress(address);
        }

        int area = sharedPreferences.getInt(PREFS_AREA, defaultQueryFilter.getSearchArea());
        int sortType = sharedPreferences.getInt(PREFS_SORT_TYPE, defaultQueryFilter.getSortType());
        int priceTier = sharedPreferences.getInt(PREFS_PRICE_TIER, defaultQueryFilter.getPriceTierFilter());

        return QueryFilter.buildNew()
                .setSortType(sortType)
                .setPriceTierFilter(priceTier)
                .setSearchArea(area)
                .setLocation(locationEntity)
                .setBackupStorage(sharedPreferences)
                .build();
    }
}
