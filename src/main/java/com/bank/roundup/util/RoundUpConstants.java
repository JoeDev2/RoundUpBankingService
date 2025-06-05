package com.bank.roundup.util;

import java.math.BigDecimal;

/**
 * Centralized constants for the RoundUp application
 * Contains all hardcoded values used throughout the application to improve maintainability
 */
public final class RoundUpConstants {

    // Financial Constants
    public static final int PENCE_PER_POUND = 100;
    public static final String DEFAULT_CURRENCY_CODE = "GBP";
    public static final int DEFAULT_DATE_RANGE_DAYS = 7; // One week
    public static final BigDecimal HUNDRED = new BigDecimal(100);
    public static final int DECIMAL_PLACES_PRECISION = 4;

    // User Input Validation
    public static final int MIN_SAVINGS_GOAL_NAME_LENGTH = 3;
    public static final int MAX_SAVINGS_GOAL_NAME_LENGTH = 50;
    public static final int MIN_MENU_OPTION = 1;
    public static final int MAX_MENU_OPTION = 2;

    // User Interface Messages
    public static final String WELCOME_MESSAGE = "Welcome to the Financial RoundUp Service";
    public static final String FEATURE_COMING_SOON = "Feature coming soon, please create a new goal for now";
    public static final String USING_MOCK_DATA_ACCOUNTS = "Using mock data for accounts";
    public static final String USING_MOCK_DATA_TRANSACTIONS = "Using mock data for transactions from {} to {}";
    public static final String CREATING_MOCK_SAVINGS_GOAL = "Creating mock savings goal: {}";
    public static final String CREATED_SAVINGS_GOAL_ID = "Created savings goal with ID: {}";
    public static final String ADDING_MONEY_TO_GOAL = "Adding £{} to savings goal: {}";
    public static final String TRANSFER_COMPLETED = "Transfer completed successfully!";

    // CLI Prompts
    public static final String MENU_QUESTION = "Are you using an existing savings goal? or creating a new one?";
    public static final String MENU_OPTION_1 = "1. Create new savings goal";
    public static final String MENU_OPTION_2 = "2. Use existing savings goal";
    public static final String DATE_PROMPT = "Please enter the starting date for your roundup savings (yyyy-MM-dd)";
    public static final String NEW_GOAL_NAME_PROMPT = "Please enter a name for your new savings goal (3-50 characters):";
    public static final String EXISTING_GOAL_NAME_PROMPT = "Please enter the name of your existing savings goal:";
    public static final String ACCOUNT_SELECTION_PROMPT = "Found multiple accounts, please pick one for the round up process:";
    public static final String SINGLE_ACCOUNT_FOUND = "Found your account: {}";
    public static final String NO_ACCOUNTS_FOUND = "No accounts found";

    // Validation Messages
    public static final String VALIDATION_EMPTY_INPUT = "Please enter a number";
    public static final String VALIDATION_INVALID_NUMBER = "Please enter a valid number";
    public static final String VALIDATION_MENU_RANGE = "Please enter 1 or 2";
    public static final String VALIDATION_ACCOUNT_RANGE = "Please enter a number between 1 and {}";
    public static final String VALIDATION_DATE_EMPTY = "Date cannot be empty";
    public static final String VALIDATION_DATE_FUTURE = "Date cannot be in the future";
    public static final String VALIDATION_DATE_FORMAT = "Invalid date format. Please use yyyy-MM-dd format";
    public static final String VALIDATION_GOAL_NAME_EMPTY = "Savings goal name cannot be empty";
    public static final String VALIDATION_GOAL_NAME_TOO_SHORT = "Savings goal name must be at least {} characters";
    public static final String VALIDATION_GOAL_NAME_TOO_LONG = "Savings goal name cannot exceed {} characters";

    // Application Messages
    public static final String QUANTITY_OF_TRANSACTIONS = "Quantity of transactions: {}";
    public static final String AMOUNT_TO_BE_SAVED = "Amount to be saved from roundup: {}p";

    // Mock Data Constants
    public static final String MOCK_ACCOUNT_UID = "12345678-1234-1234-1234-123456789012";
    public static final String MOCK_CATEGORY_UID = "87654321-4321-4321-4321-210987654321";
    public static final String MOCK_ACCOUNT_NAME = "Personal Current Account";

    // API Configuration
    public static final boolean USE_MOCK_DATA_DEFAULT = true;

    // Date/Time Formatting
    public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    // Transaction Amounts (in pence for mock data generation)
    public static final int MIN_SMALL_PURCHASE = 150;     // £1.50
    public static final int MAX_SMALL_PURCHASE = 899;     // £8.99
    public static final int MIN_MEDIUM_PURCHASE = 900;    // £9.00
    public static final int MAX_MEDIUM_PURCHASE = 2599;   // £25.99
    public static final int MIN_LARGE_PURCHASE = 2600;    // £26.00
    public static final int MAX_LARGE_PURCHASE = 4599;    // £45.99
    public static final int MIN_BIG_PURCHASE = 4600;      // £46.00
    public static final int MAX_BIG_PURCHASE = 12000;     // £120.00

    // Transaction Distribution Probabilities (for mock data)
    public static final double SMALL_PURCHASE_PROBABILITY = 0.3;
    public static final double MEDIUM_PURCHASE_PROBABILITY = 0.6;
    public static final double LARGE_PURCHASE_PROBABILITY = 0.85;

    // Transaction Generation
    public static final int MIN_TRANSACTIONS_PER_DAY = 2;
    public static final int MAX_ADDITIONAL_TRANSACTIONS_PER_DAY = 4; // 2-5 total

    private RoundUpConstants() {
        // Utility class - prevent instantiation
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
}