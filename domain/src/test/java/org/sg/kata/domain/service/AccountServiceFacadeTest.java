package org.sg.kata.domain.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.LENIENT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.sg.kata.domain.core.AccountOperation;
import org.sg.kata.domain.core.AccountStatement;
import org.sg.kata.domain.exceptions.InsufficientBalanceException;
import org.sg.kata.domain.model.AccountBalance;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = LENIENT)
class AccountServiceFacadeTest {

  @Mock AccountStatement accountStatement;

  @Mock AccountOperation accountOperation;

  @InjectMocks AccountServiceFacade accountServiceFacade;

  @BeforeEach
  void setUp() throws InsufficientBalanceException {
    doNothing().when(accountStatement).printTo(any(), any());
    when(accountOperation.deposit(any(), any())).thenReturn(new AccountBalance());
    when(accountOperation.withdraw(any(), any())).thenReturn(new AccountBalance());
  }

  @Test
  void printTo_WhenCalled_thenVerify() {
    accountServiceFacade.printTo(any(), any());
    verify(accountStatement, times(1)).printTo(any(), any());
  }

  @Test
  void deposit_WhenCalled_thenVerify() throws InsufficientBalanceException {
    accountServiceFacade.deposit(any(), any());
    verify(accountOperation, times(1)).deposit(any(), any());
  }

  @Test
  void withdrawal_WhenCalled_thenVerify() throws InsufficientBalanceException {
    accountServiceFacade.withdrawal(any(), any());
    verify(accountOperation, times(1)).withdraw(any(), any());
  }
}
