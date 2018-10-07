package com.comprame.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class SearchItemViewModel extends ViewModel {
    public final SearchItem searchItem;
    public final MutableLiveData<String> imageUrl = new MutableLiveData<>();

    public SearchItemViewModel(SearchItem searchItem) {
        this.searchItem = searchItem;
        if (searchItem.imageUrls != null &&!searchItem.imageUrls.isEmpty()) {
            imageUrl.setValue(searchItem.imageUrls.get(0));
        }
    }

    public String getDescription() {
        return searchItem.description;
    }

    public String getName() {
        return searchItem.name;
    }

    public String getPrice() {
        return String.valueOf(searchItem.unitPrice);
    }

    public LiveData<String> getImageUrl() {
        return imageUrl;
    }
}
