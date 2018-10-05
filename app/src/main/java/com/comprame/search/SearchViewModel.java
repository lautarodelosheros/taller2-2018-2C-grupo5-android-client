package com.comprame.search;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import com.comprame.domain.Item;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {

    public static class SearchFilter {
        public String name;
        public String location;
        public String description;
        public int size = 5;
        public int offset = 0;
    }

    private final MutableLiveData<SearchFilter> filter;
    private final MutableLiveData<List<Item>> items;

    public SearchViewModel() {
        filter = new MutableLiveData<>();
        items = new MutableLiveData<>();
        items.setValue(new ArrayList<>());
        filter.setValue(new SearchFilter());
    }

    public void observeForever(Observer<List<Item>> observer) {
        items.observeForever(observer);
    }

    public int size() {
        return items.getValue().size();
    }

    public List<Item> items() {
        return items.getValue();
    }

    public Item at(int i) {
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

    public void addItems(List<Item> items) {
        this.items.getValue().addAll(items);
        setItems(this.items.getValue());
    }

    public void setItems(List<Item> items) {
        this.items.setValue(items);
    }

    public void removeAllItems() {
        this.items.getValue().clear();
    }

    public SearchFilter filter() {
        return filter.getValue();
    }
}
