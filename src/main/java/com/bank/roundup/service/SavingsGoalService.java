package com.bank.roundup.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.Currency;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bank.roundup.config.BankApiConfig;
import com.bank.roundup.exception.BankApiException;
import com.bank.roundup.model.Account;
import com.bank.roundup.model.CurrencyAndAmount;
import com.bank.roundup.model.SavingsGoalRequestV2;
import com.bank.roundup.model.TopUpRequestV2;
import com.bank.roundup.util.JsonMapper;

/**
 * SavingsGoalService for business logic involving savings goals -- currently used to create a savings goal and also to add funds to a savings goal
 * 
 * For demonstration purposes, this service uses mock data instead of real API calls.
 * In production environment would refactor the put method into the abstract class and reuse the existing methods in the abstract superclass
 * Extends the abstract service class to reduce code repetition across each service and make adding new services easier
 */
public class SavingsGoalService extends BankAbstractApiService implements SavingsGoalServiceInterface {

    private static final boolean USE_MOCK_DATA = true;
    private static final String JSON_PROCESSING_ERROR = "json writing from savings goal failed";

    public SavingsGoalService(String authToken) {
        super(authToken);
    }

    public UUID createSavingsGoal(String savingsGoalName, Currency currency, Account account) 
            throws JsonProcessingException, BankApiException {

        if (USE_MOCK_DATA) {
            return handleMockSavingsGoalCreation(savingsGoalName);
        }

        String createSavingsGoalUrl = buildSavingsGoalUrl(account);
        SavingsGoalRequestV2 request = createSavingsGoalRequest(savingsGoalName, currency);
        String requestBody = serializeToJson(request);
        String responseBody = sendPutRequest(createSavingsGoalUrl, requestBody);

        return extractSavingsGoalUidFromResponse(responseBody);
    }

    public void addSavedMoneyToSavingsGoal(int roundUpAmountPence, Currency currency, UUID savingsGoalUid, Account account) 
            throws BankApiException {

        if (USE_MOCK_DATA) {
            handleMockMoneyTransfer(roundUpAmountPence, savingsGoalUid);
            return;
        }

        String addMoneyUrl = buildAddMoneyUrl(account, savingsGoalUid);
        TopUpRequestV2 request = createTopUpRequest(roundUpAmountPence, currency);
        String requestBody = serializeToJson(request);
        sendPutRequest(addMoneyUrl, requestBody);
    }

    // Helper methods for mock data handling
    private UUID handleMockSavingsGoalCreation(String savingsGoalName) {
        System.out.println("Creating mock savings goal: " + savingsGoalName);
        UUID mockSavingsGoalUid = UUID.randomUUID();
        System.out.println("Created savings goal with ID: " + mockSavingsGoalUid);
        return mockSavingsGoalUid;
    }

    private void handleMockMoneyTransfer(int roundUpAmountPence, UUID savingsGoalUid) {
        double roundUpAmountPounds = roundUpAmountPence / 100.0;
        System.out.println("Adding £" + String.format("%.2f", roundUpAmountPounds) + " to savings goal: " + savingsGoalUid);
        System.out.println("Transfer completed successfully!");
    }

    // Helper methods for URL construction
    private String buildSavingsGoalUrl(Account account) {
        return BankApiConfig.SAVINGS_GOALS_URL.replace("{accountUid}", account.getAccountUid().toString());
    }

    private String buildAddMoneyUrl(Account account, UUID savingsGoalUid) {
        return BankApiConfig.ADD_MONEY_TO_SAVINGS_GOAL_URL
                .replace("{accountUid}", account.getAccountUid().toString())
                .replace("{savingsGoalUid}", savingsGoalUid.toString())
                .replace("{transferUid}", UUID.randomUUID().toString());
    }

    // Helper methods for request object creation
    private SavingsGoalRequestV2 createSavingsGoalRequest(String savingsGoalName, Currency currency) {
        SavingsGoalRequestV2 request = new SavingsGoalRequestV2();
        request.setName(savingsGoalName);
        request.setCurrency(currency.getCurrencyCode());
        return request;
    }

    private TopUpRequestV2 createTopUpRequest(int roundUpAmountPence, Currency currency) {
        TopUpRequestV2 request = new TopUpRequestV2();
        CurrencyAndAmount amount = createCurrencyAndAmount(roundUpAmountPence, currency);
        request.setAmount(amount);
        return request;
    }

    private CurrencyAndAmount createCurrencyAndAmount(int roundUpAmountPence, Currency currency) {
        CurrencyAndAmount amount = new CurrencyAndAmount();
        amount.setCurrency(currency.getCurrencyCode());
        amount.setMinorUnits(roundUpAmountPence);
        return amount;
    }

    // Helper methods for JSON processing
    private String serializeToJson(Object object) throws BankApiException {
        try {
        ObjectMapper objectMapper = JsonMapper.getObjectMapperInstance();
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new BankApiException(JSON_PROCESSING_ERROR);
        }
    }

    private UUID extractSavingsGoalUidFromResponse(String responseBody) throws BankApiException {
        try {
            ObjectMapper objectMapper = JsonMapper.getObjectMapperInstance();
            JsonNode responseNode = objectMapper.readTree(responseBody);
            String savingsGoalUid = responseNode.get("savingsGoalUid").asText();
            return UUID.fromString(savingsGoalUid);
        } catch (JsonProcessingException e) {
            throw new BankApiException("Failed to parse response JSON");
        }
    }

    protected String sendPutRequest(String urlPath, String requestBody) throws BankApiException {
        try {
            String fullUrl = BankApiConfig.BASE_URL + urlPath;

            java.net.URL url = new java.net.URL(fullUrl);
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();

            setupConnection(connection);
            writeRequestBody(connection, requestBody);
            
            return handleResponse(connection);

        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupConnection(java.net.HttpURLConnection connection) throws ProtocolException {
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + authToken);
            connection.setRequestProperty("User-Agent", "Fintech RoundUp Demo");
    }

    private void writeRequestBody(java.net.HttpURLConnection connection, String requestBody) throws IOException {
            if (requestBody != null) {
                java.io.OutputStream os = connection.getOutputStream();
                os.write(requestBody.getBytes("UTF-8"));
                os.close();
        }
            }

    private String handleResponse(java.net.HttpURLConnection connection) throws IOException, BankApiException {
            connection.connect();
            int responseCode = connection.getResponseCode();
            System.out.println("Response code: " + responseCode);

            java.io.InputStream inputStream =
                    (responseCode >= 400) ? connection.getErrorStream() : connection.getInputStream();

        String responseBody = readResponseBody(inputStream);
        System.out.println("Response body: " + responseBody);

        if (responseCode == 200 || responseCode == 201) {
            return responseBody;
        } else {
            throw new BankApiException("API Error: " + responseCode + " - " + responseBody);
        }
    }

    private String readResponseBody(java.io.InputStream inputStream) throws IOException {
            java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(inputStream));

            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();

        return responseBuilder.toString();
    }
}