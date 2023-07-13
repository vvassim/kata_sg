package org.sg.kata.domain.core;

import java.math.BigDecimal;
import java.util.Optional;
import org.sg.kata.domain.exceptions.InsufficientBalanceException;
import org.sg.kata.domain.model.Account;
import org.sg.kata.domain.model.AccountBalance;
import org.sg.kata.domain.model.AccountTransaction;
import org.sg.kata.domain.model.enums.AccountTransactionStatus;

public class BalanceManger {

  private final AccountStatement accountStatement;

  public BalanceManger(AccountStatement accountStatement) {
    this.accountStatement = accountStatement;
  }

  /**
   * @param account the account
   * @param accountTransaction the corresponding account transaction
   * @return the account balance
   * @throws InsufficientBalanceException if account balance is Insufficient for the transaction
   */
  public AccountBalance createOrUpdateCurrentBalance(
      Account account, AccountTransaction accountTransaction) throws InsufficientBalanceException {
    Optional<AccountBalance> currentBalance = getCurrentBalanceForAccount(account);
    AccountBalance accountBalance =
        currentBalance.orElseGet(
            () -> {
              account.setBalance(
                  AccountBalance.builder()
                      .credit(new BigDecimal(0))
                      .debit(new BigDecimal(0))
                      .build());
              return account.getBalance();
            });
    switch (accountTransaction.getType()) {
      case WITHDRAW:
        handleWithdrawTransaction(account, accountBalance, accountTransaction);
        break;
      case DEPOSIT:
        handleDepositTransaction(accountBalance, accountTransaction);
        break;
    }
    accountStatement.addStatementLine(account, accountTransaction, accountBalance);
    return accountBalance;
  }

  public Optional<AccountBalance> getCurrentBalanceForAccount(Account account) {
    return Optional.ofNullable(account.getBalance());
  }

  private boolean checkingBalanceSufficiency(
      AccountBalance accountBalance, BigDecimal transactionAmount, BigDecimal overdraft) {
    return accountBalance
            .getDebit()
            .add(overdraft)
            .compareTo(accountBalance.getCredit().add(transactionAmount))
        >= 0;
  }

  private void handleWithdrawTransaction(
      Account account, AccountBalance accountBalance, AccountTransaction accountTransaction)
      throws InsufficientBalanceException {
    if (checkingBalanceSufficiency(
        accountBalance, accountTransaction.getAmount(), account.getOverdraft())) {
      accountBalance.setCredit(accountBalance.getCredit().add(accountTransaction.getAmount()));
      accountTransaction.setStatus(AccountTransactionStatus.EXECUTED);
    } else {
      accountTransaction.setStatus(AccountTransactionStatus.FAILED);
      accountStatement.addStatementLine(account, accountTransaction, accountBalance);
      throw new InsufficientBalanceException("Operation failed do to Insufficient Balance");
    }
  }

  private void handleDepositTransaction(
      AccountBalance accountBalance, AccountTransaction accountTransaction) {
    accountBalance.setDebit(accountBalance.getDebit().add(accountTransaction.getAmount()));
    accountTransaction.setStatus(AccountTransactionStatus.EXECUTED);
  }
}
