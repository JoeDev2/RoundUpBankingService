package com.bank.roundup.config;

public class BankApiConfig {

    public static final String BASE_URL = "https://api.demobank.com/v1/";

    public static final String TRANSACTIONS_URL = "accounts/{accountUid}/categories/{categoryUid}/transactions";
    public static final String ACCOUNTS_URL = "accounts";
    public static final String SAVINGS_GOALS_URL = "accounts/{accountUid}/savings-goals";
    public static final String ADD_MONEY_TO_SAVINGS_GOAL_URL = "accounts/{accountUid}/savings-goals/{savingsGoalUid}/transfer/{transferUid}";

    private BankApiConfig() {
        // Utility class
    }
}