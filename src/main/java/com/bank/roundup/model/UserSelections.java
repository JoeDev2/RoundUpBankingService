package com.bank.roundup.model;

import com.bank.roundup.util.RoundUpConstants;
import java.time.LocalDate;

/**
 * Used to model the selections a user makes via the CLI which can then be used to inform the program flow based on user preference
 * Contains validation constraints for user input
 */
public class UserSelections {

    // Validation constants
    public static final int MIN_SAVINGS_GOAL_NAME_LENGTH = RoundUpConstants.MIN_SAVINGS_GOAL_NAME_LENGTH;
    public static final int MAX_SAVINGS_GOAL_NAME_LENGTH = RoundUpConstants.MAX_SAVINGS_GOAL_NAME_LENGTH;
    public static final int MIN_MENU_OPTION = RoundUpConstants.MIN_MENU_OPTION;
    public static final int MAX_MENU_OPTION = RoundUpConstants.MAX_MENU_OPTION;

    private LocalDate startDate;

    // Default to false as most likely will be creating new goal
    private boolean isUsingExistingGoal = false;

    private String savingsGoalName;

    public UserSelections(LocalDate startDate, boolean isUsingExistingGoal, String savingsGoalName) {
        this.startDate = startDate;
        this.isUsingExistingGoal = isUsingExistingGoal;
        this.savingsGoalName = savingsGoalName;
    }

    public UserSelections(LocalDate startDate, boolean isUsingExistingGoal) {
        this.startDate = startDate;
        this.isUsingExistingGoal = isUsingExistingGoal;
    }

    public UserSelections() {}

    public LocalDate getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public boolean getIsUsingExistingGoal() {
        return this.isUsingExistingGoal;
    }

    public void setIsUsingExistingGoal(boolean isUsingExistingGoal) {
        this.isUsingExistingGoal = isUsingExistingGoal;
    }
    

    public String getSavingsGoalName() {
        return this.savingsGoalName;
    }
    
    public void setSavingsGoalName(String savingsGoalName) {
        this.savingsGoalName = savingsGoalName;
    }
}