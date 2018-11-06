package com.comprame.overview;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.comprame.buy.BuyItem;

public class OverviewViewModel extends AndroidViewModel {

    public BuyItem item;
    public String seller;

    public OverviewViewModel(@NonNull Application application) {
        super(application);

    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getSeller() {
        return this.seller;
    }

}
