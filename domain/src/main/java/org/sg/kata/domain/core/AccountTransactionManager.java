package org.sg.kata.domain.core;

import static org.sg.kata.domain.model.enums.AccountTransactionStatus.PENDING;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.sg.kata.domain.model.Account;
import org.sg.kata.domain.model.AccountTransaction;
import org.sg.kata.domain.model.enums.AccountTransactionType;

public class AccountTransactionManager {

  public AccountTransaction createTransaction(
      Account account,
      BigDecimal amount,
      OffsetDateTime transactionDateTime,
      AccountTransactionType type) {
    Long id = account.getTransactions().size() + 1L;
    AccountTransaction accountTransaction =
        AccountTransaction.builder()
            .id(id)
            .transactionDateTime(transactionDateTime)
            .type(type)
            .amount(amount)
            .status(PENDING)
            .build();
    account.getTransactions().add(accountTransaction);
    return accountTransaction;
  }
}
