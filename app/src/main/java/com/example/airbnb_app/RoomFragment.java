package com.example.airbnb_app;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RoomFragment extends Fragment {

    private static final String ARG_PLACE_NAME = "place_name";
    private static final String ARG_COUNTRY_NAME = "country_name";
    private static final String ARG_PRICE = "price";
    private static final String ARG_IMAGE_BITMAP = "image_bitmap";

    private static final String AVG_STARS = "avg_stars";

    private String placeName;
    private String countryName;
    private String price;
    private Bitmap imageBitmap;
    private String avgStars;

    public RoomFragment() {
        // Required empty public constructor
    }

    public static RoomFragment newInstance(String placeName, String countryName, String price, Bitmap imageBitmap, Double stars) {
        RoomFragment fragment = new RoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLACE_NAME, placeName);
        args.putString(ARG_COUNTRY_NAME, countryName);
        args.putString(ARG_PRICE, price);
        args.putParcelable(ARG_IMAGE_BITMAP, imageBitmap);
        args.putString(AVG_STARS, String.format("%.1f", stars));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            placeName = getArguments().getString(ARG_PLACE_NAME);
            countryName = getArguments().getString(ARG_COUNTRY_NAME);
            price = getArguments().getString(ARG_PRICE);
            imageBitmap = getArguments().getParcelable(ARG_IMAGE_BITMAP);
            avgStars = getArguments().getString(AVG_STARS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_room, container, false);

        // Update UI components with the received data
        TextView textViewPlace = view.findViewById(R.id.place_name);
        TextView textViewPrice = view.findViewById(R.id.price);
        ImageView imageView = view.findViewById(R.id.place_image);
        TextView textViewStars = view.findViewById(R.id.textView7);

        textViewPlace.setText(placeName);
        textViewPrice.setText(price);
        textViewStars.setText(avgStars);

        if (imageBitmap != null) {
            imageView.setImageBitmap(imageBitmap);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView backButton = view.findViewById(R.id.imageView4);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the fragment is attached to an activity
                if (getActivity() != null) {
                    getActivity().onBackPressed();  // This method handles the back stack as expected
                }
            }
        });
    }
}
