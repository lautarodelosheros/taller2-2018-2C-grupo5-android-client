package com.comprame.domain;

public class Purchase {
    public final String itemId;
    public final Card paymentMethod;

    public Purchase(String itemId, Card paymentMethod) {
        this.itemId = itemId;
        this.paymentMethod = paymentMethod;
    }
}
