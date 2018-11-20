package com.comprame.categories;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import com.comprame.search.SearchItem;
import com.comprame.sell.Geolocation;

import java.util.ArrayList;
import java.util.List;

public class CategoriesViewModel extends ViewModel {

    private final MutableLiveData<List<String>> categories;

    public CategoriesViewModel() {
        categories = new MutableLiveData<>();
        categories.setValue(new ArrayList<>());
    }

    public void observeForever(Observer<List<String>> observer) {
        categories.observeForever(observer);
    }

    public int size() {
        return categories.getValue().size();
    }

    public List<String> items() {
        return categories.getValue();
    }

    public String at(int i) {
        return categories.getValue().get(i);
    }

    public void addItems(List<String> categories) {
        this.categories.getValue().addAll(categories);
        setItems(this.categories.getValue());
    }

    public void setItems(List<String> categories) {
        this.categories.setValue(categories);
    }

    public void removeAllItems() {
        this.categories.getValue().clear();
    }
}
