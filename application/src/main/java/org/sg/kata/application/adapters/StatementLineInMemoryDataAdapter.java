package org.sg.kata.application.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.sg.kata.domain.model.StatementLine;
import org.sg.kata.domain.ports.StatementLineInMemoryDataPort;

public class StatementLineInMemoryDataAdapter implements StatementLineInMemoryDataPort {
  private final Map<Long, List<StatementLine>> statementLines = new ConcurrentHashMap<>();

  @Override
  public void createAccountStatement(long accountId, StatementLine statementLine) {
    statementLines.compute(
        accountId,
        (acc, statementLineList) -> {
          List<StatementLine> statementLineRef =
              Optional.ofNullable(statementLineList).orElseGet(ArrayList::new);
          statementLineRef.add(0, statementLine);
          return statementLineRef;
        });
  }

  @Override
  public List<StatementLine> getAccountStatementsById(long accountId) {
    return statementLines.get(accountId);
  }
}
