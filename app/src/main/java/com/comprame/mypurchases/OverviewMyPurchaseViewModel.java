package com.comprame.mypurchases;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.comprame.buy.BuyItem;

public class OverviewMyPurchaseViewModel extends AndroidViewModel {

    public MyPurchase purchase;

    public OverviewMyPurchaseViewModel(@NonNull Application application) {
        super(application);
    }

    public BuyItem getItem() {
        return purchase.item;
    }

}
