package com.example.airbnb_app;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RoomFragment extends Fragment {

    private static final String ARG_PLACE_NAME = "place_name";
    private static final String ARG_COUNTRY_NAME = "country_name";
    private static final String ARG_PRICE = "price";
    private static final String ARG_IMAGE_URL = "image_url";

    private String placeName;
    private String countryName;
    private String price;
    private int imageUrl;

    public RoomFragment() {
        // Required empty public constructor
    }

    public static RoomFragment newInstance(String placeName, String countryName, String price, int imageUrl) {
        RoomFragment fragment = new RoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLACE_NAME, placeName);
        args.putString(ARG_COUNTRY_NAME, countryName);
        args.putString(ARG_PRICE, price);
        args.putInt(ARG_IMAGE_URL, imageUrl);
        System.out.print("args:");
        System.out.print(args);
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
            imageUrl = getArguments().getInt(ARG_IMAGE_URL);
            System.out.print("test!!!!!!!!");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_room, container, false);

        // Update UI components with the received data
        TextView textViewPlace = view.findViewById(R.id.place_name);
//        TextView textViewCountry = view.findViewById(R.id.country_name);
        TextView textViewPrice = view.findViewById(R.id.price);
        ImageView imageView = view.findViewById(R.id.place_image);

        textViewPlace.setText(placeName);
//        textViewCountry.setText(countryName);
        textViewPrice.setText(price);
        imageView.setImageResource(imageUrl);

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
