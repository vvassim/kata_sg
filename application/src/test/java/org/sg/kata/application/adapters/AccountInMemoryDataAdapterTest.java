package org.sg.kata.application.adapters;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sg.kata.domain.model.Account;
import org.sg.kata.domain.model.StatementLine;

@ExtendWith(MockitoExtension.class)
class AccountInMemoryDataAdapterTest {

  @InjectMocks AccountInMemoryDataAdapter accountInMemoryDataAdapter;

  @Test
  void createAccount() {
    // given
    BigDecimal overDraft = new BigDecimal(100);
    // when
    Account account = accountInMemoryDataAdapter.createAccount(new BigDecimal(100));
    // then
    assertEquals(0, account.getId());
    assertEquals(0, account.getOverdraft().compareTo(overDraft));
  }
}
