package com.example.airbnb_app;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.airbnb_app.requestClasses.Filter;
import com.example.airbnb_app.requestClasses.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class SearchFragment extends Fragment {



    private EditText editTextArrivalDate, editTextDepartureDate, editTextMaxPrice, editTextLocation;
    private int year, month, day;
    private Spinner spinnerPeople;
    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        setupViews(view);
//        request.getFilter()
//        DateRange range = new DateRange(LocalDate.now(), LocalDate.now());
//        Filter filter = new Filter();
//        filter.setRange(range);
//        request.setFilter(filter);
        initializePeopleSpinner();
        // Set up the button click listener
        Button searchButton = view.findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gatherInputData();
            }
        });

        return view;
    }

    private void setupViews(View view) {
        editTextMaxPrice = view.findViewById(R.id.editTextMaxPrice);
        editTextLocation = view.findViewById(R.id.editTextLocation);
        spinnerPeople = view.findViewById(R.id.spinnerPeople);
        editTextArrivalDate = view.findViewById(R.id.editTextArrivalDate);
        editTextDepartureDate = view.findViewById(R.id.editTextDepartureDate);

        editTextArrivalDate.setOnClickListener(v -> showDatePickerDialog(true));
        editTextDepartureDate.setOnClickListener(v -> showDatePickerDialog(false));

        initializePeopleSpinner();
    }

    private void gatherInputData() {
        String maxPrice = editTextMaxPrice.getText().toString();
        String location = editTextLocation.getText().toString();
        String arrivalDate = editTextArrivalDate.getText().toString();
        String departureDate = editTextDepartureDate.getText().toString();
        int numberOfPeople = (Integer) spinnerPeople.getSelectedItem();

        Thread networkThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String response = connect(9999);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Response: " + response, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        networkThread.start();
    }

    private void initializePeopleSpinner() {
        Integer[] peopleNumbers = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, peopleNumbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeople.setAdapter(adapter);
    }

    private void showDatePickerDialog(boolean isArrival) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String date = String.format(Locale.getDefault(), "%d/%d/%d", dayOfMonth, monthOfYear + 1, year1);
                    if (isArrival) {
                        editTextArrivalDate.setText(date);
                    } else {
                        editTextDepartureDate.setText(date);
                    }
                }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

        if (!isArrival && editTextArrivalDate.getText().length() > 0) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date arrivalDate = sdf.parse(editTextArrivalDate.getText().toString());
                Calendar cal = Calendar.getInstance();
                if (arrivalDate != null) {
                    cal.setTime(arrivalDate);
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                    datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        datePickerDialog.show();
    }

    String connect(int port) {
        Filter filter = new Filter();
        Message request = new Message(4, filter); // Assuming actionId 4 is correct for your use case
        Socket requestSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        String responseDetails = "";

        try {
            // Step 1: Establish a connection to the server
            InetAddress serverAddr = InetAddress.getByName("10.0.2.2"); // Use "10.0.2.2" to connect to localhost of the host machine
            requestSocket = new Socket(serverAddr, port);

            // Step 2: Create ObjectOutputStream first
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();  // Make sure to flush the ObjectOutputStream immediately after creation

            // Step 3: Send the request object
            out.writeObject(request);

            // Step 4: Create ObjectInputStream
            in = new ObjectInputStream(requestSocket.getInputStream());

            // Step 5: Read the response from the server
            Message response = (Message) in.readObject();
            responseDetails = "Received: " + response.getMessage() + ". Status code: " + response.getActionId();
        } catch (UnknownHostException unknownHost) {
            Log.e("ConnectionError", "You are trying to connect to an unknown host!", unknownHost);
            responseDetails = "Error: Unknown host!";
        } catch (IOException ioException) {
            Log.e("ConnectionError", "IOException occurred while connecting or communicating.", ioException);
            responseDetails = "Error: IOException!";
        } catch (ClassNotFoundException e) {
            Log.e("ConnectionError", "Class not found when reading object.", e);
            responseDetails = "Error: Class not found!";
        } finally {
            // Step 6: Close all streams and the socket
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (requestSocket != null) {
                    requestSocket.close();
                }
            } catch (IOException ioException) {
                Log.e("ConnectionError", "IOException occurred while closing the connections.", ioException);
            }
        }
        return responseDetails;
    }


}