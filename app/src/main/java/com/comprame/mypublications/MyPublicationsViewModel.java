package com.comprame.mypublications;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyPublicationsViewModel extends ViewModel {

    public final MutableLiveData<List<Publication>> publications;

    public MyPublicationsViewModel() {
        publications = new MutableLiveData<>();
        publications.setValue(new ArrayList<>());
    }

    public void observeForever(Observer<List<Publication>> observer) {
        publications.observeForever(observer);
    }

    public int size() {
        return publications.getValue().size();
    }

    public void addItem(Publication publication) {
        this.publications.getValue().add(publication);
        setItems(this.publications.getValue());
    }

    public void setItems(List<Publication> publications) {
        this.publications.setValue(publications);
    }

    public int positionOf(Publication publication) {
        return publications.getValue().indexOf(publication);
    }
}
