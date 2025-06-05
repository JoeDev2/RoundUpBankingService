package com.bank.roundup.ui;

import com.bank.roundup.model.UserSelections;
import com.bank.roundup.service.UserSelectionsValidator;
import com.bank.roundup.service.UserSelectionsValidator.ValidationResult;
import com.bank.roundup.model.Account;
import com.bank.roundup.util.RoundUpConstants;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class CLI {

    private final Scanner scanner;
    private final UserSelectionsValidator validator;

    public CLI() {
        this.scanner = new Scanner(System.in);
        this.validator = new UserSelectionsValidator();
    }

    public void displayWelcomeMessage() {
        System.out.println(RoundUpConstants.WELCOME_MESSAGE);
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public UserSelections askForOptions() throws Exception {
        int selection = askForMenuSelection();
        UserSelections userSelections = new UserSelections();

        if (selection == 1) {
            userSelections.setSavingsGoalName(askForSavingsGoalName());
        } else if (selection == 2) {
            userSelections.setIsUsingExistingGoal(true);
            userSelections.setSavingsGoalName(askForExistingSavingsGoalName());
        }

        userSelections.setStartDate(askForStartDate());

        if (validator.validateUserSelections(userSelections)) {
            return userSelections;
        } else {
            throw new Exception("Invalid selections");
        }
    }

    public Account askForAccountSelection(List<Account> accounts) {
        if (accounts.isEmpty()) {
            System.out.println(RoundUpConstants.NO_ACCOUNTS_FOUND);
            return null;
        }

        if (accounts.size() == 1) {
            System.out.println("Found your account: " + accounts.get(0).getName());
            return accounts.get(0);
        }

        System.out.println(RoundUpConstants.ACCOUNT_SELECTION_PROMPT);
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            System.out.println((i + 1) + ". " + account.getName() + " " + account.getAccountType());
        }

        while (true) {
            String input = scanner.nextLine();
            ValidationResult result = validator.validateAccountSelection(input, accounts.size());
            
            if (result.isValid()) {
                int selectedIndex = Integer.parseInt(input.trim()) - 1;
                return accounts.get(selectedIndex);
            } else {
                System.out.println(result.getErrorMessage());
            }
        }
    }

    private int askForMenuSelection() {
        System.out.println(RoundUpConstants.MENU_QUESTION);
        System.out.println(RoundUpConstants.MENU_OPTION_1);
        System.out.println(RoundUpConstants.MENU_OPTION_2);

        while (true) {
            String input = scanner.nextLine();
            ValidationResult result = validator.validateMenuSelection(input);
            
            if (result.isValid()) {
                return Integer.parseInt(input.trim());
            } else {
                System.out.println(result.getErrorMessage());
            }
        }
    }

    private LocalDate askForStartDate() {
        while (true) {
            System.out.println(RoundUpConstants.DATE_PROMPT);
            String input = scanner.nextLine();
            ValidationResult result = validator.validateDateInput(input);
            
            if (result.isValid()) {
                return LocalDate.parse(input.trim());
            } else {
                System.out.println(result.getErrorMessage());
            }
        }
    }

    private String askForSavingsGoalName() {
        while (true) {
            System.out.println(RoundUpConstants.NEW_GOAL_NAME_PROMPT);
            String input = scanner.nextLine();
            ValidationResult result = validator.validateSavingsGoalNameInput(input);
            
            if (result.isValid()) {
                return input.trim();
            } else {
                System.out.println(result.getErrorMessage());
            }
        }
    }

    private String askForExistingSavingsGoalName() {
        while (true) {
            System.out.println(RoundUpConstants.EXISTING_GOAL_NAME_PROMPT);
            String input = scanner.nextLine();
            ValidationResult result = validator.validateSavingsGoalNameInput(input);
            
            if (result.isValid()) {
                return input.trim();
            } else {
                System.out.println(result.getErrorMessage());
            }
        }
    }

    public void close() {
        scanner.close();
    }
    
}