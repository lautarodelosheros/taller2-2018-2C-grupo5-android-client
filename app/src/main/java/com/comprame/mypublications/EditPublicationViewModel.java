package com.comprame.mypublications;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.comprame.sell.Geolocation;

import java.util.ArrayList;
import java.util.List;

public class EditPublicationViewModel extends AndroidViewModel {
    public final MutableLiveData<String> id = new MutableLiveData<>();
    public final MutableLiveData<String> name = new MutableLiveData<>();
    public final MutableLiveData<String> description = new MutableLiveData<>();
    public final MutableLiveData<String> units = new MutableLiveData<>();
    public final MutableLiveData<String> unitPrice = new MutableLiveData<>();
    public final MutableLiveData<Geolocation> geolocation = new MutableLiveData<>();
    public final MutableLiveData<List<String>> paymentMethod = new MutableLiveData<>();
    public final MutableLiveData<List<String>> categories = new MutableLiveData<>();
    public final MutableLiveData<Boolean> enabled = new MutableLiveData<>();
    private final List<String> imageUrls = new ArrayList<>();

    private Publication publication;

    public EditPublicationViewModel(Application app) {
        super(app);
        enabled.setValue(false);
        categories.setValue(new ArrayList<>());
        paymentMethod.setValue(new ArrayList<>());
        name.observeForever((s) -> enabled.setValue(this.isValid()));
        description.observeForever((s) -> enabled.setValue(this.isValid()));
        units.observeForever((s) -> enabled.setValue(this.isValid()));
        unitPrice.observeForever((s) -> enabled.setValue(this.isValid()));
    }


    public String descriptionError() {
        String value = description.getValue();
        if (value == null || value.isEmpty()) {
            return "Debe ingresar una descripción para el producto.";
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
                return "Debe ingresar una cantidad de unidades mayor o igual a uno.";
            }
        } catch (NumberFormatException e) {
            return "Debe ingresar una cantidad de unidades mayor o igual a uno.";
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

    public void addCategory(String category) {
        this.categories.getValue().add(category);
    }

    public Publication asPublication() {
        return new Publication(id.getValue()
                , name.getValue()
                , description.getValue()
                , Integer.valueOf(units.getValue())
                , Double.valueOf(unitPrice.getValue())
                , imageUrls
                , publication.getSellerId()
                , geolocation.getValue()
                , paymentMethod.getValue()
                , categories.getValue());
    }

    public void setGeolocation(Geolocation geolocation) {
        this.geolocation.setValue(geolocation);
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
        this.id.setValue(publication.getId());
        this.name.setValue(publication.getName());
        this.description.setValue(publication.getDescription());
        this.units.setValue(String.valueOf(publication.getUnits()));
        this.unitPrice.setValue(String.valueOf(publication.getUnitPrice()));
        this.paymentMethod.setValue(publication.getPaymentMethod());
        this.geolocation.setValue(publication.getGeolocation());
        this.categories.setValue(publication.getCategories());
        this.imageUrls.addAll(publication.getImageUrls());
    }
}
