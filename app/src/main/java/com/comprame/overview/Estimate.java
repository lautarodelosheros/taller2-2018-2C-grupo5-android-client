package com.comprame.overview;

import com.comprame.sell.Geolocation;

public class Estimate {
    public final Geolocation location;
    public final String item_id;
    public final int units;

    public Estimate(Geolocation location, String item_id, int units) {
        this.location = location;
        this.item_id = item_id;
        this.units = units;
    }
}
