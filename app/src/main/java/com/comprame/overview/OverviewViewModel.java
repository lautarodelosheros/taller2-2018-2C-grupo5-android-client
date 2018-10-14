package com.comprame.overview;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.comprame.buy.BuyItem;
import com.comprame.buy.Card;
import com.comprame.buy.Purchase;

public class OverviewViewModel extends AndroidViewModel {

    public BuyItem item;

    public OverviewViewModel(@NonNull Application application) {
        super(application);
    }

}
