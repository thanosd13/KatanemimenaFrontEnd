package com.example.airbnb_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.airbnb_app.adapter.TopPlacesAdapter;
import com.example.airbnb_app.model.TopPlacesData;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView topPlacesRecycler;
    private TopPlacesAdapter topPlacesAdapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        topPlacesRecycler.setLayoutManager(layoutManager);

        List<TopPlacesData> topPlacesDataList = new ArrayList<>();
        topPlacesDataList.add(new TopPlacesData("room 1", "Athens, Greece", "$200 - $500", R.drawable.test2));
        topPlacesDataList.add(new TopPlacesData("room 2", "Athens, Glyfada", "$200 - $500", R.drawable.test2));
        topPlacesDataList.add(new TopPlacesData("Kasimir Hill", "India", "$200 - $500", R.drawable.topplaces));
        topPlacesDataList.add(new TopPlacesData("Kasimir Hill", "India", "$200 - $500", R.drawable.topplaces));
        topPlacesDataList.add(new TopPlacesData("Kasimir Hill", "India", "$200 - $500", R.drawable.topplaces));

        topPlacesAdapter = new TopPlacesAdapter(getContext(), topPlacesDataList);
        topPlacesRecycler.setAdapter(topPlacesAdapter);
    }
}
