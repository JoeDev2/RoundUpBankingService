package com.bank.roundup;

import com.bank.roundup.ui.CLI;
import com.bank.roundup.service.AccountService;
import com.bank.roundup.service.TransactionService;
import com.bank.roundup.service.SavingsGoalService;

public class RoundUpLauncher {

    private static final String DEMO_TOKEN = "demo-auth-token-12345";

    public static void main(String[] args) throws Exception {
        System.out.println("=== Banking RoundUp Service Demo ===");
        System.out.println("This is a demonstration of a transaction roundup savings service.");
        System.out.println("For demo purposes, mock data is used instead of real banking APIs.");
        System.out.println();

        // Initialize all dependencies
        CLI cli = new CLI();
        AccountService accountService = new AccountService(DEMO_TOKEN);
        TransactionService transactionService = new TransactionService(DEMO_TOKEN);
        SavingsGoalService savingsGoalService = new SavingsGoalService(DEMO_TOKEN);

        // Create RoundUp with proper dependencies
        RoundUp roundUp = new RoundUp(cli, accountService, transactionService, savingsGoalService);

        try {
            roundUp.startRoundUp();
        } catch (Exception e) {
            System.err.println("Error during roundup process: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cli.close();
        }
    }
}