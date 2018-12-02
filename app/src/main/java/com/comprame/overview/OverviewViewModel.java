package com.comprame.overview;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.comprame.buy.BuyItem;
import com.comprame.sell.Geolocation;


public class OverviewViewModel extends AndroidViewModel {

    public BuyItem item;
    public String seller;
    public Geolocation location;
    public DeliveryEstimate delivery;
    public MutableLiveData<String> deliveryCost;

    public OverviewViewModel(@NonNull Application application) {
        super(application);
        deliveryCost = new MutableLiveData<>();
        setDelivery(delivery);
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getSeller() {
        return this.seller;
    }

    public void setDelivery(DeliveryEstimate delivery) {
        if (delivery != null) {
            deliveryCost.setValue("+ " + delivery.value + "(" + delivery.location.getAddress() + ")");
        } else {
            deliveryCost.setValue("");
        }
    }
}

