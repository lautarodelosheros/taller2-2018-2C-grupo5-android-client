package com.comprame.domain;

public class Card {
    public final String number;
    public final String securityCode;
    public final String expirationMonth;
    public final String expirationYear;
    public final String owner;

    public Card(String number, String securityCode, String expirationMonth, String expirationYear, String owner) {
        this.number = number;
        this.securityCode = securityCode;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
        this.owner = owner;
    }
}
