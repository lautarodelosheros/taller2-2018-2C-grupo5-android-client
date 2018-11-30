package com.comprame.mypublications;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import com.comprame.buy.BuyItem;
import com.comprame.sell.SellItem;

import java.util.ArrayList;
import java.util.List;

public class MyPublicationsViewModel extends ViewModel {

    public final MutableLiveData<List<SellItem>> items;

    public MyPublicationsViewModel() {
        items = new MutableLiveData<>();
        items.setValue(new ArrayList<>());
    }

    public void observeForever(Observer<List<SellItem>> observer) {
        items.observeForever(observer);
    }

    public int size() {
        return items.getValue().size();
    }

    public void addItem(SellItem sellItem) {
        this.items.getValue().add(sellItem);
        setItems(this.items.getValue());
    }

    public void setItems(List<SellItem> sellItems) {
        this.items.setValue(sellItems);
    }

    public int positionOf(SellItem sellItem) {
        return items.getValue().indexOf(sellItem);
    }
}
