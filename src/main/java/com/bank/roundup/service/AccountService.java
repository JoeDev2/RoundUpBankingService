package com.bank.roundup.service;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bank.roundup.config.BankApiConfig;
import com.bank.roundup.exception.BankApiException;
import com.bank.roundup.model.Account;
import com.bank.roundup.util.JsonMapper;

/**
 * AccountService for business logic involving bank accounts - currently, just getting the accounts
 * for the user via their authentication token.
 * 
 * For demonstration purposes, this service uses mock data instead of real API calls.
 * Extends the abstract service class to reduce code repetition across each service and make adding new services easier
 */
public class AccountService extends BankAbstractApiService implements AccountServiceInterface {

    private static final boolean USE_MOCK_DATA = true;

    public AccountService(String authToken) {
        super(authToken);
    }

    public List<Account> getAccountsForUser() throws BankApiException, JsonProcessingException {
        
        if (USE_MOCK_DATA) {
            // Use mock data for demonstration
            System.out.println("Using mock data for accounts");
            return MockDataService.generateMockAccounts();
        }
        
        // Original API call implementation (kept for reference)
        String responseBody = sendGetRequest(BankApiConfig.ACCOUNTS_URL);

        // get instance of object mapper
        ObjectMapper objectMapper = JsonMapper.getObjectMapperInstance();

        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode accountsNode = rootNode.get("accounts");
        
        // convert the accounts array to a List<Account>
        List<Account> accounts = Arrays.asList(objectMapper.readValue(accountsNode.toString(), Account[].class));

        return accounts;
    }
}