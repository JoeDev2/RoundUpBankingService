package com.bank.roundup.model;

public enum TransactionDirection {

    IN,
    OUT;

public static TransactionDirection fromDisplayName(String displayName) {
    try {
        if (displayName == null) {
            throw new IllegalArgumentException("TransactionDirection is null");
        }
        return valueOf(displayName);
    } catch (IllegalArgumentException exception) {
        throw new IllegalArgumentException("Invalid Transaction Direction: " + displayName);
    }
}
}