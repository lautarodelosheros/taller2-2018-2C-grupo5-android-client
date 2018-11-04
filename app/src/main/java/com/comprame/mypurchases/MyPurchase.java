package com.comprame.mypurchases;

import com.comprame.buy.Card;

public class MyPurchase {
    public final String id;
    public final String itemId;
    public final int units;
    public final Card paymentMethod;
    public final String buyerId;

    public MyPurchase(String id, String itemId, int units, Card paymentMethod) {
        this.id = id;
        this.itemId = itemId;
        this.units = units;
        this.paymentMethod = paymentMethod;
        this.buyerId = "";
    }
}
