package com.bank.roundup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.bank.roundup.exception.BankApiException;
import com.bank.roundup.model.Account;
import com.bank.roundup.service.AccountService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Currency;
import java.util.List;

import static org.junit.Assert.*;

/**
 * AccountServiceTest - Originally designed for Banking API Tech Challenge
 * 
 * IMPORTANT: This project was originally a tech challenge that used real banking APIs.
 * These tests were written to mock actual HTTP API responses from a banking API.
 * 
 * Since then, the project has been generalized for portfolio purposes and now uses mock data instead 
 * of real API calls (USE_MOCK_DATA = true). Many of these tests are now redundant but are kept to 
 * demonstrate:
 * - Ability to write comprehensive unit tests
 * - Mocking HTTP responses and API interactions  
 * - Testing error scenarios and edge cases
 * - Using Mockito for test doubles and verification
 * 
 * In a real production environment, these tests would be valuable for ensuring API integration works correctly.
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    private static final String TEST_TOKEN = "API token";

    // Sample API response that would have been returned by the banking API
    private static final String ORIGINAL_API_MOCK_RESPONSE = 
        "{\"accounts\":[{" +
        "\"accountUid\":\"e6af0d3e-7346-4f85-8d5e-2ec980cac8f7\"," +
        "\"accountType\":\"PRIMARY\"," +
        "\"defaultCategory\":\"e6afee0c-f07e-4aee-82f6-575cc6372084\"," +
        "\"currency\":\"GBP\"," +
        "\"createdAt\":\"2025-04-24T10:23:22.259Z\"," +
        "\"name\":\"Personal\"" +
        "}]}";

    static class TestableAccountService extends AccountService {
        public TestableAccountService(String token) {
            super(token);
        }
        
        // Method kept to show how HTTP calls would be mocked in real API testing
        public String sendGetRequest(String url) throws BankApiException {
            return super.sendGetRequest(url);
        }
    }

    @Spy
    private TestableAccountService accountService = new TestableAccountService(TEST_TOKEN);

    @Before
    public void setUp() throws BankApiException {
        // In original implementation, this would mock HTTP responses:
        // doReturn(ORIGINAL_API_MOCK_RESPONSE).when(accountService).sendGetRequest(anyString());
        // Now redundant since service uses internal mock data
    }

    @Test
    public void testConstruction() {
        // Basic constructor test - still valid
        AccountService testAccountService = new AccountService(TEST_TOKEN);
        // Verify service can be instantiated without issues
    }

    @Test
    public void shouldGetAccountsForUser() throws BankApiException, JsonProcessingException {
        // This test now works with internal mock data instead of mocked API responses
        List<Account> userAccounts = accountService.getAccountsForUser();

        assertFalse(userAccounts.isEmpty());
        assertEquals(3, userAccounts.size()); // Updated to match current mock data
        assertEquals(userAccounts.get(0).getCurrency(), Currency.getInstance("GBP"));
        assertEquals("12345678-1234-1234-1234-123456789012", userAccounts.get(0).getAccountUid().toString());
    }

    // Tests below are kept to demonstrate API testing capabilities but are now redundant
    // since the service no longer makes HTTP calls
    
    @Test
    @Ignore("Test kept for portfolio - originally tested API endpoint validation")
    public void shouldVerifyCorrectEndpointCalled() throws BankApiException, JsonProcessingException {
        // Originally verified that correct banking API endpoint was called
        // Now redundant as service uses mock data directly
        // This demonstrates ability to verify HTTP interactions in real API testing
        
        // Original test would have been:
        // accountService.getAccountsForUser();
        // verify(accountService, times(1)).sendGetRequest(BankApiConfig.ACCOUNTS_URL);
    }

    @Test
    @Ignore("Test kept for portfolio - originally tested JSON parsing error handling")
    public void shouldHandleInvalidJsonResponse() throws BankApiException, JsonProcessingException {
        // Originally tested handling of malformed JSON from API
        // Demonstrates error handling and exception testing capabilities
        
        // Original test would have been:
        // String invalidJson = "{invalid-json}";
        // doReturn(invalidJson).when(accountService).sendGetRequest(anyString());
        // expectedException.expect(JsonProcessingException.class);
        // accountService.getAccountsForUser();
    }

    @Test
    @Ignore("Test kept for portfolio - originally tested API exception propagation") 
    public void shouldPropagateApiExceptions() throws BankApiException, JsonProcessingException {
        // Originally tested that API communication errors were properly propagated
        // Shows understanding of exception handling in service layers
        
        // Original test would have been:
        // doThrow(new BankApiException("API error")).when(accountService).sendGetRequest(anyString());
        // expectedException.expect(BankApiException.class);
        // accountService.getAccountsForUser();
    }
}