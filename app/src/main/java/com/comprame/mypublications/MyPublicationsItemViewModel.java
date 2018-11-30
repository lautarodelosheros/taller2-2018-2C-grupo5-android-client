package com.comprame.mypublications;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.comprame.buy.BuyItem;
import com.comprame.sell.SellItem;

public class MyPublicationsItemViewModel extends ViewModel {
    public final SellItem sellItem;
    public final MutableLiveData<String> imageUrl = new MutableLiveData<>();

    public MyPublicationsItemViewModel(SellItem sellItem) {
        this.sellItem = sellItem;
        if (sellItem.imageUrls != null && !sellItem.imageUrls.isEmpty()) {
            imageUrl.setValue(sellItem.imageUrls.get(0));
        }
    }

    public String getDescription() {
        return sellItem.description;
    }

    public String getName() {
        return sellItem.name;
    }

    public String getPrice() {
        return String.valueOf(sellItem.unitPrice);
    }

    public LiveData<String> getImageUrl() {
        return imageUrl;
    }
}
