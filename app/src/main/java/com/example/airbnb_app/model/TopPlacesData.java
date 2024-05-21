package com.example.airbnb_app.model;

import android.graphics.Bitmap;

public class TopPlacesData {

    String placeName;
    String countryName;
    String price;
    Bitmap image;

    Double avgStar;

    public TopPlacesData(String placeName, String countryName, String price, Bitmap image, Double avgStar) {
        this.placeName = placeName;
        this.countryName = countryName;
        this.price = price;
        this.image = image;
        this.avgStar = avgStar;
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
}
