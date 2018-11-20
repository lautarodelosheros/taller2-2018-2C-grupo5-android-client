package com.comprame.buy;

import com.comprame.sell.Geolocation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BuyItem implements Serializable {
    private String id;
    private String name;
    private String description;
    private double unitPrice;
    private List<String> imageUrls = new ArrayList<>();
    private String sellerId;
    private Geolocation geolocation;
    private List<String> paymentMethods;
    private List<String> categories;

    public BuyItem(String id
            , String name
            , String description
            , double unitPrice
            , String sellerId
            , Geolocation geolocation
            , ArrayList<String> paymentMethods
            , List<String> categories) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.sellerId = sellerId;
        this.geolocation = geolocation;
        this.paymentMethods = paymentMethods;
        this.categories = categories;
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

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String seller) {
        this.sellerId = sellerId;
    }

    public Geolocation getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(Geolocation geolocation) {
        this.geolocation = geolocation;
    }

    public void addImage(String url) {
        this.imageUrls.add(url);
    }

    public String getImage(int i) {
        if (i < this.imageUrls.size())
            return this.imageUrls.get(i);
        else
            return null;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public List<String> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<String> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getImages() {
        return imageUrls;
    }
}
