package com.bank.roundup.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

/***
 *
 * class for modelling a transaction response from Bank Api
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

    private String feedItemUid;

    private TransactionDirection direction;
    private String transactionTime;
    private Amount amount;

    public static class Amount {

        private String currency;
        private Integer minorUnits;

        public Integer getMinorUnits() {
            return minorUnits;
        }

        public void setMinorUnits(Integer minorUnits) {
            this.minorUnits = minorUnits;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

    }

    public Transaction() {

    }

    public String getFeedItemUid() {
        return feedItemUid;
    }

    public void setFeedItemUid(String feedItemUid) {
        this.feedItemUid = feedItemUid;
    }

    public TransactionDirection getDirection() {
        return direction;
    }

    public void setDirection(TransactionDirection direction) {
        this.direction = direction;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountInPounds() {
        if (amount == null || amount.getMinorUnits() == null) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(amount.getMinorUnits()).divide(new BigDecimal(100));
    }

    public OffsetDateTime convertStringTransactionTimeToDateTime() {
        return OffsetDateTime.parse(transactionTime);
    }

    public LocalDate convertStringTransactionTimeToLocalDate() {
        return convertStringTransactionTimeToDateTime().toLocalDate();
    }
}