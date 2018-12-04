package com.comprame.overview;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.comprame.buy.BuyItem;
import com.comprame.sell.Geolocation;

import java.util.Date;


public class OverviewViewModel extends AndroidViewModel {

    public BuyItem item;
    public String seller;
    public Geolocation geolocation;
    public DeliveryEstimate delivery;
    public MutableLiveData<String> deliveryCost;
    public MutableLiveData<Date> deliveryDate;


    public OverviewViewModel(@NonNull Application application) {
        super(application);
        deliveryCost = new MutableLiveData<>();
        deliveryDate = new MutableLiveData<>();
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
            deliveryCost.setValue("+ " + delivery.value + "(" + delivery.geolocation.getAddress() + ")");
        } else {
            deliveryCost.setValue("");
        }
    }
}

