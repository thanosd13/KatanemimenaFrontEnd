package com.example.airbnb_app;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airbnb_app.model.TopPlacesData;
import com.example.airbnb_app.requestClasses.DateRange;
import com.example.airbnb_app.requestClasses.Filter;
import com.example.airbnb_app.requestClasses.Message;
import com.example.airbnb_app.requestClasses.Room;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoomFragment extends Fragment {

    private static final String ARG_PLACE_NAME = "place_name";
    private static final String ARG_COUNTRY_NAME = "country_name";
    private static final String ARG_PRICE = "price";
    private static final String ARG_IMAGE_BITMAP = "image_bitmap";

    private static final String AVG_STARS = "avg_stars";
    private static final String DATE_RANGE = "date_range";

    private static final String MASTER_IP="192.168.1.9";

    private String placeName;
    private String countryName;
    private String price;
    private Bitmap imageBitmap;
    private String avgStars;
    private DateRange dateRange;

    public RoomFragment() {
        // Required empty public constructor
    }

    public static RoomFragment newInstance(String placeName, String countryName, String price, Bitmap imageBitmap, Double stars, DateRange date) {
        RoomFragment fragment = new RoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLACE_NAME, placeName);
        args.putString(ARG_COUNTRY_NAME, countryName);
        args.putString(ARG_PRICE, price);
        args.putParcelable(ARG_IMAGE_BITMAP, imageBitmap);
        args.putString(AVG_STARS, String.format("%.1f", stars));
        args.putSerializable(DATE_RANGE, date);
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
            dateRange = (DateRange) getArguments().getSerializable(DATE_RANGE);
            System.out.println("paff!!!");
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

        Button bookNowButton = view.findViewById(R.id.button);
        bookNowButton.setOnClickListener(v -> bookingNow());
    }

    private void bookingNow() {

        if(dateRange.getEndDate() == null || dateRange.getStartDate() == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Error");
            builder.setMessage("You need to fill in the dates you wish to make the reservation for.");

            builder.setNegativeButton("Ok", (dialog, which) -> {
                // Dismiss dialog and cancel booking
                dialog.dismiss();
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        Thread networkThread = new Thread(() -> {
            try {
                Message response= connect(9999);
                if (response.getActionId() == 37) {
                    if (getActivity() != null) {

                        getActivity().runOnUiThread(() -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Booking Successful");
                            builder.setMessage("Congratulations. " + response.getMessage() +"!" + " Please rate your experience!");
                            builder.setPositiveButton("OK", (dialog, which) -> {
                                RatingFragment ratingFragment = new RatingFragment();

                                Bundle args = new Bundle();
                                args.putString("room_name", placeName);
                                ratingFragment.setArguments(args);
                                FragmentManager fragmentManager = getParentFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, ratingFragment);
                                fragmentTransaction.commit();  // Commit the transaction
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        });
                    }
                } else if (response.getActionId() == 38) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Error");
                            builder.setMessage(response.getMessage() +"!");
                            builder.setNegativeButton("Ok", (dialog, which) -> {
                                dialog.dismiss();
                            });

                            AlertDialog dialogNew = builder.create();
                            dialogNew.show();
                        });
                    }
                }
            } catch (Exception e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Error");
                builder.setMessage("Something went wrong!");

                builder.setNegativeButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        networkThread.start();

    }

    public void successMessage () {

    }

    private Message connect(int port) {
        Filter filter = new Filter();
        filter.setRoomName(placeName);
        filter.setRange(dateRange);
        Message request = new Message(6, filter);
        Message response = null;
        Socket requestSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {
            requestSocket = new Socket(MASTER_IP, port);
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

}
