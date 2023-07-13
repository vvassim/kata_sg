package org.sg.kata.application.cli;

import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.sg.kata.domain.core.AccountOperation;
import org.sg.kata.domain.core.AccountStatement;
import org.sg.kata.domain.core.AccountTransactionManager;
import org.sg.kata.domain.core.BalanceManger;
import org.sg.kata.domain.exceptions.InsufficientBalanceException;
import org.sg.kata.domain.model.Account;
import org.sg.kata.domain.ports.AccountInMemoryDataPort;
import org.sg.kata.domain.ports.StatementLineInMemoryDataPort;
import org.sg.kata.domain.ports.StatementPrinterLogPort;
import org.sg.kata.domain.service.AccountServiceFacade;

@Slf4j
public class AccountCli {
  private final AccountServiceFacade accountServiceFacade;
  private final AccountInMemoryDataPort accountInMemoryDataPort;
  private final StatementPrinterLogPort statementPrinterLogPort;

  public AccountCli(
      StatementLineInMemoryDataPort statementLineInMemoryDataAdapter,
      AccountInMemoryDataPort accountInMemoryDataAdapter,
      StatementPrinterLogPort statementPrinterLogAdapter) {
    this.accountInMemoryDataPort = accountInMemoryDataAdapter;
    this.statementPrinterLogPort = statementPrinterLogAdapter;
    AccountStatement accountStatement = new AccountStatement(statementLineInMemoryDataAdapter);
    AccountOperation accountOperation =
        new AccountOperation(new AccountTransactionManager(), new BalanceManger(accountStatement));
    this.accountServiceFacade = new AccountServiceFacade(accountOperation, accountStatement);
  }

  public void deposit(Account account, BigDecimal amount) {
    try {
      accountServiceFacade.deposit(account, amount);
    } catch (InsufficientBalanceException e) {
      log.error(e.getMessage());
    }
  }

  public Account createAccount(BigDecimal overdraft) {
    return accountInMemoryDataPort.createAccount(overdraft);
  }

  public void withdrawal(Account account, BigDecimal amount) {
    try {
      accountServiceFacade.withdrawal(account, amount);
    } catch (InsufficientBalanceException e) {
      log.error(e.getMessage());
    }
  }

  public void printAllAccountStatements(Account account) {
    accountServiceFacade.printTo(account, statementPrinterLogPort);
  }
}
