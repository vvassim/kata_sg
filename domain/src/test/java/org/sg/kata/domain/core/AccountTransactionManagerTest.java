package org.sg.kata.domain.core;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sg.kata.domain.model.Account;
import org.sg.kata.domain.model.AccountTransaction;
import org.sg.kata.domain.model.enums.AccountTransactionType;

@ExtendWith(MockitoExtension.class)
class AccountTransactionManagerTest {

  @InjectMocks AccountTransactionManager accountTransactionManager;

  @Test
  void createTransaction_whenCalled_thenTransaction() {
    // given
    Account account = Account.builder().id(1L).overdraft(new BigDecimal(1)).build();
    OffsetDateTime transactionTime = OffsetDateTime.now();
    BigDecimal transactionAmount = new BigDecimal(100);
    AccountTransactionType accountTransactionType = AccountTransactionType.DEPOSIT;
    // when
    AccountTransaction transaction =
        accountTransactionManager.createTransaction(
            account, transactionAmount, transactionTime, accountTransactionType);
    // then
    assertFalse(account.getTransactions().isEmpty());
    assertEquals(transactionAmount, transaction.getAmount());
    assertEquals(transactionTime, transaction.getTransactionDateTime());
  }
}
