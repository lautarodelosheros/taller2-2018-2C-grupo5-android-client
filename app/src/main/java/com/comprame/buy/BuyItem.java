package com.comprame.buy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BuyItem implements Serializable {
    private String id;
    private String name;
    private String description;
    private double unitPrice;
    private List<String> imageUrls = new ArrayList<>();
    private String seller;
    private String location;
    private List<String> paymentMethods;
    private String categories;

    public BuyItem(String id
            , String name
            , String description
            , double unitPrice
            , String seller
            , String location
            , ArrayList<String> paymentMethods
            , String categories) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.seller = seller;
        this.location = location;
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

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public List<String> getImages() {
        return imageUrls;
    }
}
