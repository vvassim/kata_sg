package org.sg.kata.application.adapters;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sg.kata.domain.model.StatementLine;

@ExtendWith(MockitoExtension.class)
class StatementLineInMemoryDataAdapterTest {
  @InjectMocks StatementLineInMemoryDataAdapter statementLineInMemoryDataAdapter;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  StatementLine statementLine;

  @Test
  void creatingAccount_whenCalling_thenCreateAccount() {
    // given
    long accountID = 0;
    // when
    statementLineInMemoryDataAdapter.createAccountStatement(accountID, statementLine);
    // then
    assertFalse(statementLineInMemoryDataAdapter.getAccountStatementsById(accountID).isEmpty());
  }
}
