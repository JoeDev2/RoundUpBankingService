package com.bank.roundup;

import com.bank.roundup.model.Account;
import com.bank.roundup.model.Transaction;
import com.bank.roundup.model.UserSelections;
import com.bank.roundup.service.AccountServiceInterface;
import com.bank.roundup.service.SavingsGoalServiceInterface;
import com.bank.roundup.service.TransactionServiceInterface;
import com.bank.roundup.ui.CLI;
import com.bank.roundup.util.RoundUpConstants;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

public class RoundUp {

    private final CLI cli;
    private final AccountServiceInterface accountService;
    private final TransactionServiceInterface transactionService;
    private final SavingsGoalServiceInterface savingsGoalService;

    public RoundUp(CLI cli, 
                   AccountServiceInterface accountService,
                   TransactionServiceInterface transactionService, 
                   SavingsGoalServiceInterface savingsGoalService) {
        this.cli = cli;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.savingsGoalService = savingsGoalService;
    }

    public void startRoundUp() throws Exception {
        cli.displayWelcomeMessage();

        //  get user instructions
        UserSelections selections = cli.askForOptions();

        //  get accounts the user holds based on token
        List<Account> userAccounts = accountService.getAccountsForUser();

        //  get transactions for the specified week
        LocalDate todayDate = LocalDate.now();
        LocalDate endDate = selections.getStartDate().plusDays(RoundUpConstants.DEFAULT_DATE_RANGE_DAYS - 1);

        if (endDate.isAfter(todayDate)) {
            endDate = todayDate;
        }

        Account selectedAccount = cli.askForAccountSelection(userAccounts);

        List<Transaction> transactions = transactionService.getTransactionsForTimePeriod(
                selections.getStartDate(),
                endDate,
                selectedAccount
        );

        System.out.println("Quantity of transactions: " + transactions.size());

        //  compute roundup
        BigDecimal roundUpAmountPounds = transactionService.calculateRoundUpAmount(transactions);
        int roundUpAmountPence = roundUpAmountPounds.multiply(RoundUpConstants.HUNDRED).intValue();

        System.out.println("Amount to be saved from roundup: " + roundUpAmountPence + "p");

        //  create a savings goal
        //  initially intended to get existing goal from API - could be implemented in the future
        if (!selections.getIsUsingExistingGoal()) {

            // defaults to GBP for now - in future could determine dynamically based on account region or transaction origin region
            Currency defaultCurrency = Currency.getInstance(RoundUpConstants.DEFAULT_CURRENCY_CODE);
            UUID savingsGoalUid = savingsGoalService.createSavingsGoal(selections.getSavingsGoalName(), defaultCurrency, selectedAccount);

            savingsGoalService.addSavedMoneyToSavingsGoal(roundUpAmountPence, defaultCurrency, savingsGoalUid, selectedAccount);

        } else {
            cli.displayMessage(RoundUpConstants.FEATURE_COMING_SOON);
        }
    }
}