package com.comprame.buy;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.comprame.domain.Item;
import com.comprame.domain.Purchase;

import java.util.List;

public class BuyViewModel extends AndroidViewModel {

    public Item item;
    public LiveData<Purchase> purcharse = new MutableLiveData<>();
    public BuyViewModel(@NonNull Application application) {
        super(application);
    }

}
