package com.bank.roundup.model;

import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.UUID;

/**
 *  class used to model an account object from the API response
 *  uses enum for accountType to enforce correct values
 */

public class Account {

    public Account() {

    }

    private UUID accountUid;
    private AccountType accountType;
    private UUID defaultCategory;
    private Currency currency;
    private OffsetDateTime createdAt;
    private String name;

    public UUID getAccountUid() {
        return accountUid;
    }

    public void setAccountUid(UUID accountUid) {
        this.accountUid = accountUid;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public UUID getDefaultCategory() {
        return defaultCategory;
    }

    public void setDefaultCategory(UUID defaultCategory) {
        this.defaultCategory = defaultCategory;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}