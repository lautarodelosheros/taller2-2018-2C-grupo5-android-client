package com.comprame.search;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import com.comprame.sell.Geolocation;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {

    public static class SearchFilter {
        public String name;
        public Geolocation geolocation;
        public String description;
        public String kilometers;
        public int size = 5;
        public int offset = 0;
    }

    private final MutableLiveData<SearchFilter> filter;
    private final MutableLiveData<List<SearchItem>> items;

    public SearchViewModel() {
        filter = new MutableLiveData<>();
        items = new MutableLiveData<>();
        items.setValue(new ArrayList<>());
        filter.setValue(new SearchFilter());
    }

    public void observeForever(Observer<List<SearchItem>> observer) {
        items.observeForever(observer);
    }

    public int size() {
        return items.getValue().size();
    }

    public List<SearchItem> items() {
        return items.getValue();
    }

    public SearchItem at(int i) {
        return items.getValue().get(i);
    }

    public void incOffset(int i) {
        // Should Check for total items available
        SearchFilter value = this.filter.getValue();
        value.offset += i;
        this.filter.postValue(value);
    }

    public void setOffset(int i) {
        SearchFilter value = this.filter.getValue();
        value.offset = i;
        this.filter.postValue(value);
    }

    public void addItems(List<SearchItem> searchItems) {
        this.items.getValue().addAll(searchItems);
        setItems(this.items.getValue());
    }

    public void setItems(List<SearchItem> searchItems) {
        this.items.setValue(searchItems);
    }

    public void removeAllItems() {
        this.items.getValue().clear();
    }

    public SearchFilter filter() {
        return filter.getValue();
    }
}
