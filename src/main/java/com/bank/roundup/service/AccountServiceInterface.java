package com.bank.roundup.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.bank.roundup.model.Account;
import com.bank.roundup.exception.BankApiException;

import java.util.List;

/**
 * AccountServiceInterface for business logic involving bank accounts - currently, just getting the accounts
 * for the user via their authentication token.
 * 
 * For demonstration purposes, implementations may use mock data instead of real API calls.
 * Defines the contract for account services to reduce code repetition and make adding new implementations easier
 */
public interface AccountServiceInterface {
    
    /**
     * Gets the accounts for the authenticated user
     * 
     * @return List of user accounts
     * @throws BankApiException if there's an API communication error
     * @throws JsonProcessingException if there's an error parsing the response
     */
    List<Account> getAccountsForUser() throws BankApiException, JsonProcessingException;


}