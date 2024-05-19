package com.example.airbnb_app.requestClasses;

import java.io.Serializable;

public class Room implements Serializable {

    private static final long serialVersionUID = 5L;

    private String roomName;
    private Integer noOfPersons;
    private String area;
    private double stars;
    private int noOfReviews;
    private String roomImage;

    private byte[] image;

    private Integer price;

    private Integer totalStars;

    public Integer getTotalStars() {
        return totalStars;
    }

    public void setTotalStars(Integer totalStars) {
        this.totalStars = totalStars;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public  Availability availability=new Availability();



    // Constructors, getters and setters

    public Room(String roomName, Integer noOfPersons, String area, double stars, int noOfReviews, String roomImage,Integer price) {
        this.roomName = roomName;
        this.noOfPersons = noOfPersons;
        this.area = area;
        this.stars = stars;
        this.noOfReviews = noOfReviews;
        this.roomImage = roomImage;
        this.price=price;
    }

    public void calculateStars(){

        this.stars = (double) totalStars/noOfReviews;


    }

    public Room() {
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getNoOfPersons() {
        return noOfPersons;
    }

    public void setNoOfPersons(Integer noOfPersons) {
        this.noOfPersons = noOfPersons;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public int getNoOfReviews() {
        return noOfReviews;
    }

    public void setNoOfReviews(int noOfReviews) {
        this.noOfReviews = noOfReviews;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }
}