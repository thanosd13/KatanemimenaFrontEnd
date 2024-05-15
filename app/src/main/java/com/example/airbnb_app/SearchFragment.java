package com.example.airbnb_app;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.airbnb_app.requestClasses.DateRange;
import com.example.airbnb_app.requestClasses.Filter;
import com.example.airbnb_app.requestClasses.Message;

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

    private Message request = new Message();

    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        editTextMaxPrice = view.findViewById(R.id.editTextMaxPrice);
        editTextLocation = view.findViewById(R.id.editTextLocation);
        spinnerPeople = view.findViewById(R.id.spinnerPeople);
        editTextArrivalDate = view.findViewById(R.id.editTextArrivalDate);
        editTextDepartureDate = view.findViewById(R.id.editTextDepartureDate);
        editTextArrivalDate.setOnClickListener(v -> showDatePickerDialog(true));
        editTextDepartureDate.setOnClickListener(v -> showDatePickerDialog(false));
//        request.getFilter()
        DateRange range = new DateRange(LocalDate.now(), LocalDate.now());
        Filter filter = new Filter();
        filter.setRange(range);
        request.setFilter(filter);
        initializePeopleSpinner();

        return view;
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



}