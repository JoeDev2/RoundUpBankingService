package com.bank.roundup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.bank.roundup.exception.BankApiException;
import com.bank.roundup.model.Account;
import com.bank.roundup.model.AccountType;
import com.bank.roundup.model.Transaction;
import com.bank.roundup.service.TransactionService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * TransactionServiceTest - Originally designed for Banking API Tech Challenge
 * 
 * IMPORTANT: This project was originally a tech challenge that used real banking APIs.
 * These tests were written to mock actual HTTP API responses from a banking transaction API.
 * 
 * The original implementation would make HTTP calls to endpoints like:
 * - GET /api/v2/accounts/{accountUid}/feed/category/{categoryUid}/transactions-between
 * 
 * Since then, the project has been generalized for portfolio purposes and now uses mock data instead 
 * of real API calls (USE_MOCK_DATA = true). Many HTTP-related tests are now redundant but are kept to 
 * demonstrate:
 * - Mocking complex API responses with realistic transaction data
 * - Testing business logic (roundup calculations) 
 * - Date filtering and transaction processing
 * - Integration testing with mocked external dependencies
 * 
 * The roundup calculation tests remain valuable as they test core business logic.
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    private static final String TEST_TOKEN = "API token";
    private Account testAccount;

    // This would have been a real response from the banking transaction API
    // Kept to show the complexity of real banking API responses we were handling
    private static final String ORIGINAL_BANKING_API_RESPONSE = 
        "{\"feedItems\":[" +
        "{\"feedItemUid\":\"e98f805c-98da-450e-98e0-edb24ee77063\"," +
        "\"categoryUid\":\"e6afee0c-f07e-4aee-82f6-575cc6372084\"," +
        "\"amount\":{\"currency\":\"GBP\",\"minorUnits\":638}," +
        "\"direction\":\"OUT\"," +
        "\"transactionTime\":\"2025-04-24T22:39:09.687Z\"," +
        "\"counterPartyName\":\"Coffee Shop\"," +
        "\"spendingCategory\":\"EATING_OUT\"}," +
        "{\"feedItemUid\":\"e6fc475f-dee4-4970-b142-6e4f686383d0\"," +
        "\"amount\":{\"currency\":\"GBP\",\"minorUnits\":2162}," +
        "\"direction\":\"OUT\"," +
        "\"transactionTime\":\"2025-04-24T11:40:47.569Z\"," +
        "\"counterPartyName\":\"Grocery Store\"," +
        "\"spendingCategory\":\"GROCERIES\"}" +
        "]}";

    static class TestableTransactionService extends TransactionService {
        public TestableTransactionService(String token) {
            super(token);
        }

        // Method kept to show how HTTP calls would be mocked in real API testing
        public String sendGetRequest(String url) throws BankApiException {
            return super.sendGetRequest(url);
        }
    }

    @Spy
    private TestableTransactionService transactionService = new TestableTransactionService(TEST_TOKEN);

    @Before
    public void setUp() throws BankApiException {
        // In original implementation, this would mock HTTP responses:
        // doReturn(ORIGINAL_BANKING_API_RESPONSE).when(transactionService).sendGetRequest(anyString());
        // Now redundant since service uses internal mock data
        
        testAccount = new Account();
        testAccount.setAccountUid(UUID.randomUUID());
        testAccount.setAccountType(AccountType.PRIMARY);
        testAccount.setName("Account Name");
        testAccount.setDefaultCategory(UUID.randomUUID());
    }

    @Test
    public void testConstruction() {
        // Basic constructor test - still valid for any implementation
        TransactionService transactionService = new TransactionService(TEST_TOKEN);
    }

    @Test
    @Ignore("Test kept for portfolio - originally tested API integration with date filtering")
    public void shouldGetTransactionsForUser() throws BankApiException, JsonProcessingException {
        // Originally tested that API calls correctly filtered transactions by date and direction
        // Now redundant as service uses mock data, but demonstrates integration testing approach

        // Current implementation still works but tests different behavior:
        List<Transaction> transactions = transactionService.getTransactionsForTimePeriod(
                LocalDate.now(),
                LocalDate.now(),
                testAccount
        );

        assertFalse(transactions.isEmpty());
        assertTrue("Should have at least 2 transactions", transactions.size() >= 2);
        assertTrue("Should have at most 5 transactions", transactions.size() <= 5);
        
        // Original test would have verified:
        // - Correct API endpoint called with account/category UUIDs
        // - Proper date range formatting for API
        // - JSON parsing of complex transaction structures
        // - Filtering of IN vs OUT transactions
    }

    // ========== BUSINESS LOGIC TESTS - STILL VALUABLE ==========
    // These tests validate core roundup calculation logic regardless of data source

    @Test
    public void roundUpAmountCalculatesZeroWithEmptyList() {
        // Core business logic test - always valuable
        List<Transaction> transactions = Collections.emptyList();
        BigDecimal result = transactionService.calculateRoundUpAmount(transactions);
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    public void roundUpAmountCalculatesWithWholeNumbers() {
        // Core business logic test - validates that whole pound amounts don't get rounded up
        List<Transaction> transactions = new ArrayList<>();

        Transaction transaction1 = new Transaction();
        Transaction.Amount amount1 = new Transaction.Amount();
        amount1.setMinorUnits(500); // £5.00 exactly
        transaction1.setAmount(amount1);

        Transaction transaction2 = new Transaction();
        Transaction.Amount amount2 = new Transaction.Amount();
        amount2.setMinorUnits(1000); // £10.00 exactly
        transaction2.setAmount(amount2);

        transactions.add(transaction1);
        transactions.add(transaction2);

        BigDecimal result = transactionService.calculateRoundUpAmount(transactions);
        assertEquals(0, result.compareTo(BigDecimal.ZERO)); // No roundup for whole pounds
    }

    @Test
    public void roundUpAmountCalculatesWithFractions() {
        // Core business logic test - validates actual roundup calculations
        // This is the heart of the "roundup savings" feature
        List<Transaction> transactions = new ArrayList<>();

        Transaction transaction1 = new Transaction();
        Transaction.Amount amount1 = new Transaction.Amount();
        amount1.setMinorUnits(543); // £5.43 -> round up to £6.00 = 57p
        transaction1.setAmount(amount1);

        Transaction transaction2 = new Transaction();
        Transaction.Amount amount2 = new Transaction.Amount();
        amount2.setMinorUnits(295); // £2.95 -> round up to £3.00 = 5p
        transaction2.setAmount(amount2);

        Transaction transaction3 = new Transaction();
        Transaction.Amount amount3 = new Transaction.Amount();
        amount3.setMinorUnits(1099); // £10.99 -> round up to £11.00 = 1p
        transaction3.setAmount(amount3);

        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);

        BigDecimal result = transactionService.calculateRoundUpAmount(transactions);
        // 57p + 5p + 1p = 63p = £0.63
        assertEquals(new BigDecimal("0.63"), result.setScale(2, RoundingMode.HALF_UP));
    }
}