package org.sg.kata.domain.core;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sg.kata.domain.model.Account;
import org.sg.kata.domain.model.AccountBalance;
import org.sg.kata.domain.model.AccountTransaction;
import org.sg.kata.domain.model.StatementLine;
import org.sg.kata.domain.ports.StatementLineInMemoryDataPort;
import org.sg.kata.domain.ports.StatementPrinterLogPort;

@ExtendWith(MockitoExtension.class)
class AccountStatementTest {

  @Mock
  StatementLineInMemoryDataPort statementLineInMemoryDataPort;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  Account account;

  @Mock
  AccountBalance accountBalance;

  @Mock
  AccountTransaction accountTransaction;

  @Mock
  StatementPrinterLogPort statementPrinterLogPort;

  @InjectMocks AccountStatement accountStatement;

  @Test
  void getStatementLines_WhenCalled_ThenVerify() {
    // when
    accountStatement.getStatementLines(account);
    // then
    verify(statementLineInMemoryDataPort).getAccountStatementsById(isA(Long.class));
  }

  @Test
  void addStatementLine_whenCalled_thenVerify() {
    // given
    when(accountBalance.getCredit()).thenReturn(new BigDecimal(0));
    when(accountBalance.getDebit()).thenReturn(new BigDecimal(0));
    // when
    accountStatement.addStatementLine(account, accountTransaction, accountBalance);
    // then
    verify(statementLineInMemoryDataPort)
        .createAccountStatement(any(Long.class), any(StatementLine.class));
  }

  @Test
  void printTo_WhenCalled_ThenVerify() {
    when(statementLineInMemoryDataPort.getAccountStatementsById(isA(Long.class)))
        .thenReturn(new ArrayList<>());
    // when
    accountStatement.printTo(account, statementPrinterLogPort);
    // then
    verify(statementPrinterLogPort).printStatementLines(anyList());
  }
}
