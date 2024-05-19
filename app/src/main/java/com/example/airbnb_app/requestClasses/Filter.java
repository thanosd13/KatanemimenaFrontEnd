package com.example.airbnb_app.requestClasses;

import java.io.Serializable;
import java.util.function.Predicate;

public class Filter implements Serializable {

    private static final long serialVersionUID = 1L;

    private String area;

    private Double stars;





    public Filter(String area) {
        this.area = area;
    }

    public Filter(String area, Integer noOfPersons) {
        this.area = area;
        this.noOfPersons = noOfPersons;
    }

    private DateRange range;



    public DateRange getRange() {
        return range;
    }

    public void setRange(DateRange range) {
        this.range = range;
    }

    private Integer price;

    private String roomName;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Filter(DateRange range, String roomName) {
        this.range = range;
        this.roomName = roomName;
    }

    public Filter() {

    }

    private Boolean booked;

    public Boolean isBooked() {
        return booked;
    }

    public void setBooked(Boolean booked) {
        this.booked = booked;
    }

    public Filter(Boolean booked) {
        this.booked = booked;
    }

    public Filter(DateRange range){
        this.range=range;
    }





    public Integer getPrice() {
        return price;
    }

    public Filter(String area, Integer price, Integer noOfPersons, Double stars) {
        this.area = area;
        this.price = price;
        this.noOfPersons = noOfPersons;
        this.stars = stars;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    private Integer noOfPersons;



    public void setNoOfPersons(Integer noOfPersons) {
        this.noOfPersons = noOfPersons;
    }

    public Double getStars() {
        return stars;
    }

    public void setStars(Double stars) {
        this.stars = stars;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getNoOfPersons() {
        return noOfPersons;
    }

    public void setNoOfPersons(int noOfPersons) {
        this.noOfPersons = noOfPersons;
    }

    public Predicate<Room> toPredicate() {
        return room -> {
            boolean matches = true;
            if (area != null) {
                matches &= room.getArea().equals(area);
            }
            if (noOfPersons != null){
                matches &= room.getNoOfPersons()>=noOfPersons;
            }
            if(stars!=null){
                matches &= room.getStars()>=stars;
            }
            if(price!=null){
                matches &= room.getPrice()<=price;
            }
            if (range != null) {
                matches &= room.availability.isFullyAvailable(range);
            }
            if(booked != null){
                if(booked){
                    matches &= room.availability.getReservations()!=0;
                }
            }
            // Add additional checks for future fields here
            return matches;
        };
    }
}
