package com.comprame.buy;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.comprame.library.view.Format;
import com.comprame.sell.Geolocation;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BuyViewModel extends AndroidViewModel {

    public Purchase asPuchase() {
        Purchase.DeliveryOrder deliveryOrder = null;
        if (includeDelivery.getValue()) {
            deliveryOrder = new Purchase.DeliveryOrder(deliveryLocation.getValue(),
                    Format.iso(deliveryDate.getValue()));
        }
        return new Purchase(item.getId()
                , units.getValue()
                , card.asCard()
                , deliveryOrder);
    }

    public class CardViewModel {
        public final MutableLiveData<String> ownerName;
        public final MutableLiveData<String> number;
        public final MutableLiveData<String> securityCode;
        public final MutableLiveData<String> expirationMonth;
        public final MutableLiveData<String> expirationYear;

        CardViewModel() {
            super();
            this.ownerName = new MutableLiveData<>();
            this.number = new MutableLiveData<>();
            this.securityCode = new MutableLiveData<>();
            this.expirationMonth = new MutableLiveData<>();
            this.expirationYear = new MutableLiveData<>();
        }

        Card asCard() {
            return new Card(number.getValue()
                    , securityCode.getValue()
                    , expirationMonth.getValue()
                    , expirationYear.getValue()
                    , ownerName.getValue());
        }
    }

    public BuyItem item;

    public final CardViewModel card = new CardViewModel();
    public final MutableLiveData<Integer> units = new MutableLiveData<>();
    public final MutableLiveData<Double> total = new MutableLiveData<>();
    public final MutableLiveData<String> seller = new MutableLiveData<>();
    public final MutableLiveData<Boolean> includeDelivery = new MutableLiveData<>();
    public final MutableLiveData<Geolocation> deliveryLocation = new MutableLiveData<>();
    public final MutableLiveData<Date> deliveryDate = new MutableLiveData<>();
    public final MutableLiveData<Double> deliveryCost = new MutableLiveData<>();


    public BuyViewModel(@NonNull Application application) {
        super(application);
        units.setValue(1);
        includeDelivery.setValue(false);
        includeDelivery.observeForever(b -> {
            if (item != null) {
                if (b && deliveryCost.getValue() != null)
                    total.setValue(units.getValue() * item.getUnitPrice() + deliveryCost.getValue());
                else
                    total.setValue(units.getValue() * item.getUnitPrice());
            }

        });
        deliveryCost.observeForever((cost) -> {
            if (cost != null) {
                total.setValue(total.getValue() + cost);
            }
        });
    }

    public void setSeller(String seller) {
        this.seller.setValue(seller);
    }

    public String getSeller() {
        return this.seller.getValue();
    }

    public void dec() {
        int value = units.getValue();
        if (value > 1) {
            units.setValue(--value);
            total.setValue(value * item.getUnitPrice());
        }
    }

    public void inc() {
        int value = units.getValue();
        units.setValue(++value);
        total.setValue(value * item.getUnitPrice());
    }

    public void withDelivery(boolean include) {
        includeDelivery.setValue(include);
    }
}
