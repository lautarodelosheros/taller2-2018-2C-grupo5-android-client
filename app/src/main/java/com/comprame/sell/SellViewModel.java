package com.comprame.sell;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.comprame.login.Session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SellViewModel extends AndroidViewModel {
    public final MutableLiveData<String> name = new MutableLiveData<>();
    public final MutableLiveData<String> description = new MutableLiveData<>();
    public final MutableLiveData<String> units = new MutableLiveData<>();
    public final MutableLiveData<String> unitPrice = new MutableLiveData<>();
    public final MutableLiveData<Geolocation> geolocation = new MutableLiveData<>();
    public final MutableLiveData<String> paymentMethod = new MutableLiveData<>();
    public final MutableLiveData<String> categories = new MutableLiveData<>();
    public final MutableLiveData<Boolean> enabled = new MutableLiveData<>();
    private final List<String> imageUrls = new ArrayList<>();

    public SellViewModel(Application app) {
        super(app);
        enabled.setValue(false);
        units.setValue("1");
        name.observeForever((s) -> enabled.setValue(this.isValid()));
        description.observeForever((s) -> enabled.setValue(this.isValid()));
        units.observeForever((s) -> enabled.setValue(this.isValid()));
        unitPrice.observeForever((s) -> enabled.setValue(this.isValid()));
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

    public String unitPriceError() {
        String unitPriceValue = unitPrice.getValue();
        try {
            if (unitPriceValue == null || Double.valueOf(unitPriceValue) <= 1) {
                return "Debe ingresar un precio mayor a uno.";
            }
        } catch (NumberFormatException e) {
            return "Debe ingresar un precio mayor a uno.";
        }
        return null;
    }

    public String unitsError() {
        String units = this.units.getValue();
        try {
            if (units == null || Double.valueOf(units) < 1) {
                return "Debe ingresar una cantidad de unidades mayor a uno.";
            }
        } catch (NumberFormatException e) {
            return "Debe ingresar una cantidad de unidades mayor a uno.";
        }
        return null;
    }

    private boolean isValid() {
        return descriptionError() == null
                && nameError() == null
                && unitPriceError() == null
                && unitsError() == null;
    }

    public void addImageUrl(String newUrl) {
        this.imageUrls.add(newUrl);
    }

    public SellItem asSellItem() {
        return new SellItem(name.getValue()
                , description.getValue()
                , Integer.valueOf(units.getValue())
                , Double.valueOf(unitPrice.getValue())
                , imageUrls
                , Session.getInstance().getSessionToken()
                , geolocation.getValue()
                , paymentMethod.getValue()
                , Arrays.asList(categories.getValue().split(" ")));
    }

    public void setGeolocation(Geolocation geolocation) {
        this.geolocation.setValue(geolocation);
    }
}
