package com.comprame.buy;

import com.comprame.sell.Geolocation;

public class Purchase {
    public static class DeliveryOrder {
        public final Geolocation geolocation;
        public final String date;

        public DeliveryOrder(Geolocation geolocation, String date) {
            this.geolocation = geolocation;
            this.date = date;
        }
    }
    public final String itemId;
    public final int units;
    public final Card paymentMethod;
    public final String buyerId;
    public final DeliveryOrder deliveryOrder;

    public Purchase(String itemId, int units, Card paymentMethod, DeliveryOrder deliveryOrder) {
        this.itemId = itemId;
        this.units = units;
        this.paymentMethod = paymentMethod;
        this.deliveryOrder = deliveryOrder;
        this.buyerId = "";
    }
}
