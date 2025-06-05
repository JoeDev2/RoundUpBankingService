package com.bank.roundup.model;

public class CurrencyAndAmount {

    private String currency;
    private Integer minorUnits;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getMinorUnits() {
        return minorUnits;
    }

    public void setMinorUnits(Integer minorUnits) {
        this.minorUnits = minorUnits;
    }

}