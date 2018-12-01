package com.comprame.mypublications;

import com.comprame.sell.Geolocation;

import java.io.Serializable;
import java.util.List;

public class Publication implements Serializable {
    private String id;
    private String name;
    private String description;
    private int units;
    private double unitPrice;
    private List<String> imageUrls;
    private String sellerId;
    private Geolocation geolocation;
    private List<String> paymentMethod;
    private List<String> categories;
    private String currency;

    public Publication(String id
            , String name
            , String description
            , int units
            , double unitPrice
            , List<String> imageUrls
            , String sellerId
            , Geolocation geolocation
            , List<String> paymentMethod
            , List<String> categories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.units = units;
        this.unitPrice = unitPrice;
        this.imageUrls = imageUrls;
        this.sellerId = sellerId;
        this.geolocation = geolocation;
        this.paymentMethod = paymentMethod;
        this.categories = categories;
        this.currency = "ARS";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Geolocation getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(Geolocation geolocation) {
        this.geolocation = geolocation;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<String> getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(List<String> paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
