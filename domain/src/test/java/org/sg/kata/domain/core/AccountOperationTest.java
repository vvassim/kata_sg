package org.sg.kata.domain.core;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.quality.Strictness.LENIENT;
import static org.sg.kata.domain.model.enums.AccountTransactionStatus.FAILED;
import static org.sg.kata.domain.model.enums.AccountTransactionType.DEPOSIT;
import static org.sg.kata.domain.model.enums.AccountTransactionType.WITHDRAW;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.sg.kata.domain.exceptions.InsufficientBalanceException;
import org.sg.kata.domain.model.Account;
import org.sg.kata.domain.model.AccountBalance;
import org.sg.kata.domain.model.AccountTransaction;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = LENIENT)
class AccountOperationTest {

  @Mock AccountStatement accountStatement;

  @Mock BalanceManger balanceManger;

  @Mock AccountTransactionManager accountTransactionManager;

  @InjectMocks AccountOperation accountOperation;

  @BeforeEach
  void setUp() {
    doNothing()
        .when(accountStatement)
        .addStatementLine(
            isA(Account.class), isA(AccountTransaction.class), isA(AccountBalance.class));
  }

  @Test
  void deposit_WhenCalled_thenVerify() throws InsufficientBalanceException {
    // given
    Account account = Account.builder().id(1L).overdraft(new BigDecimal(0)).build();
    OffsetDateTime transactionTime = OffsetDateTime.now();
    BigDecimal transactionAmount = new BigDecimal(100);
    AccountTransaction transaction =
        AccountTransaction.builder()
            .transactionDateTime(transactionTime)
            .type(DEPOSIT)
            .amount(transactionAmount)
            .build();
    doReturn(transaction)
        .when(accountTransactionManager)
        .createTransaction(any(), any(), any(), any());
    doReturn(new AccountBalance()).when(balanceManger).createOrUpdateCurrentBalance(any(), any());
    // when
    accountOperation.deposit(account, transactionAmount);
    // then
    verify(accountTransactionManager, times(1)).createTransaction(any(), any(), any(), any());
    verify(balanceManger, times(1)).createOrUpdateCurrentBalance(account, transaction);
  }

  @Test
  void withdraw_WhenCalled_thenVerify() throws InsufficientBalanceException {
    // given
    Account account = Account.builder().id(1L).overdraft(new BigDecimal(0)).build();
    OffsetDateTime transactionTime = OffsetDateTime.now();
    BigDecimal transactionAmount = new BigDecimal(100);
    AccountTransaction transaction =
        AccountTransaction.builder()
            .transactionDateTime(transactionTime)
            .type(WITHDRAW)
            .amount(transactionAmount)
            .build();
    doReturn(transaction)
        .when(accountTransactionManager)
        .createTransaction(any(), any(), any(), any());
    doReturn(new AccountBalance()).when(balanceManger).createOrUpdateCurrentBalance(any(), any());
    // when
    accountOperation.deposit(account, transactionAmount);
    // then
    verify(accountTransactionManager, times(1)).createTransaction(any(), any(), any(), any());
    verify(balanceManger, times(1)).createOrUpdateCurrentBalance(account, transaction);
  }

  @Test
  void
      throwException_WhenWithdrawAndInsufficientBalanceInMultiThreadEnv_ThenThrowInsufficientBalanceException()
          throws InsufficientBalanceException {
    // given
    BigDecimal overDraft = new BigDecimal(200);
    Account account = Account.builder().id(1L).overdraft(overDraft).build();
    OffsetDateTime transactionTime = OffsetDateTime.now();
    BigDecimal transactionAmount = new BigDecimal(200);
    AccountTransaction transaction =
        AccountTransaction.builder()
            .transactionDateTime(transactionTime)
            .type(WITHDRAW)
            .amount(transactionAmount)
            .build();
    doReturn(transaction)
        .when(accountTransactionManager)
        .createTransaction(any(), any(), any(), any());

    Runnable withdrawOperation =
        () -> {
          try {
            accountOperation.withdraw(account, transactionAmount);
          } catch (InsufficientBalanceException e) {
            throw new RuntimeException(e.getMessage());
          }
        };
    // when
    CompletableFuture<Void> withdrawOperation1 = CompletableFuture.runAsync(withdrawOperation);
    CompletableFuture<Void> withdrawOperation2 = CompletableFuture.runAsync(withdrawOperation);
    // then
    try {
      CompletableFuture.allOf(withdrawOperation1, withdrawOperation2).join();
    } catch (RuntimeException e) {
      assertEquals("Operation failed do to Insufficient Balance", e.getCause().getMessage());
      assertTrue(
          withdrawOperation1.isCompletedExceptionally()
              || withdrawOperation1.isCompletedExceptionally());
      assertFalse(account.getTransactions().isEmpty());
      assertTrue(
          account.getTransactions().stream()
              .anyMatch(accountTransaction -> accountTransaction.getStatus().equals(FAILED)));
    }
  }
}
