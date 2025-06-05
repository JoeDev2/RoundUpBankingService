package com.bank.roundup.exception;

public class BankApiException extends Exception {

    public BankApiException(String msg) {
        super(msg);
    }

    public BankApiException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BankApiException(Throwable cause) {
        super(cause);
    }
}