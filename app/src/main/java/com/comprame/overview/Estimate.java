package com.comprame.overview;

import com.comprame.sell.Geolocation;

public class Estimate {
    public final Geolocation geolocation;
    public final String item_id;
    public final int units;

    public Estimate(Geolocation geolocation, String item_id, int units) {
        this.geolocation = geolocation;
        this.item_id = item_id;
        this.units = units;
    }
}
