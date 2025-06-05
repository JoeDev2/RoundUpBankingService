package com.bank.roundup.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.bank.roundup.exception.BankApiException;
import com.bank.roundup.model.Account;
import com.bank.roundup.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * TransactionServiceInterface for business logic involving transactions -- used to retrieve transactions and calculate roundup amounts
 * 
 * For demonstration purposes, implementations may use mock data instead of real API calls.
 * Defines the contract for transaction services to reduce code repetition and make adding new implementations easier
 */
public interface TransactionServiceInterface {
    
    /**
     * Gets transactions for a specific time period and account
     * 
     * @param startDate Start date for transaction search
     * @param endDate End date for transaction search
     * @param account The account to search transactions for
     * @return List of transactions matching the criteria
     * @throws BankApiException if there's an API communication error
     * @throws JsonProcessingException if there's an error parsing the response
     */
    List<Transaction> getTransactionsForTimePeriod(LocalDate startDate, LocalDate endDate, Account account) 
            throws BankApiException, JsonProcessingException;
    
    /**
     * Calculates the total roundup amount for a list of transactions
     * 
     * @param transactions List of transactions to calculate roundup for
     * @return Total roundup amount in pounds
     */
    BigDecimal calculateRoundUpAmount(List<Transaction> transactions);
}