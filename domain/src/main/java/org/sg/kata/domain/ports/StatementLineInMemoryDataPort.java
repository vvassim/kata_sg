package org.sg.kata.domain.ports;

import java.util.List;
import org.sg.kata.domain.model.StatementLine;

public interface StatementLineInMemoryDataPort {

  void createAccountStatement(long accountId, StatementLine statementLine);

  List<StatementLine> getAccountStatementsById(long accountId);
}
