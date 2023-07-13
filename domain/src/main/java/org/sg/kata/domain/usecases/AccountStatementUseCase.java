package org.sg.kata.domain.usecases;

import org.sg.kata.domain.model.Account;
import org.sg.kata.domain.ports.StatementPrinterLogPort;

public interface AccountStatementUseCase {
  void printTo(Account account, StatementPrinterLogPort statementPrinterLogPort);
}
