package com.comprame.mypublications;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class MyPublicationsItemViewModel extends ViewModel {
    public final Publication publication;
    public final MutableLiveData<String> imageUrl = new MutableLiveData<>();

    public MyPublicationsItemViewModel(Publication publication) {
        this.publication = publication;
        if (publication.getImageUrls() != null && !publication.getImageUrls().isEmpty()) {
            imageUrl.setValue(publication.getImageUrls().get(0));
        }
    }

    public String getDescription() {
        return publication.getDescription();
    }

    public String getName() {
        return publication.getName();
    }

    public String getPrice() {
        return String.valueOf(publication.getUnitPrice());
    }

    public LiveData<String> getImageUrl() {
        return imageUrl;
    }
}
