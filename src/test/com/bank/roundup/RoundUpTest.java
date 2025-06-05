package com.bank.roundup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.bank.roundup.ui.CLI;
import com.bank.roundup.service.AccountServiceInterface;
import com.bank.roundup.service.TransactionServiceInterface;
import com.bank.roundup.service.SavingsGoalServiceInterface;
import com.bank.roundup.model.UserSelections;
import com.bank.roundup.model.Account;
import com.bank.roundup.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@RunWith(MockitoJUnitRunner.class)
public class RoundUpTest {

    @Mock
    private CLI mockCli;
    
    @Mock
    private AccountServiceInterface mockAccountService;
    
    @Mock
    private TransactionServiceInterface mockTransactionService;
    
    @Mock
    private SavingsGoalServiceInterface mockSavingsGoalService;

    @Test
    public void shouldCalculateCorrectRoundUpAndCreateSavingsGoal() throws Exception {
        // Given
        UserSelections mockSelections = new UserSelections();
        mockSelections.setStartDate(LocalDate.now().minusDays(7));
        mockSelections.setSavingsGoalName("Test Goal");
        mockSelections.setIsUsingExistingGoal(false);

        Account mockAccount = new Account();
        List<Account> accounts = Arrays.asList(mockAccount);
        
        Transaction mockTransaction = new Transaction();
        Transaction.Amount amount = new Transaction.Amount();
        amount.setMinorUnits(235); // £2.35
        mockTransaction.setAmount(amount);
        List<Transaction> transactions = Arrays.asList(mockTransaction);

        UUID mockSavingsGoalUid = UUID.randomUUID();

        // When
        when(mockCli.askForOptions()).thenReturn(mockSelections);
        when(mockAccountService.getAccountsForUser()).thenReturn(accounts);
        when(mockCli.askForAccountSelection(accounts)).thenReturn(mockAccount);
        when(mockTransactionService.getTransactionsForTimePeriod(any(), any(), eq(mockAccount)))
            .thenReturn(transactions);
        when(mockTransactionService.calculateRoundUpAmount(transactions))
            .thenReturn(new BigDecimal("0.65")); // 65p roundup
        when(mockSavingsGoalService.createSavingsGoal(eq("Test Goal"), any(), eq(mockAccount)))
            .thenReturn(mockSavingsGoalUid);

        RoundUp roundUp = new RoundUp(mockCli, mockAccountService, mockTransactionService, mockSavingsGoalService);
        
        // Then
        roundUp.startRoundUp();

        // Verify interactions
        verify(mockAccountService).getAccountsForUser();
        verify(mockTransactionService).getTransactionsForTimePeriod(any(), any(), eq(mockAccount));
        verify(mockTransactionService).calculateRoundUpAmount(transactions);
        verify(mockSavingsGoalService).createSavingsGoal("Test Goal", Currency.getInstance("GBP"), mockAccount);
        verify(mockSavingsGoalService).addSavedMoneyToSavingsGoal(65, Currency.getInstance("GBP"), mockSavingsGoalUid, mockAccount);
    }

    @Test
    public void shouldHandleExistingSavingsGoal() throws Exception {
        // Given
        UserSelections mockSelections = new UserSelections();
        mockSelections.setIsUsingExistingGoal(true);
        mockSelections.setStartDate(LocalDate.now().minusDays(7));
        mockSelections.setSavingsGoalName("Existing Goal");
        
        Account mockAccount = new Account();
        List<Account> accounts = Arrays.asList(mockAccount);
        
        Transaction mockTransaction = new Transaction();
        Transaction.Amount amount = new Transaction.Amount();
        amount.setMinorUnits(235); // £2.35
        mockTransaction.setAmount(amount);
        List<Transaction> transactions = Arrays.asList(mockTransaction);

        when(mockCli.askForOptions()).thenReturn(mockSelections);
        when(mockAccountService.getAccountsForUser()).thenReturn(accounts);
        when(mockCli.askForAccountSelection(accounts)).thenReturn(mockAccount);
        when(mockTransactionService.getTransactionsForTimePeriod(any(), any(), eq(mockAccount)))
            .thenReturn(transactions);
        when(mockTransactionService.calculateRoundUpAmount(transactions))
            .thenReturn(new BigDecimal("0.65")); // 65p roundup

        RoundUp roundUp = new RoundUp(mockCli, mockAccountService, mockTransactionService, mockSavingsGoalService);

        // When
        roundUp.startRoundUp();

        // Then
        verify(mockCli).displayMessage("Feature coming soon, please create a new goal for now");
        verify(mockSavingsGoalService, never()).createSavingsGoal(anyString(), any(), any());
    }
}
