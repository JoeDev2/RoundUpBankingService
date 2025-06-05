package com.bank.roundup.model;

public enum AccountType {

    PRIMARY,
    SECONDARY,
    BUSINESS;


public static AccountType fromDisplayName(String displayName) {
    try {
        if (displayName == null) {
            throw new IllegalArgumentException("Account type is null");
        }
        return valueOf(displayName);
    } catch (IllegalArgumentException exception) {
        throw new IllegalArgumentException("Invalid account type: " + displayName);
    }
}
}