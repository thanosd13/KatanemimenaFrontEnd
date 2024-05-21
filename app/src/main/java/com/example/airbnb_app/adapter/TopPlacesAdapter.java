package com.example.airbnb_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airbnb_app.R;
import com.example.airbnb_app.model.TopPlacesData;

import java.util.List;

public class TopPlacesAdapter extends RecyclerView.Adapter<TopPlacesAdapter.TopPlacesViewHolder> {

    private Context context;
    private List<TopPlacesData> topPlacesDataList;
    private OnPlaceClickListener listener;

    public interface OnPlaceClickListener {
        void onPlaceClick(TopPlacesData place);
    }

    public TopPlacesAdapter(Context context, List<TopPlacesData> topPlacesDataList, OnPlaceClickListener listener) {
        this.context = context;
        this.topPlacesDataList = topPlacesDataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TopPlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.top_places_row_item, parent, false);
        return new TopPlacesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopPlacesViewHolder holder, int position) {
        TopPlacesData data = topPlacesDataList.get(position);

        holder.countryName.setText(data.getCountryName());
        holder.placeName.setText(data.getPlaceName());
        holder.price.setText(data.getPrice());
        holder.placeImage.setImageBitmap(data.getImage());

        // Format the star rating to one decimal place
        double stars = data.getAvgStar();
        String formattedStars = String.format("%.1f", stars);
        holder.stars.setText(formattedStars);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlaceClick(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topPlacesDataList.size();
    }

    public static final class TopPlacesViewHolder extends RecyclerView.ViewHolder {
        ImageView placeImage;
        TextView placeName, countryName, price, stars;

        public TopPlacesViewHolder(@NonNull View itemView) {
            super(itemView);

            placeImage = itemView.findViewById(R.id.place_image);
            placeName = itemView.findViewById(R.id.place_name);
            countryName = itemView.findViewById(R.id.country_name);
            price = itemView.findViewById(R.id.price);
            stars = itemView.findViewById(R.id.textView7);
        }
    }
}
