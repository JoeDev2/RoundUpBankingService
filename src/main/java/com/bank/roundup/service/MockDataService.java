package com.bank.roundup.service;

import com.bank.roundup.model.Account;
import com.bank.roundup.model.AccountType;
import com.bank.roundup.model.Transaction;
import com.bank.roundup.model.TransactionDirection;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Mock data service that generates realistic banking data for demonstration purposes.
 * This replaces actual API calls with in-memory generated data to showcase the roundup functionality.
 */
public class MockDataService {

    private static final Random random = new Random();
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private static final String[] MERCHANT_NAMES = {
            "Coffee Shop Ltd", "Grocery Store", "Gas Station", "Online Shopping",
            "Restaurant Group", "Pharmacy Chain", "Bookstore", "Tech Store",
            "Fast Food Chain", "Department Store", "Auto Service", "Utility Company"
    };

    /**
     * Generates a list of mock user accounts
     */
    public static List<Account> generateMockAccounts() {
        List<Account> accounts = new ArrayList<>();
        
        // Primary account
        Account primaryAccount = new Account();
        primaryAccount.setAccountUid(UUID.fromString("12345678-1234-1234-1234-123456789012"));
        primaryAccount.setAccountType(AccountType.PRIMARY);
        primaryAccount.setDefaultCategory(UUID.fromString("87654321-4321-4321-4321-210987654321"));
        primaryAccount.setName("Personal Current Account");
        primaryAccount.setCurrency(Currency.getInstance("GBP"));
        accounts.add(primaryAccount);
        
        // Secondary savings account
        Account savingsAccount = new Account();
        savingsAccount.setAccountUid(UUID.fromString("98765432-9876-9876-9876-987654321098"));
        savingsAccount.setAccountType(AccountType.SECONDARY);
        savingsAccount.setDefaultCategory(UUID.fromString("11111111-2222-3333-4444-555555555555"));
        savingsAccount.setName("High Yield Savings Account");
        savingsAccount.setCurrency(Currency.getInstance("GBP"));
        accounts.add(savingsAccount);
        
        // Business account
        Account businessAccount = new Account();
        businessAccount.setAccountUid(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        businessAccount.setAccountType(AccountType.BUSINESS);
        businessAccount.setDefaultCategory(UUID.fromString("22222222-3333-4444-5555-666666666666"));
        businessAccount.setName("Business Current Account");
        businessAccount.setCurrency(Currency.getInstance("GBP"));
        accounts.add(businessAccount);
        
        return accounts;
    }

    /**
     * Generates mock transactions for a given date range
     */
    public static List<Transaction> generateMockTransactions(LocalDate startDate, LocalDate endDate, Account account) {
        List<Transaction> transactions = new ArrayList<>();
        
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            // Generate 2-5 transactions per day
            int transactionsPerDay = 2 + random.nextInt(4);
            
            for (int i = 0; i < transactionsPerDay; i++) {
                Transaction transaction = createMockTransaction(currentDate, i);
                transactions.add(transaction);
            }
            
            currentDate = currentDate.plusDays(1);
        }
        
        return transactions;
    }

    private static Transaction createMockTransaction(LocalDate date, int sequenceInDay) {
        Transaction transaction = new Transaction();
        
        transaction.setFeedItemUid(UUID.randomUUID().toString());
        transaction.setDirection(TransactionDirection.OUT);
        
        // Generate transaction time within the day
        int hour = 8 + (sequenceInDay * 3) % 16; // Spread throughout day
        int minute = random.nextInt(60);
        int second = random.nextInt(60);
        
        String transactionTime = date.atTime(hour, minute, second)
                .atZone(ZoneOffset.UTC)
                .format(ISO_FORMATTER);
        transaction.setTransactionTime(transactionTime);
        
        // Generate realistic transaction amounts (in pence)
        Transaction.Amount amount = new Transaction.Amount();
        amount.setCurrency("GBP");
        
        // Generate amounts between £1.50 and £45.99 with realistic distribution
        int minorUnits = generateRealisticAmount();
        amount.setMinorUnits(minorUnits);
        
        transaction.setAmount(amount);
        
        return transaction;
    }

    private static int generateRealisticAmount() {
        // Create realistic spending patterns
        double randomValue = random.nextDouble();
        
        if (randomValue < 0.3) {
            // Small purchases £1.50 - £8.99
            return 150 + random.nextInt(750);
        } else if (randomValue < 0.6) {
            // Medium purchases £9.00 - £25.99
            return 900 + random.nextInt(1700);
        } else if (randomValue < 0.85) {
            // Larger purchases £26.00 - £45.99
            return 2600 + random.nextInt(2000);
        } else {
            // Occasional big purchases £46.00 - £120.00
            return 4600 + random.nextInt(7400);
        }
    }

    /**
     * Generates a mock savings goal creation response
     */
    public static String generateMockSavingsGoalResponse(String goalName) {
        UUID savingsGoalUid = UUID.randomUUID();
        return String.format(
            "{\"savingsGoalUid\":\"%s\",\"name\":\"%s\",\"currency\":\"GBP\",\"status\":\"ACTIVE\"}",
            savingsGoalUid.toString(),
            goalName
        );
    }

    /**
     * Generates a mock top-up response
     */
    public static String generateMockTopUpResponse(UUID transferUid) {
        return String.format(
            "{\"transferUid\":\"%s\",\"success\":true,\"status\":\"COMPLETED\"}",
            transferUid.toString()
        );
    }

    /**
     * Generates a mock accounts list response in JSON format
     */
    public static String generateMockAccountsResponse() {
        return "{\"accounts\":[{" +
                "\"accountUid\":\"12345678-1234-1234-1234-123456789012\"," +
                "\"accountType\":\"PRIMARY\"," +
                "\"defaultCategory\":\"87654321-4321-4321-4321-210987654321\"," +
                "\"currency\":\"GBP\"," +
                "\"name\":\"Personal Current Account\"" +
                "}]}";
    }

    /**
     * Generates a mock transactions response in the expected JSON format
     */
    public static String generateMockTransactionsResponse(LocalDate startDate, LocalDate endDate) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\"feedItems\":[");
        
        List<Transaction> transactions = generateMockTransactions(startDate, endDate, null);
        
        for (int i = 0; i < transactions.size(); i++) {
            Transaction tx = transactions.get(i);
            if (i > 0) jsonBuilder.append(",");
            
            jsonBuilder.append("{")
                    .append("\"feedItemUid\":\"").append(tx.getFeedItemUid()).append("\",")
                    .append("\"direction\":\"").append(tx.getDirection()).append("\",")
                    .append("\"transactionTime\":\"").append(tx.getTransactionTime()).append("\",")
                    .append("\"amount\":{")
                    .append("\"currency\":\"").append(tx.getAmount().getCurrency()).append("\",")
                    .append("\"minorUnits\":").append(tx.getAmount().getMinorUnits())
                    .append("}")
                    .append("}");
        }
        
        jsonBuilder.append("]}");
        return jsonBuilder.toString();
    }
} 