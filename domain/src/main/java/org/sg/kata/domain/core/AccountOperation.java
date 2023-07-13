package org.sg.kata.domain.core;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.sg.kata.domain.exceptions.InsufficientBalanceException;
import org.sg.kata.domain.model.Account;
import org.sg.kata.domain.model.AccountBalance;
import org.sg.kata.domain.model.AccountTransaction;
import org.sg.kata.domain.model.enums.AccountTransactionType;

@RequiredArgsConstructor
public class AccountOperation {

  private final AccountTransactionManager accountTransactionManager;
  private final BalanceManger balanceManger;

  public synchronized AccountBalance deposit(Account account, BigDecimal amount)
      throws InsufficientBalanceException {
    AccountTransaction transaction =
        accountTransactionManager.createTransaction(
            account, amount, OffsetDateTime.now(), AccountTransactionType.DEPOSIT);
    return balanceManger.createOrUpdateCurrentBalance(account, transaction);
  }

  public synchronized AccountBalance withdraw(Account account, BigDecimal amount)
      throws InsufficientBalanceException {
    AccountTransaction transaction =
        accountTransactionManager.createTransaction(
            account, amount, OffsetDateTime.now(), AccountTransactionType.WITHDRAW);
    return balanceManger.createOrUpdateCurrentBalance(account, transaction);
  }
}
