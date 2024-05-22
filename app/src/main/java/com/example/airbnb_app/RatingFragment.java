package com.example.airbnb_app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.airbnb_app.requestClasses.DateRange;
import com.example.airbnb_app.requestClasses.Filter;
import com.example.airbnb_app.requestClasses.Message;
import com.example.airbnb_app.requestClasses.MyParcel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class RatingFragment extends Fragment {

    private RatingBar ratingBar;
    private float currentRating; // Variable to store the current rating

    private String roomName; // Variable to store the room name passed in as an argument

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rating, container, false);

        // Retrieve the room name from arguments
        if (getArguments() != null) {
            setRoomName(getArguments().getString("room_name"));
        }

        // Initialize the RatingBar
        ratingBar = view.findViewById(R.id.rating_stars);

        // Initialize the Button and set OnClickListener
        Button completeRatingButton = view.findViewById(R.id.complete_rating_button);
        completeRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRating = ratingBar.getRating();
                Thread networkThread = new Thread(() -> {
                    Message response = connect(9999);
                    if (response.getActionId() == 36) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Completed rate");
                                builder.setMessage("Thank you so much!");
                                builder.setPositiveButton("OK", (dialog, which) -> {
                                    dialog.dismiss();
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            });
                        }
                    }
                });
                networkThread.start();
            }
        });

        return view;
    }

    private Message connect(int port) {
        Filter filter = new Filter();
        double stars = currentRating;
        filter.setRoomName(roomName);
        filter.setStars(stars);
        Message request = new Message(5, filter);
        Message response = null;
        Socket requestSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {
            InetAddress serverAddr = InetAddress.getByName("10.0.2.2");
            requestSocket = new Socket(serverAddr, port);
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            out.writeObject(request);
            in = new ObjectInputStream(requestSocket.getInputStream());
            response = (Message) in.readObject();
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
        return response;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
