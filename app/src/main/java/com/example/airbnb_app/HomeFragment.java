package com.example.airbnb_app;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.airbnb_app.adapter.TopPlacesAdapter;
import com.example.airbnb_app.model.TopPlacesData;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements TopPlacesAdapter.OnPlaceClickListener {

    private RecyclerView topPlacesRecycler;
    private TopPlacesAdapter topPlacesAdapter;

    // Use simple string keys for arguments if necessary
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView(view);
    }

    private void initRecyclerView(View rootView) {
        topPlacesRecycler = rootView.findViewById(R.id.top_places_recycler);
        topPlacesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        List<TopPlacesData> topPlacesDataList = new ArrayList<>();
        topPlacesDataList.add(new TopPlacesData("room 1", "Athens, Greece", "$200 - $500", R.drawable.test2));
        topPlacesDataList.add(new TopPlacesData("room 2", "Athens, Glyfada", "$200 - $500", R.drawable.test2));
        topPlacesDataList.add(new TopPlacesData("room 1", "Athens, Greece", "$200 - $500", R.drawable.test2));
        topPlacesDataList.add(new TopPlacesData("room 1", "Athens, Greece", "$200 - $500", R.drawable.topplaces));

        topPlacesAdapter = new TopPlacesAdapter(getContext(), topPlacesDataList, this);
        topPlacesRecycler.setAdapter(topPlacesAdapter);
    }

    @Override
    public void onPlaceClick(TopPlacesData place) {
        // Ensure that the activity is correctly handling the fragment manager
        if(getActivity() != null) {
            RoomFragment roomFragment = RoomFragment.newInstance(
                    place.getPlaceName(), place.getCountryName(), place.getPrice(), place.getImageUrl());
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, roomFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
