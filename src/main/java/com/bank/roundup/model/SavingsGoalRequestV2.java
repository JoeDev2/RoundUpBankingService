package com.bank.roundup.model;

public class SavingsGoalRequestV2 {

    private String name;
    private String currency;
    private CurrencyAndAmount target;
    private String base62EncodedPhoto;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public CurrencyAndAmount getTarget() {
        return target;
    }

    public void setTarget(CurrencyAndAmount target) {
        this.target = target;
    }

    public String getBase62EncodedPhoto() {
        return base62EncodedPhoto;
    }

    public void setBase62EncodedPhoto(String base62EncodedPhoto) {
        this.base62EncodedPhoto = base62EncodedPhoto;
    }

}