package com.comprame.mysellings;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.comprame.buy.BuyItem;

public class MySellingsItemViewModel extends ViewModel {
    public final BuyItem buyItem;
    public final MutableLiveData<String> imageUrl = new MutableLiveData<>();

    public MySellingsItemViewModel(BuyItem buyItem) {
        this.buyItem = buyItem;
        if (buyItem.getImages() != null &&!buyItem.getImages().isEmpty()) {
            imageUrl.setValue(buyItem.getImage(0));
        }
    }

    public String getDescription() {
        return buyItem.getDescription();
    }

    public String getName() {
        return buyItem.getName();
    }

    public String getPrice() {
        return String.valueOf(buyItem.getUnitPrice());
    }

    public LiveData<String> getImageUrl() {
        return imageUrl;
    }
}
