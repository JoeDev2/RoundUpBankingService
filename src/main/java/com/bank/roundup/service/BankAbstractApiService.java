package com.bank.roundup.service;

import com.bank.roundup.exception.BankApiException;
import com.bank.roundup.config.BankApiConfig;

public abstract class BankAbstractApiService {

    protected final String authToken;

    public BankAbstractApiService(String authToken) {
        this.authToken = authToken;
    }

    /**
     * Creates a full URL from the provided path
     */
    protected String buildFullUrl(String urlPath) {
        return BankApiConfig.BASE_URL + urlPath;
    }

    /**
     * Sends a Get request to the Bank API using the provided url path to provide the necessary url parameters
     * 
     * Note: In a production environment, this would use actual HTTP calls.
     * For demonstration purposes, this implementation uses mock data.
     */
    protected String sendGetRequest(String urlPath) throws BankApiException {
        try {
            String fullUrl = buildFullUrl(urlPath);

            java.net.HttpURLConnection connection = createConnection(fullUrl, "GET");

            connection.connect();

            return readResponse(connection);

        } catch (Exception e) {
            throw new BankApiException("Error: " + e.getMessage());
        }
    }

    /**
     * Sets up a connection with common headers
     */
    protected java.net.HttpURLConnection createConnection(String fullUrl, String method) throws java.io.IOException {
        System.out.println("Making " + method + " request to: " + fullUrl);

        java.net.URL url = new java.net.URL(fullUrl);
        java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();

        connection.setRequestMethod(method);

        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + authToken);
        connection.setRequestProperty("User-Agent", "Bank RoundUp Demo");

        if (method.equals("PUT") || method.equals("POST")) {
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
        }

        return connection;
    }

    /**
     * Reads the response from a connection
     */
    protected String readResponse(java.net.HttpURLConnection connection) throws java.io.IOException, BankApiException {
        int responseCode = connection.getResponseCode();
        System.out.println("Response code: " + responseCode);

        java.io.InputStream inputStream =
                (responseCode >= 400) ? connection.getErrorStream() : connection.getInputStream();

        java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(inputStream));

        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line);
        }
        reader.close();

        String responseBody = responseBuilder.toString();
        System.out.println("Response body: " + responseBody);

        if (responseCode == 200 || responseCode == 201) {
            return responseBody;
        } else {
            throw new BankApiException("API Error: " + responseCode + " - " + responseBody);
        }
    }
}