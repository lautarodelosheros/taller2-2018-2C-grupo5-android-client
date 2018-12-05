package com.comprame.buy;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.comprame.R;
import com.comprame.library.view.Format;
import com.comprame.sell.Geolocation;

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

        public void observe(Runnable run) {
            this.ownerName.observeForever((x) -> run.run());
            this.number.observeForever((x) -> run.run());
            this.securityCode.observeForever((x) -> run.run());
            this.expirationMonth.observeForever((x) -> run.run());
            this.expirationYear.observeForever((x) -> run.run());
        }

        Card asCard() {
            return new Card(number.getValue()
                    , securityCode.getValue()
                    , expirationMonth.getValue()
                    , expirationYear.getValue()
                    , ownerName.getValue());
        }

        public String ownerNameError() {
            if (ownerName.getValue() == null || ownerName.getValue().isEmpty())
                return "Es requerida.";
            return null;
        }

        public String numberError() {
            if (number.getValue() == null || number.getValue().isEmpty()) return "Es requerida.";
            return null;
        }

        public String securityCodeError() {
            if (securityCode.getValue() == null || securityCode.getValue().isEmpty())
                return "Es requerida.";
            return null;
        }

        public String expirationMonthError() {
            if (expirationMonth.getValue() == null || expirationMonth.getValue().isEmpty())
                return "Es requerida.";
            return null;
        }

        public String expirationYearError() {
            if (expirationYear.getValue() == null || expirationYear.getValue().isEmpty())
                return "Es requerida.";
            return null;
        }

        public boolean isValid() {
            return ownerNameError() == null
                    && numberError() == null
                    && securityCodeError() == null
                    && expirationMonthError() == null
                    && expirationYearError() == null;
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
    public final MutableLiveData<String> buyText = new MutableLiveData<>();
    public final MutableLiveData<Boolean> enabled = new MutableLiveData<>();


    public BuyViewModel(@NonNull Application application) {
        super(application);
        units.setValue(1);
        enabled.setValue(false);
        includeDelivery.setValue(false);
        includeDelivery.observeForever(b -> {
            if (b) {
                buyText.setValue(application.getResources().getString(R.string.buy_with_delivery));
                if (item != null && deliveryCost.getValue() != null)
                    total.setValue(units.getValue() * item.getUnitPrice() + deliveryCost.getValue());
            } else {
                buyText.setValue(application.getResources().getString(R.string.buy_without_delivery));
                if (item != null)
                    total.setValue(units.getValue() * item.getUnitPrice());
            }
            enabled.setValue(isValid());
        });
        deliveryCost.observeForever((cost) -> {
            if (cost != null
                    && includeDelivery.getValue() != null
                    && includeDelivery.getValue()) {
                total.setValue(total.getValue() + cost);
            }
            enabled.setValue(isValid());
        });
        card.observe(() -> {
            enabled.setValue(isValid());
        });
        buyText.setValue(application.getResources().getString(R.string.buy_without_delivery));

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

    public String deliveryLocationError() {
        if (includeDelivery.getValue() && deliveryLocation.getValue() == null) {
            return "Direccion es requerida para el envio";
        }
        return null;
    }


    public String deliveryDateError() {
        if (includeDelivery.getValue() && deliveryDate.getValue() == null) {
            return "Fecha de entraga es requerida para el envio";
        }
        return null;
    }

    public boolean isValid() {
        return deliveryLocationError() == null
                && deliveryDateError() == null
                && card.isValid();
    }
}
