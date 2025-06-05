package com.bank.roundup.model;

public class TopUpRequestV2 {

    private CurrencyAndAmount amount;

    public CurrencyAndAmount getAmount() {
        return amount;
    }

    public void setAmount(CurrencyAndAmount amount) {
        this.amount = amount;
    }
}