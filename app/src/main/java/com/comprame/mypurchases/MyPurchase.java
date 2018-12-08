package com.comprame.mypurchases;

import com.comprame.buy.BuyItem;
import com.comprame.buy.Card;

public class MyPurchase {
    public final String id;
    public final String itemId;
    public final BuyItem item;
    public final int units;
    public final String status;
    public final Card paymentMethod;
    public final String buyerId;

    public MyPurchase(String id, String itemId, BuyItem item, int units, Card paymentMethod) {
        this.id = id;
        this.itemId = itemId;
        this.item = item;
        this.units = units;
        this.status = "";
        this.paymentMethod = paymentMethod;
        this.buyerId = "";
    }
}
