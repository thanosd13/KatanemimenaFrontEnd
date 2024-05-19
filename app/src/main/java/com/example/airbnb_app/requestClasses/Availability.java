package com.example.airbnb_app.requestClasses;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Availability implements Serializable {

    private static final long serialVersionUID = 2L;

    private List<DateRange> bookedDates=new ArrayList<>();
    private List<DateRange> availableDates=new ArrayList<>();

    public int getReservations(){
        return this.bookedDates.size();
    }



    public boolean isFullyAvailable(DateRange queryRange) {
        return availableDates.stream()
                .anyMatch(available -> available.contains(queryRange.getStartDate()) && available.contains(queryRange.getEndDate()));
    }

    public boolean addAvailableDates(DateRange newRange) {


        if (overlapsWithBookedDates(newRange)) {
            System.out.println("Cannot add available dates as it overlaps with booked periods.");
            return false;
        }




        this.availableDates = addDateRange(this.availableDates, newRange);
        return true;
    }





    private boolean overlapsWithBookedDates(DateRange newRange) {
        for (DateRange booked : bookedDates) {
            if (booked.overlaps(newRange)) {
                return true;
            }
        }
        return false;
    }

    public boolean addReservation(DateRange newReservation) {

        if (!isFullyAvailable(newReservation)) {
            return false;  // The new reservation is not fully available, can't be added.
        }
        List<DateRange> newAvailableDates = new ArrayList<>();
        boolean reservationAdded = false;

        for (DateRange available : new ArrayList<>(availableDates)) {
            if (available.overlaps(newReservation)) {
                reservationAdded = true;
                // Before the new reservation starts
                if (available.getStartDate().isBefore(newReservation.getStartDate())) {
                    newAvailableDates.add(new DateRange(available.getStartDate(), newReservation.getStartDate()));
                }
                // After the new reservation ends (making the same end date available since the room is free post-9 AM)
                if (available.getEndDate().isAfter(newReservation.getEndDate())) {
                    newAvailableDates.add(new DateRange(newReservation.getEndDate(), available.getEndDate()));
                }
            } else {
                newAvailableDates.add(new DateRange(available.getStartDate(), available.getEndDate()));
            }
        }

        if (reservationAdded) {
            availableDates.clear();
            availableDates.addAll(newAvailableDates);
            bookedDates.add(newReservation);
        }

        return reservationAdded;
    }

    private List<DateRange> addDateRange(List<DateRange> dateRanges, DateRange newRange) {
        List<DateRange> updatedRanges = new ArrayList<>();
        boolean merged = false;
        for (DateRange range : dateRanges) {
            if (range.overlaps(newRange) || range.adjacent(newRange)) {
                LocalDate minStartDate = range.getStartDate().isBefore(newRange.getStartDate()) ? range.getStartDate() : newRange.getStartDate();
                LocalDate maxEndDate = range.getEndDate().isAfter(newRange.getEndDate()) ? range.getEndDate() : newRange.getEndDate();
                newRange = new DateRange(minStartDate, maxEndDate);
                merged = true;
            } else {
                updatedRanges.add(range);
            }
        }
        if (!merged) {
            updatedRanges.add(newRange);
        } else {
            // Merging overlapping ranges
            updatedRanges = addDateRange(updatedRanges, newRange);
        }
        return updatedRanges.stream()
                .sorted((r1, r2) -> r1.getStartDate().compareTo(r2.getStartDate()))
                .collect(Collectors.toList());
    }

    public boolean isBookedInGivenRange(DateRange givenRange) {
        for(DateRange range:bookedDates){
            if(range.overlaps(givenRange)){
                return true;
            }

        }
        return false;
    }

    public int getNumberOfReservationsInGivenRange(DateRange range){
        return (int) bookedDates.stream()
                .filter(bookedDate -> bookedDate.overlaps(range))
                .count();


    }
}
