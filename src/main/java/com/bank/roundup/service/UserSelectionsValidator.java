package com.bank.roundup.service;

import com.bank.roundup.model.UserSelections;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Centralized validation for all user input related to UserSelections
 * Contains both individual field validation and complete object validation
 */
public class UserSelectionsValidator {

    public UserSelectionsValidator() {}

    /**
     * Validates the complete UserSelections object
     */
    public boolean validateUserSelections(UserSelections selections) {
        if (selections == null) {
            return false;
        }
        
        if (!isValidStartDate(selections.getStartDate())) {
            return false;
        }
        
        if (!selections.getIsUsingExistingGoal() && !isValidSavingsGoalName(selections.getSavingsGoalName())) {
            return false;
        }
        
        return true;
    }

    /**
     * Validates menu selection input
     */
    public ValidationResult validateMenuSelection(String input) {
        if (input == null || input.trim().isEmpty()) {
            return ValidationResult.failure("Please enter a number");
        }

        try {
            int selection = Integer.parseInt(input.trim());
            if (selection < UserSelections.MIN_MENU_OPTION || selection > UserSelections.MAX_MENU_OPTION) {
                return ValidationResult.failure("Please enter 1 or 2");
            }
            return ValidationResult.success();
        } catch (NumberFormatException e) {
            return ValidationResult.failure("Please enter a valid number");
        }
    }

    /**
     * Validates date string input
     */
    public ValidationResult validateDateInput(String dateInput) {
        if (dateInput == null || dateInput.trim().isEmpty()) {
            return ValidationResult.failure("Date cannot be empty");
        }

        try {
            LocalDate date = LocalDate.parse(dateInput.trim());
            if (date.isAfter(LocalDate.now())) {
                return ValidationResult.failure("Date cannot be in the future");
            }
            return ValidationResult.success();
        } catch (DateTimeParseException e) {
            return ValidationResult.failure("Invalid date format. Please use yyyy-MM-dd format");
        }
    }

    /**
     * Validates savings goal name input
     */
    public ValidationResult validateSavingsGoalNameInput(String name) {
        if (name == null || name.trim().isEmpty()) {
            return ValidationResult.failure("Savings goal name cannot be empty");
        }

        String trimmedName = name.trim();
        
        if (trimmedName.length() < UserSelections.MIN_SAVINGS_GOAL_NAME_LENGTH) {
            return ValidationResult.failure("Savings goal name must be at least " + 
                UserSelections.MIN_SAVINGS_GOAL_NAME_LENGTH + " characters");
        }
        
        if (trimmedName.length() > UserSelections.MAX_SAVINGS_GOAL_NAME_LENGTH) {
            return ValidationResult.failure("Savings goal name cannot exceed " + 
                UserSelections.MAX_SAVINGS_GOAL_NAME_LENGTH + " characters");
        }
        
        return ValidationResult.success();
    }

    /**
     * Validates account selection input
     */
    public ValidationResult validateAccountSelection(String input, int maxAccounts) {
        if (input == null || input.trim().isEmpty()) {
            return ValidationResult.failure("Please enter a number");
        }

        try {
            int selection = Integer.parseInt(input.trim());
            if (selection < 1 || selection > maxAccounts) {
                return ValidationResult.failure("Please enter a number between 1 and " + maxAccounts);
            }
            return ValidationResult.success();
        } catch (NumberFormatException e) {
            return ValidationResult.failure("Please enter a valid number");
        }
    }

    // Private helper methods
    private boolean isValidStartDate(LocalDate startDate) {
        return startDate != null && !startDate.isAfter(LocalDate.now());
    }

    private boolean isValidSavingsGoalName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        String trimmedName = name.trim();
        return trimmedName.length() >= UserSelections.MIN_SAVINGS_GOAL_NAME_LENGTH && 
               trimmedName.length() <= UserSelections.MAX_SAVINGS_GOAL_NAME_LENGTH;
    }

    /**
     * Result class for validation operations
     */
    public static class ValidationResult {
        private final boolean isValid;
        private final String errorMessage;

        private ValidationResult(boolean isValid, String errorMessage) {
            this.isValid = isValid;
            this.errorMessage = errorMessage;
        }

        public static ValidationResult success() {
            return new ValidationResult(true, null);
        }

        public static ValidationResult failure(String errorMessage) {
            return new ValidationResult(false, errorMessage);
        }

        public boolean isValid() {
            return isValid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}