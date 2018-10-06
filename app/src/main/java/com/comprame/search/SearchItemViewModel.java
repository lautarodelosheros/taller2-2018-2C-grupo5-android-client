package com.comprame.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.comprame.domain.Item;

public class SearchItemViewModel extends ViewModel {
    public final Item item;
    public final MutableLiveData<String> imageUrl = new MutableLiveData<>();

    public SearchItemViewModel(Item item) {
        this.item = item;
        imageUrl.setValue(item.getImage(0));
    }

    public String getDescription() {
        return item.getDescription();
    }

    public String getName() {
        return item.getName();
    }

    public String getPrice() {
        return String.valueOf(item.getUnitPrice());
    }

    public LiveData<String> getImageUrl() {
        return imageUrl;
    }
}
