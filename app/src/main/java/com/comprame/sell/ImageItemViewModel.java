package com.comprame.sell;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ImageItemViewModel extends ViewModel {
    public final MutableLiveData<String> imageUrl = new MutableLiveData<>();

    public ImageItemViewModel(String imageUrl) {
        this.imageUrl.setValue(imageUrl);
    }

    public LiveData<String> getImageUrl() {
        return imageUrl;
    }
}
