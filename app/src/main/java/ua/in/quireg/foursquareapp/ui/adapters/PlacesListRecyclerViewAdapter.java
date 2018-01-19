package ua.in.quireg.foursquareapp.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.mvp.models.presentation.PlaceEntity;

/**
 * Created by Arcturus Mengsk on 1/19/2018, 12:25 AM.
 * foursquareapp
 */

public class PlacesListRecyclerViewAdapter extends RecyclerView.Adapter<PlacesListRecyclerViewAdapter.PlaceEntityViewHolder> {

    private List<PlaceEntity> mItemsList = new ArrayList<>();

    @Override
    public PlaceEntityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.places_list_item, parent, false);
        return new PlaceEntityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaceEntityViewHolder holder, int position) {
        PlaceEntity placeEntity = mItemsList.get(position);
        holder.title.setText(placeEntity.getName());
        holder.type_and_priceCategory.setText(String.format("%s, %s", placeEntity.getType(), placeEntity.getPriceCategory()));
        holder.distance_and_address.setText(String.format("%sm, %s", placeEntity.getDistanceTo(), placeEntity.getAddress()));
        holder.rating.setText(String.valueOf(placeEntity.getRating()));
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    public void updateList(List<PlaceEntity> newList) {
        mItemsList.clear();
        mItemsList.addAll(newList);
        this.notifyDataSetChanged();
    }

    public class PlaceEntityViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, type_and_priceCategory, distance_and_address, rating;

        public PlaceEntityViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.places_list_image);
            title = view.findViewById(R.id.places_list_title);
            type_and_priceCategory = view.findViewById(R.id.places_list_type_and_priceCategory);
            distance_and_address = view.findViewById(R.id.places_list_distance_and_address);
            rating = view.findViewById(R.id.places_list_rating);
        }
    }
}
