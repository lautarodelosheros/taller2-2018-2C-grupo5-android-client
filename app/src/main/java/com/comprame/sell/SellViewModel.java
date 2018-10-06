package com.comprame.sell;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.comprame.domain.SellItem;

import java.util.List;

public class SellViewModel extends AndroidViewModel {
    public final MutableLiveData<String> name = new MutableLiveData<>();
    public final MutableLiveData<String> description = new MutableLiveData<>();
    public final MutableLiveData<String> units = new MutableLiveData<>();
    public final MutableLiveData<String> price = new MutableLiveData<>();
    public final MutableLiveData<String> location = new MutableLiveData<>();
    public final MutableLiveData<String> paymentMethod = new MutableLiveData<>();
    public final MutableLiveData<List<String>> categories = new MutableLiveData<>();
    public final MutableLiveData<Boolean> enabled = new MutableLiveData<>();

    public SellViewModel(Application app) {
        super(app);
        enabled.setValue(false);
        units.setValue("1");
        price.setValue("0");
        name.observeForever((s) -> enabled.setValue(this.isValid()));
        description.observeForever((s) -> enabled.setValue(this.isValid()));
        units.observeForever((s) -> enabled.setValue(this.isValid()));
        price.observeForever((s) -> enabled.setValue(this.isValid()));
    }


    public String descriptionError() {
        String value = description.getValue();
        if (value == null || value.isEmpty()) {
            return "Debe ingresar una descripcion para el producto.";
        }
        return null;
    }

    public String nameError() {
        String value = name.getValue();
        if (value == null || value.isEmpty()) {
            return "Debe ingresar un nombre para el producto.";
        }
        return null;
    }

    public String unitPrice() {
        String unitPriceValue = price.getValue();
        if (unitPriceValue == null || Double.valueOf(unitPriceValue) <= 1) {
            return "Debe ingresar un precio mayor a uno.";
        }
        return null;
    }

    boolean isValid() {
        return descriptionError() == null
                && nameError() == null
                && unitPrice() == null;
    }

    public SellItem asSellItem() {
        return new SellItem(name.getValue()
                , description.getValue()
                , Integer.valueOf(units.getValue())
                , Double.valueOf(price.getValue())
                , location.getValue()
                , paymentMethod.getValue()
                , categories.getValue());
    }
}
