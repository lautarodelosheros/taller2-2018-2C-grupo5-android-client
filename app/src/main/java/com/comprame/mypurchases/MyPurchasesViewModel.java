package com.comprame.mypurchases;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import com.comprame.buy.BuyItem;

import java.util.ArrayList;
import java.util.List;

public class MyPurchasesViewModel extends ViewModel {

    public final MutableLiveData<List<MyPurchase>> items;

    public MyPurchasesViewModel() {
        items = new MutableLiveData<>();
        items.setValue(new ArrayList<>());
    }

    public void observeForever(Observer<List<MyPurchase>> observer) {
        items.observeForever(observer);
    }

    public int size() {
        return items.getValue().size();
    }

    public void addItem(MyPurchase buyItem) {
        this.items.getValue().add(buyItem);
        setItems(this.items.getValue());
    }

    public void setItems(List<MyPurchase> buyItems) {
        this.items.setValue(buyItems);
    }

    public int positionOf(MyPurchase buyItem) {
        return items.getValue().indexOf(buyItem);
    }
}
