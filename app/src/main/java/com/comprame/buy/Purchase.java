package com.comprame.buy;

public class Purchase {
    public final String itemId;
    public final int units;
    public final Card paymentMethod;

    public Purchase(String itemId, int units, Card paymentMethod) {
        this.itemId = itemId;
        this.units = units;
        this.paymentMethod = paymentMethod;
    }
}
