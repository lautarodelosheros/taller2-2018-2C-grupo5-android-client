package com.comprame.mysellings;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.comprame.buy.BuyItem;

public class OverviewMySellViewModel extends AndroidViewModel {

    public BuyItem item;

    public OverviewMySellViewModel(@NonNull Application application) {
        super(application);
    }

}
