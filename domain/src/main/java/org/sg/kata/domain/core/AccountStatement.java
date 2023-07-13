package org.sg.kata.domain.core;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sg.kata.domain.model.Account;
import org.sg.kata.domain.model.AccountBalance;
import org.sg.kata.domain.model.AccountTransaction;
import org.sg.kata.domain.model.StatementLine;
import org.sg.kata.domain.ports.StatementLineInMemoryDataPort;
import org.sg.kata.domain.ports.StatementPrinterLogPort;

@RequiredArgsConstructor
@Slf4j
public class AccountStatement {

  private final StatementLineInMemoryDataPort statementLineInMemoryDataPort;

  public List<StatementLine> getStatementLines(Account account) {
    return statementLineInMemoryDataPort.getAccountStatementsById(account.getId());
  }

  public void addStatementLine(
      Account account, AccountTransaction accountTransaction, AccountBalance accountBalance) {
    StatementLine statementLine =
        new StatementLine(accountTransaction, new AccountBalance(accountBalance));
    statementLineInMemoryDataPort.createAccountStatement(account.getId(), statementLine);
  }

  public void printTo(Account account, StatementPrinterLogPort statementPrinterLogPort) {
    statementPrinterLogPort.printStatementLines(getStatementLines(account));
  }
}
