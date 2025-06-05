package com.bank.roundup.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.bank.roundup.exception.BankApiException;
import com.bank.roundup.model.Account;

import java.util.Currency;
import java.util.UUID;

/**
 * SavingsGoalServiceInterface for business logic involving savings goals -- currently used to create a savings goal and also to add funds to a savings goal
 * 
 * For demonstration purposes, implementations may use mock data instead of real API calls.
 * In production environment would refactor the put method into the abstract class and reuse the existing methods in the abstract superclass
 * Defines the contract for savings goal services to reduce code repetition and make adding new implementations easier
 */
public interface SavingsGoalServiceInterface {
    
    /**
     * Creates a new savings goal for the specified account
     * 
     * @param savingsGoalName Name of the savings goal
     * @param currency Currency for the savings goal
     * @param account Account to create the savings goal for
     * @return UUID of the created savings goal
     * @throws JsonProcessingException if there's an error processing the request
     * @throws BankApiException if there's an API communication error
     */
    UUID createSavingsGoal(String savingsGoalName, Currency currency, Account account) 
            throws JsonProcessingException, BankApiException;
    
    /**
     * Adds saved money to an existing savings goal
     * 
     * @param roundUpAmountPence Amount to add in pence
     * @param currency Currency of the amount
     * @param savingsGoalUid UUID of the savings goal
     * @param account Account associated with the savings goal
     * @throws BankApiException if there's an API communication error
     */
    void addSavedMoneyToSavingsGoal(int roundUpAmountPence, Currency currency, UUID savingsGoalUid, Account account) 
            throws BankApiException;
}
