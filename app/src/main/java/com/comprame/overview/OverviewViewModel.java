package com.comprame.overview;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.comprame.buy.BuyItem;

public class OverviewViewModel extends AndroidViewModel {

    public BuyItem item;

    public OverviewViewModel(@NonNull Application application) {
        super(application);
    }

}
