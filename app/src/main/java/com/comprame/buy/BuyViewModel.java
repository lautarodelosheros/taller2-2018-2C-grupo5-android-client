package com.comprame.buy;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

public class BuyViewModel extends AndroidViewModel {

    public Purchase asPuchase() {
        return new Purchase(item.getId()
                , units.getValue()
                , card.asCard());
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


    public BuyViewModel(@NonNull Application application) {
        super(application);
        units.setValue(1);
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
}
