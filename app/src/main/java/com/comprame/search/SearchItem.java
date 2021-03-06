package com.comprame.search;

import com.comprame.sell.Geolocation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchItem implements Serializable {
    public final String id;
    public final String name;
    public final String description;
    public final double unitPrice;
    public final List<String> categories;
    public final List<String> imageUrls = new ArrayList<>();
    public final String sellerId;
    public final Geolocation geolocation;
    public final List<String> paymentMethods;

    public SearchItem(String id
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


}
