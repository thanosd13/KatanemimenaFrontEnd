package com.example.airbnb_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class RatingFragment extends Fragment {

    private RatingBar ratingBar;
    private TextView ratingText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rating, container, false);

        // Initialize the RatingBar
        ratingBar = view.findViewById(R.id.rating_stars);
        // Initialize the TextView
        ratingText = view.findViewById(R.id.rating_text);

        // Set an OnRatingBarChangeListener to get the rating value
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // Update the TextView with the number of stars
                ratingText.setText("Rating: " + rating + " Stars");
            }
        });

        return view;
    }
}
