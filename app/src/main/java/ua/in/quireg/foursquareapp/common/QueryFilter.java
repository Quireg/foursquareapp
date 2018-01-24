package ua.in.quireg.foursquareapp.common;

import android.content.SharedPreferences;

import ua.in.quireg.foursquareapp.models.LocationEntity;

/**
 * Created by Arcturus Mengsk on 1/23/2018, 7:00 PM.
 * foursquareapp
 */

@SuppressWarnings("WeakerAccess")
public class QueryFilter {

    public static final int SORT_TYPE_RELEVANCE = 0;
    public static final int SORT_TYPE_DISTANCE = 1;

    private static final String SORT_TYPE_PREFS = "sort_type";
    private static final String PRICE_TIER_PREFS = "price_tier";
    private static final String RADIUS_PREFS = "radius";

    private volatile int sortType;
    private volatile int priceTierFilter; //{1,2,4,8} bits
    private volatile String searchRadius;
    private volatile LocationEntity location;
    private SharedPreferences mSharedPreferences;

    private QueryFilter(QueryFilter.Builder builder) {
        sortType = builder.sortType;
        priceTierFilter = builder.priceTierFilter;
        searchRadius = builder.searchRadius;
        location = builder.location;
        mSharedPreferences = builder.sharedPreferences;

    }

    private void saveState() {
        if (mSharedPreferences == null) {
            throw new UnsupportedOperationException("No shared preferences provided");
        }

        mSharedPreferences.edit()
                .putInt(SORT_TYPE_PREFS, this.getSortType())
                .putInt(PRICE_TIER_PREFS, this.getPriceTierFilter())
                .putString(RADIUS_PREFS, this.getSearchRadius())
                .apply();
    }

    public synchronized void resetState() {
        if (mSharedPreferences == null) {
            throw new UnsupportedOperationException("No shared preferences provided");
        }

        QueryFilter defaultFilter = QueryFilter.buildNew().build();

        mSharedPreferences.edit()
                .putInt(SORT_TYPE_PREFS, defaultFilter.getSortType())
                .putInt(PRICE_TIER_PREFS, defaultFilter.getPriceTierFilter())
                .putString(RADIUS_PREFS, defaultFilter.getSearchRadius())
                .apply();
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
        this.priceTierFilter = priceTierFilter;
        saveState();
    }

    public String getSearchRadius() {
        return searchRadius;
    }

    public void setSearchRadius(String searchRadius) {
        this.searchRadius = searchRadius;
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
        private int priceTierFilter = 0;
        private String searchRadius = "10000";
        private LocationEntity location = null;
        private SharedPreferences sharedPreferences;

        private Builder() {
        }

        public QueryFilter.Builder setSortType(int sortType) {
            if (sortType == SORT_TYPE_RELEVANCE | sortType == SORT_TYPE_DISTANCE) {
                this.sortType = sortType;
            } else {
                throw new UnsupportedOperationException("0 or 1");
            }
            return this;
        }

        public QueryFilter.Builder setPriceTierFilter(int value) {
            if (value >= 0 && value <= 15) {
                this.priceTierFilter = value;
            } else {
                throw new UnsupportedOperationException("between 0 and 15 only");
            }
            return this;
        }

        public QueryFilter.Builder setSearchRadius(String radius) {

            if (Integer.valueOf(radius) > 500 && Integer.valueOf(radius) < 100000) {
                this.searchRadius = radius;
            } else {
                throw new UnsupportedOperationException("Between 500 and 100,000 only");
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

        return QueryFilter.buildNew()
                .setSortType(sharedPreferences.getInt(SORT_TYPE_PREFS, defaultQueryFilter.getSortType()))
                .setPriceTierFilter(sharedPreferences.getInt(PRICE_TIER_PREFS, defaultQueryFilter.getPriceTierFilter()))
                .setSearchRadius(sharedPreferences.getString(RADIUS_PREFS, defaultQueryFilter.getSearchRadius()))
                .setLocation(null)
                .setBackupStorage(sharedPreferences)
                .build();

//        Those who suggested to use putFloat and getFloat are unfortunately very wrong. Casting a double to a float can result in
//        Lost precision
//        Overflow
//        Underflow
//        Dead kittens
    }

}
