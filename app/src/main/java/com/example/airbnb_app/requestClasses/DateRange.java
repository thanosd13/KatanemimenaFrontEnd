package com.example.airbnb_app.requestClasses;

import java.io.Serializable;
import java.time.LocalDate;

public class DateRange implements Serializable {

    private static final long serialVersionUID = 3L;
    private LocalDate startDate;
    private LocalDate endDate;

    public DateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean overlaps(DateRange other) {
        return !this.endDate.isBefore(other.startDate) && !this.startDate.isAfter(other.endDate);
    }

    public boolean adjacent(DateRange other) {
        boolean endsDayBefore = this.endDate.plusDays(1).equals(other.startDate);
        boolean startsDayAfter = this.startDate.minusDays(1).equals(other.endDate);
        return endsDayBefore || startsDayAfter;
    }

    public boolean contains(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}