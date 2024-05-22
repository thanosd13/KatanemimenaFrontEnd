package com.example.airbnb_app.model;

import android.graphics.Bitmap;

import com.example.airbnb_app.requestClasses.DateRange;

public class TopPlacesData {

    String placeName;
    String countryName;
    String price;
    Bitmap image;

    Double avgStar;
    DateRange date;
    int noOfReviews;

    public TopPlacesData(String placeName, String countryName, String price, Bitmap image, Double avgStar, DateRange date, int noOfReviews) {
        this.placeName = placeName;
        this.countryName = countryName;
        this.price = price;
        this.image = image;
        this.avgStar = avgStar;
        this.date = date;
        this.noOfReviews = noOfReviews;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Double getAvgStar() {
        return avgStar;
    }

    public void setAvgStar(Double avgStar) {
        this.avgStar = avgStar;
    }

    public DateRange getDate() {
        return date;
    }

    public void setDate(DateRange date) {
        this.date = date;
    }

    public int getNoOfReviews() {
        return noOfReviews;
    }

    public void setNoOfReviews(int noOfReviews) {
        this.noOfReviews = noOfReviews;
    }
}
