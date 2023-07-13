package org.sg.kata.domain.service;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.sg.kata.domain.core.AccountOperation;
import org.sg.kata.domain.core.AccountStatement;
import org.sg.kata.domain.exceptions.InsufficientBalanceException;
import org.sg.kata.domain.model.Account;
import org.sg.kata.domain.model.AccountBalance;
import org.sg.kata.domain.ports.StatementPrinterLogPort;
import org.sg.kata.domain.usecases.AccountStatementUseCase;
import org.sg.kata.domain.usecases.DepositUseCase;
import org.sg.kata.domain.usecases.WithdrawalUseCase;

@RequiredArgsConstructor
public class AccountServiceFacade
    implements DepositUseCase, WithdrawalUseCase, AccountStatementUseCase {

  private final AccountOperation accountOperation;

  private final AccountStatement accountStatement;

  @Override
  public void printTo(Account account, StatementPrinterLogPort statementPrinterLogPort) {
    accountStatement.printTo(account, statementPrinterLogPort);
  }

  @Override
  public AccountBalance deposit(Account account, BigDecimal amount)
      throws InsufficientBalanceException {
    return accountOperation.deposit(account, amount);
  }

  @Override
  public AccountBalance withdrawal(Account account, BigDecimal amount)
      throws InsufficientBalanceException {
    return accountOperation.withdraw(account, amount);
  }
}
