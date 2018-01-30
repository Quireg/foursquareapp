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

    private static final String PREFS_SORT_TYPE = "sort_type";
    private static final String PREFS_PRICE_TIER = "price_tier";
    private static final String PREFS_RADIUS = "radius";
    private static final String PREFS_LOC_LAT = "loc_lat";
    private static final String PREFS_LOC_LON = "loc_lon";
    private static final String PREFS_LOC_ADDRESS = "loc_address";

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

        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor
                .putInt(PREFS_SORT_TYPE, this.getSortType())
                .putInt(PREFS_PRICE_TIER, this.getPriceTierFilter())
                .putString(PREFS_RADIUS, this.getSearchRadius());


        if (location != null) {

            editor.putLong(PREFS_LOC_LAT,  Double.doubleToLongBits(location.getLat()))
                    .putLong(PREFS_LOC_LON, Double.doubleToLongBits(location.getLon()));

            if(location.getAddress() != null) {
                editor.putString(PREFS_LOC_ADDRESS, location.getAddress());
            }

        }

        if (!editor.commit()) {
            throw new RuntimeException("Failed to save filter state");
        }
    }

    public synchronized void resetState() {
        if (mSharedPreferences == null) {
            throw new UnsupportedOperationException("No shared preferences provided");
        }

        QueryFilter defaultFilter = QueryFilter.buildNew().build();

        sortType = defaultFilter.sortType;
        priceTierFilter = defaultFilter.priceTierFilter;
        searchRadius = defaultFilter.searchRadius;
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

            if (radius == null) {
                return this;
            }

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

        LocationEntity locationEntity = null;

        long latitudeLongBits = sharedPreferences.getLong(PREFS_LOC_LAT, 0);
        long longitudeLongBits = sharedPreferences.getLong(PREFS_LOC_LON, 0);
        String address = sharedPreferences.getString(PREFS_LOC_ADDRESS, null);

        if (latitudeLongBits != 0 && longitudeLongBits != 0) {

//        Those who suggested to use putFloat and getFloat are unfortunately very wrong. Casting a double to a float can result in
//        Lost precision
//        Overflow
//        Underflow
//        Dead kittens

            locationEntity = new LocationEntity(
                    Double.longBitsToDouble(latitudeLongBits),
                    Double.longBitsToDouble(longitudeLongBits)
            );


            locationEntity.setAddress(address);
        }

        return QueryFilter.buildNew()
                .setSortType(sharedPreferences.getInt(PREFS_SORT_TYPE, defaultQueryFilter.getSortType()))
                .setPriceTierFilter(sharedPreferences.getInt(PREFS_PRICE_TIER, defaultQueryFilter.getPriceTierFilter()))
                .setSearchRadius(sharedPreferences.getString(PREFS_RADIUS, null))
                .setLocation(locationEntity)
                .setBackupStorage(sharedPreferences)
                .build();


    }

}
