package com.bank.roundup.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bank.roundup.model.Account;
import com.bank.roundup.model.Transaction;
import com.bank.roundup.config.BankApiConfig;
import com.bank.roundup.exception.BankApiException;
import com.bank.roundup.model.TransactionDirection;
import com.bank.roundup.util.JsonMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TransactionService for business logic involving transactions -- used to retrieve transactions and calculate roundup amounts
 * 
 * For demonstration purposes, this service uses mock data instead of real API calls.
 * Extends the abstract service class to reduce code repetition across each service and make adding new services easier
 */
public class TransactionService extends BankAbstractApiService implements TransactionServiceInterface {

    private static final boolean USE_MOCK_DATA = true;

    public TransactionService(String authToken) {
        super(authToken);
    }

    public List<Transaction> getTransactionsForTimePeriod(LocalDate startDate,
                                                          LocalDate endDate,
                                                          Account account
    ) throws BankApiException, JsonProcessingException {

        if (USE_MOCK_DATA) {
            // Use mock data for demonstration
            System.out.println("Using mock data for transactions from " + startDate + " to " + endDate);
            return MockDataService.generateMockTransactions(startDate, endDate, account);
        }

        // Original API call implementation (kept for reference)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        String transactionsUrl = BankApiConfig.TRANSACTIONS_URL;

        transactionsUrl = transactionsUrl.replace("{accountUid}", account.getAccountUid().toString());
        transactionsUrl = transactionsUrl.replace("{categoryUid}", account.getDefaultCategory().toString());

        String minimumTimeStamp = startDate.atStartOfDay().atZone(ZoneOffset.UTC).format(formatter);
        String maximumTimeStamp = endDate.atTime(23, 59, 59,999_999_999).atZone(ZoneOffset.UTC).format(formatter);

        transactionsUrl = transactionsUrl + "?minTransactionTimestamp=" + minimumTimeStamp + "&maxTransactionTimestamp=" + maximumTimeStamp;

        String responseBody = sendGetRequest(transactionsUrl);

        // parse response body to list of transactions
        ObjectMapper objectMapper = JsonMapper.getObjectMapperInstance();

        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode transactionsNode = rootNode.get("feedItems");

        // Convert the transactions array to a List<Transactions>
        List<Transaction> transactions = Arrays.asList(objectMapper.readValue(transactionsNode.toString(), Transaction[].class));

        // filter by date
        return transactions.stream()
                .filter(transaction -> {
                    LocalDate transactionDate = transaction.convertStringTransactionTimeToLocalDate();

                    boolean inDateRange = transactionDate.isEqual(startDate) || transaction.convertStringTransactionTimeToLocalDate().isAfter(startDate)
                            && transaction.convertStringTransactionTimeToLocalDate().isEqual(endDate) || transaction.convertStringTransactionTimeToLocalDate().isBefore(endDate);

                    boolean isOutgoing = transaction.getDirection() == TransactionDirection.OUT;

                    return inDateRange && isOutgoing;

                }

                )

                .collect(Collectors.toList());

    }

    public BigDecimal calculateRoundUpAmount(List<Transaction> transactions) {

        BigDecimal totalRoundUpAmount = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            int minorUnits = transaction.getAmount().getMinorUnits();

            BigDecimal initialpounds = new BigDecimal(minorUnits).divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);

            BigDecimal roundedUpAmount = initialpounds.setScale(0, RoundingMode.CEILING);

            BigDecimal roundedUpDifference = roundedUpAmount.subtract(initialpounds);

            totalRoundUpAmount = totalRoundUpAmount.add(roundedUpDifference);
        }

        return totalRoundUpAmount;

    }
}