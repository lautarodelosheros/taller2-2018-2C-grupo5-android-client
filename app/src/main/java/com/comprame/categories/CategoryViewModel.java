package com.comprame.categories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.comprame.search.SearchItem;

public class CategoryViewModel extends ViewModel {
    public final String category;

    public CategoryViewModel(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
