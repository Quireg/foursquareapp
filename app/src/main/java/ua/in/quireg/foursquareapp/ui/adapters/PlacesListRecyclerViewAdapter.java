package ua.in.quireg.foursquareapp.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.LinkedList;
import java.util.List;

import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.mvp.models.presentation.PlaceEntity;

/**
 * Created by Arcturus Mengsk on 1/19/2018, 12:25 AM.
 * foursquareapp
 */

public class PlacesListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context mContext;
    private String mListTitle;
    private List<PlaceEntity> mItemsList = new LinkedList<>();

    private static final int HEADERS_COUNT = 1;

    public PlacesListRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case TYPE_HEADER: {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.places_list_header, parent, false);
                return new ListHeaderViewHolder(itemView);
            }
            case TYPE_ITEM: {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.places_list_item, parent, false);
                return new PlaceEntityViewHolder(itemView);
            }
            default: {
                throw new RuntimeException("Unknown view type");
            }
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        position = position - HEADERS_COUNT;

        if (holder instanceof ListHeaderViewHolder) {
            ((ListHeaderViewHolder) holder).title.setText(mListTitle);
        }

        if (holder instanceof PlaceEntityViewHolder) {

            PlaceEntity placeEntity = mItemsList.get(position);

            ((PlaceEntityViewHolder) holder).title.setText(
                    placeEntity.getName()
            );

            ((PlaceEntityViewHolder) holder).type_and_priceCategory.setText(
                    String.format("%s, %s", placeEntity.getType(), placeEntity.getPriceCategory())
            );

            if (placeEntity.getAddress().isEmpty()) {
                ((PlaceEntityViewHolder) holder).distance_and_address.setText(
                        String.format(
                                "%s steps away",
                                convertMetersIntoSteps(placeEntity.getDistanceTo()
                                )
                        )
                );
            } else {
                ((PlaceEntityViewHolder) holder).distance_and_address.setText(
                        String.format(
                                "%s steps away%s",
                                convertMetersIntoSteps(placeEntity.getDistanceTo()),
                                String.format(" at %s", placeEntity.getAddress())
                        )
                );
            }

            ((PlaceEntityViewHolder) holder).rating.setText(
                    placeEntity.getRating()
            );

            ((PlaceEntityViewHolder) holder).rating.setTextColor(
                    Color.parseColor(placeEntity.getRatingColor())
            );

            if(placeEntity.getImageUri() != null) {
                Glide.with(mContext)
                        .load(placeEntity.getImageUri())
                        .into(((PlaceEntityViewHolder) holder).image);
            }

        }

    }

    @Override
    public int getItemCount() {
        return mItemsList.size() + HEADERS_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_HEADER;

        return TYPE_ITEM;
    }

    public void updateList(List<PlaceEntity> list, String title) {
        int itemCount = getItemCount();
        if (itemCount > 0) {
            mItemsList.clear();
            notifyItemRangeRemoved(0, itemCount);
        }
        mListTitle = title;
        mItemsList.addAll(list);
        notifyItemRangeInserted(0, getItemCount());

    }

    class PlaceEntityViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, type_and_priceCategory, distance_and_address, rating;

        PlaceEntityViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.places_list_image);
            title = view.findViewById(R.id.places_list_title);
            type_and_priceCategory = view.findViewById(R.id.places_list_type_and_priceCategory);
            distance_and_address = view.findViewById(R.id.places_list_distance_and_address);
            rating = view.findViewById(R.id.places_list_rating);
        }
    }

    class ListHeaderViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        ListHeaderViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.list_header_textview);
        }
    }

    private String convertMetersIntoSteps(String meters) {
        return String.valueOf(Math.round(Integer.parseInt(meters) * 1.5));
    }

}
