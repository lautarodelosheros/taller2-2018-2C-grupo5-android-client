package com.comprame.sell;

import java.io.Serializable;
import java.util.List;

public class SellItem implements Serializable {
    public final String name;
    public final String description;
    public final int units;
    public final double unitPrice;
    public final List<String> imageUrls;
    public final String sellerId;
    public final String location;
    public final String paymentMethod;
    public final List<String> categories;

    public SellItem(String name
            , String description
            , int units
            , double unitPrice
            , List<String> imageUrls
            , String sellerId
            , String location
            , String paymentMethod
            , List<String> categories) {
        this.name = name;
        this.description = description;
        this.units = units;
        this.unitPrice = unitPrice;
        this.imageUrls = imageUrls;
        this.sellerId = sellerId;
        this.location = location;
        this.paymentMethod = paymentMethod;
        this.categories = categories;
    }
}
