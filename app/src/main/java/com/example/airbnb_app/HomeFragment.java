package com.example.airbnb_app;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.airbnb_app.adapter.TopPlacesAdapter;
import com.example.airbnb_app.model.TopPlacesData;
import com.example.airbnb_app.requestClasses.Filter;
import com.example.airbnb_app.requestClasses.Message;
import com.example.airbnb_app.requestClasses.Room;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
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

        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);  // Show the ProgressBar while loading

        Thread networkThread = new Thread(() -> {
            try {
                List<Room> rooms = connect(9999);
                List<TopPlacesData> topPlacesDataList = new ArrayList<>();
                for (Room room : rooms) {
                    topPlacesDataList.add(new TopPlacesData(room.getRoomName(), room.getArea(), room.getPrice().toString(), R.drawable.topplaces));
                }
                getActivity().runOnUiThread(() -> {
                    initRecyclerView(view, topPlacesDataList);
                    progressBar.setVisibility(View.GONE);  // Hide the ProgressBar on successful load
                });
            } catch (Exception e) {
                Log.e("HomeFragment", "Error loading data", e);
                getActivity().runOnUiThread(() -> {
                    // Update UI to show error state or hide progress bar
                    progressBar.setVisibility(View.GONE);  // Hide the ProgressBar on error
                    Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                });
            }
        });
        networkThread.start();
    }




    private void initRecyclerView(View rootView, List<TopPlacesData> topPlacesDataList) {
        topPlacesRecycler = rootView.findViewById(R.id.top_places_recycler);
        topPlacesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
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

    private List<Room> connect(int port) {
        Filter filter = new Filter();
        Message request = new Message(4, filter);
        Message response;
        Socket requestSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        List<Room> rooms = new ArrayList<>();

        try {
            InetAddress serverAddr = InetAddress.getByName("10.0.2.2");
            requestSocket = new Socket(serverAddr, port);
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            out.writeObject(request);
            in = new ObjectInputStream(requestSocket.getInputStream());
            response = (Message) in.readObject();
            rooms = response.getRooms();
        } catch (Exception e) {
            Log.e("ConnectionError", "Error during network communication", e);
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (requestSocket != null) requestSocket.close();
            } catch (IOException ioException) {
                Log.e("ConnectionError", "IOException occurred while closing the connections.", ioException);
            }
        }
        return rooms;
    }

}