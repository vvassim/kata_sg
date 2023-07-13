package org.sg.kata.domain.core;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.quality.Strictness.LENIENT;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
import org.sg.kata.domain.model.enums.AccountTransactionType;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = LENIENT)
class BalanceMangerTest {

  @Mock AccountStatement accountStatement;

  @InjectMocks BalanceManger balanceManger;

  @BeforeEach
  void setUp() {
    doNothing()
        .when(accountStatement)
        .addStatementLine(
            isA(Account.class), isA(AccountTransaction.class), isA(AccountBalance.class));
  }

  @Test
  void createOrUpdateCurrentBalance_whenCalled_thenDeposit() throws Exception {
    Account account = Account.builder().id(1L).overdraft(new BigDecimal(0)).build();
    OffsetDateTime transactionTime = OffsetDateTime.now();
    BigDecimal transactionAmount = new BigDecimal(100);
    AccountTransactionType accountTransactionType = AccountTransactionType.DEPOSIT;
    AccountTransaction transaction =
        AccountTransaction.builder()
            .transactionDateTime(transactionTime)
            .type(accountTransactionType)
            .amount(transactionAmount)
            .build();
    AccountBalance currentBalance =
        balanceManger.createOrUpdateCurrentBalance(account, transaction);
    assertEquals(0, currentBalance.getDebit().compareTo(transactionAmount));
  }

  @Test
  void createOrUpdateCurrentBalance_whenCalled_thenWithdraw() throws Exception {
    Account account = Account.builder().id(1L).overdraft(new BigDecimal(100)).build();
    OffsetDateTime transactionTime = OffsetDateTime.now();
    BigDecimal transactionAmount = new BigDecimal(100);
    AccountTransactionType accountTransactionType = AccountTransactionType.WITHDRAW;
    AccountTransaction transaction =
        AccountTransaction.builder()
            .transactionDateTime(transactionTime)
            .type(accountTransactionType)
            .amount(transactionAmount)
            .build();
    AccountBalance currentBalance =
        balanceManger.createOrUpdateCurrentBalance(account, transaction);
    assertEquals(0, currentBalance.getCredit().compareTo(transactionAmount));
  }

  @Test
  void createOrUpdateCurrentBalance_WhenInsufficientBalance__thenThrowException() {
    // given
    Account account = Account.builder().id(1L).overdraft(new BigDecimal(0)).build();
    OffsetDateTime transactionTime = OffsetDateTime.now();
    BigDecimal transactionAmount = new BigDecimal(100);
    AccountTransactionType accountTransactionType = AccountTransactionType.WITHDRAW;
    AccountTransaction transaction =
        AccountTransaction.builder()
            .transactionDateTime(transactionTime)
            .type(accountTransactionType)
            .amount(transactionAmount)
            .build();
    // when/then
    Throwable exception =
        assertThrows(
            InsufficientBalanceException.class,
            () -> balanceManger.createOrUpdateCurrentBalance(account, transaction));
    assertEquals("Operation failed do to Insufficient Balance", exception.getMessage());
  }

  @Test
  void createOrUpdateCurrentBalance_WhenInsufficientBalance_thenThrowException() throws Exception {
    // given
    Account account = Account.builder().id(1L).overdraft(new BigDecimal(0)).build();
    account.setBalance(new AccountBalance());
    // when/then
    assertTrue(balanceManger.getCurrentBalanceForAccount(account).isPresent());
  }
}
