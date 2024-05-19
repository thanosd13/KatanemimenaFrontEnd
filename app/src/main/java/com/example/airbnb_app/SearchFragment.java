package com.example.airbnb_app;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.airbnb_app.requestClasses.DateRange;
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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SearchFragment extends Fragment {



    private EditText editTextArrivalDate, editTextDepartureDate, editTextMaxPrice, editTextLocation;
    private int year, month, day;
    private Spinner spinnerPeople;
    private Spinner spinnerStars;
    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        setupViews(view);
        initializePeopleSpinner();
        initializeStarsSpinner();
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
        spinnerStars = view.findViewById(R.id.spinnerStars);
        editTextArrivalDate = view.findViewById(R.id.editTextArrivalDate);
        editTextDepartureDate = view.findViewById(R.id.editTextDepartureDate);

        editTextArrivalDate.setOnClickListener(v -> showDatePickerDialog(true));
        editTextDepartureDate.setOnClickListener(v -> showDatePickerDialog(false));

        setupMaxPriceEditText();

        Button searchButton = view.findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(v -> gatherInputData());
    }

    // this method auto type the symbol of euro in maxPrice field
    private void setupMaxPriceEditText() {
        editTextMaxPrice.addTextChangedListener(new TextWatcher() {
            private boolean ignoreChange = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ignoreChange) return;
                ignoreChange = true;

                String str = s.toString().replaceAll("€", "").trim();
                if (!str.isEmpty()) {
                    editTextMaxPrice.setText(str + " €");
                    editTextMaxPrice.setSelection(str.length());
                } else {
                    editTextMaxPrice.setText("");
                }
                ignoreChange = false;
            }
        });
    }




    private void gatherInputData() {
        final Integer[] maxPrice = {null};  // Use an array to hold the value
        try {
            String priceText = editTextMaxPrice.getText().toString().replaceAll("[€\\s]", "");
            if (!priceText.isEmpty()) {
                maxPrice[0] = Integer.parseInt(priceText);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid format for price", Toast.LENGTH_SHORT).show();
        }

        final String location = editTextLocation.getText().toString().isEmpty() ? null : editTextLocation.getText().toString();
        final LocalDate arrivalDate = parseDate(editTextArrivalDate.getText().toString());
        final LocalDate departureDate = parseDate(editTextDepartureDate.getText().toString());
        final Integer[] numberOfPeople = {null};  // Use an array to hold the value
        final Integer[] numberOfStars = {null};
        final double[] finalStars = {0.0};

        try {
            String selectedItem = spinnerPeople.getSelectedItem().toString();
            if (!selectedItem.isEmpty() && selectedItem.matches("\\d+")) {
                numberOfPeople[0] = Integer.parseInt(selectedItem);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid number format for number of people", Toast.LENGTH_SHORT).show();
        }

        try {
            String selectedItemStars = spinnerStars.getSelectedItem().toString();
            if (!selectedItemStars.isEmpty() && selectedItemStars.matches("\\d+")) {
                numberOfStars[0] = Integer.parseInt(selectedItemStars);
                finalStars[0] = numberOfStars[0];
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid number format for number of stars", Toast.LENGTH_SHORT).show();
        }

        Thread networkThread = new Thread(() -> {
            String response = connect(9999, numberOfPeople[0], maxPrice[0], location, arrivalDate, departureDate, finalStars[0]);
            getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Response: " + response, Toast.LENGTH_LONG).show());
        });
        networkThread.start();
    }

    private LocalDate parseDate(String dateString) {
        if (!dateString.isEmpty()) {
            try {
                // Updated to match the format "dd/MM/yyyy"
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                return LocalDate.parse(dateString, formatter);
            } catch (DateTimeParseException e) {
                Toast.makeText(getContext(), "Invalid date format. Expected format: dd/MM/yyyy", Toast.LENGTH_SHORT).show();
                Log.e("SearchFragment", "Failed to parse date: " + dateString, e);
            }
        } else {
            Log.e("SearchFragment", "Empty date string cannot be parsed");
        }
        return null;
    }




    private void initializePeopleSpinner() {
        List<String> peopleNumbers = new ArrayList<>();
        peopleNumbers.add("Adults"); // This will act as a hint
        for (int i = 1; i <= 10; i++) {
            peopleNumbers.add(Integer.toString(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, peopleNumbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeople.setAdapter(adapter);
        spinnerPeople.setSelection(0, false); // Display hint by default
    }

    private void initializeStarsSpinner() {
        List<String> starsArray = new ArrayList<>();
        starsArray.add("Stars"); // This will act as a hint
        for (int i = 1; i <= 5; i++) {
            starsArray.add(Integer.toString(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, starsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStars.setAdapter(adapter);
        spinnerStars.setSelection(0, false); // Display hint by default
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



    private String connect(int port, Integer numberOfPeople, Integer maxPrice, String location, LocalDate startDate, LocalDate endDate, Double stars) {
        Filter filter = new Filter();
        DateRange date = null;
        if (maxPrice != null) filter.setPrice(maxPrice);
        if (location != null) filter.setArea(location);
        if (numberOfPeople != null) filter.setNoOfPersons(numberOfPeople);
        if(stars != null) filter.setStars(stars);
        if(startDate != null && endDate != null) {
            date = new DateRange(startDate, endDate);
            filter.setRange(date);
        }
        Message request = new Message(4, filter);
        Message response = null;
        Socket requestSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        String responseDetails = "";

        try {
            InetAddress serverAddr = InetAddress.getByName("10.0.2.2");
            requestSocket = new Socket(serverAddr, port);
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            out.writeObject(request);
            in = new ObjectInputStream(requestSocket.getInputStream());
            response = (Message) in.readObject();
            responseDetails = "Received: " + response.getMessage() + ". Status code: " + response.getActionId();
        } catch (Exception e) {
            Log.e("ConnectionError", "Error during network communication", e);
            responseDetails = "Error: " + e.getMessage();
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (requestSocket != null) requestSocket.close();
            } catch (IOException ioException) {
                Log.e("ConnectionError", "IOException occurred while closing the connections.", ioException);
            }
        }
        return responseDetails;
    }






}