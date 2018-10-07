package com.comprame.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SearchItem implements Serializable {
    public final String id;
    public final String name;
    public final String description;
    public final double unitPrice;
    public final String categories;
    public final List<String> imageUrls = new ArrayList<>();
    public final String seller;
    public final String location;
    public final List<String> paymentMethods;

    public SearchItem(String id
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


}
